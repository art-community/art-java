/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.json.descriptor;

import com.fasterxml.jackson.core.*;
import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.json.exception.*;
import io.art.meta.descriptor.Reader;
import io.art.meta.model.*;
import io.art.meta.schema.MetaCreatorTemplate.*;
import io.art.meta.transformer.*;
import lombok.*;
import static com.fasterxml.jackson.core.JsonToken.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.json.constants.JsonModuleConstants.Errors.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.io.*;
import java.util.*;


@AllArgsConstructor
public class JsonModelReader implements Reader {
    private final JsonFactory jsonFactory;

    @Override
    public <T> T read(MetaType<T> type, InputStream json) {
        if (isNull(json)) return null;
        MetaTransformer<T> transformer = type.inputTransformer();
        try (JsonParser parser = jsonFactory.createParser(json)) {
            JsonToken nextToken = parser.nextToken();
            if (isNull(nextToken) || nextToken == VALUE_NULL) return null;
            switch (type.externalKind()) {
                case LAZY:
                    return transformer.fromLazy(() -> read(type.parameters().get(0), json));
                case STRING:
                    return transformer.fromString(parser.getText());
                case LONG:
                    return transformer.fromLong(parser.getLongValue());
                case DOUBLE:
                    return transformer.fromDouble(parser.getDoubleValue());
                case FLOAT:
                    return transformer.fromFloat(parser.getFloatValue());
                case INTEGER:
                    return transformer.fromInteger(parser.getIntValue());
                case BOOLEAN:
                    return transformer.fromBoolean(parser.getBooleanValue());
                case CHARACTER:
                    String text = parser.getText();
                    if (isEmpty(text)) return null;
                    return transformer.fromCharacter(text.charAt(0));
                case SHORT:
                    return transformer.fromShort(parser.getShortValue());
                case BYTE:
                    return transformer.fromByte(parser.getByteValue());
                case BINARY:
                    return transformer.fromByteArray(parser.getBinaryValue());
                case MAP:
                case LAZY_MAP:
                    return transformer.fromMap(parseMap(type, parser));
                case ARRAY:
                case LAZY_ARRAY:
                    return transformer.fromArray(parseArray(type, parser));
                case ENTITY:
                    return cast(parseEntity(type, parser));
            }
            throw new ImpossibleSituationException();
        } catch (Throwable throwable) {
            throw new JsonException(throwable);
        }
    }


    private static Object parseEntity(MetaType<?> type, JsonParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        MetaClass<?> definition = type.declaration();
        MetaCreatorInstance creator = definition.creator().instantiate();
        ImmutableMap<String, MetaProperty<?>> properties = creator.properties();
        do {
            if (currentToken == END_OBJECT) {
                return creator.create();
            }
            if (currentToken != FIELD_NAME) {
                currentToken = parser.nextToken();
                continue;
            }
            String field = parser.getCurrentName();
            if (isEmpty(field)) {
                currentToken = parser.nextToken();
                continue;
            }
            MetaProperty<?> property = properties.get(field);
            if (isNull(property)) {
                currentToken = parser.nextToken();
                continue;
            }
            MetaType<?> propertyType = property.type();
            if (propertyType.externalKind() == LAZY) {
                propertyType = propertyType.parameters().get(0);
            }
            switch (currentToken = parser.nextToken()) {
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case VALUE_EMBEDDED_OBJECT:
                case VALUE_NULL:
                case END_ARRAY:
                    break;
                default:
                    creator.put(property, parseField(propertyType, field, parser));
                    break;
            }
        } while (!parser.isClosed());
        return creator.create();
    }

    private static Map<?, ?> parseMap(MetaType<?> type, JsonParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        MetaType<?> valueType = type.parameters().get(1);
        if (valueType.externalKind() == LAZY) {
            valueType = valueType.parameters().get(0);
        }
        Map<Object, Object> map = map();
        do {
            if (currentToken == END_OBJECT) {
                return map;
            }
            if (currentToken != FIELD_NAME) {
                currentToken = parser.nextToken();
                continue;
            }
            String field = parser.getCurrentName();
            if (isEmpty(field)) {
                currentToken = parser.nextToken();
                continue;
            }
            switch (currentToken = parser.nextToken()) {
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case VALUE_EMBEDDED_OBJECT:
                case END_ARRAY:
                    break;
                case VALUE_NULL:
                    map.put(field, null);
                    break;
                default:
                    map.put(field, parseField(valueType, field, parser));
                    break;
            }
        } while (!parser.isClosed());
        return map;
    }

    private static Object parseField(MetaType<?> type, String field, JsonParser parser) throws IOException {
        MetaTransformer<?> transformer = type.inputTransformer();
        if (type.externalKind() == LAZY) {
            return transformer.fromLazy(() -> wrapExceptionCall(() -> parseField(type.parameters().get(0), field, parser), JsonException::new));
        }
        switch (parser.nextToken()) {
            case VALUE_NULL:
                return null;
            case START_OBJECT:
                if (type.externalKind() == ENTITY) {
                    return parseEntity(type, parser);
                }
                if (type.externalKind() != MAP && type.externalKind() != LAZY_MAP) {
                    throw new JsonException(format(JSON_OBJECT_FIELD_EXCEPTION, field, type));
                }
                return transformer.fromMap(parseMap(type, parser));
            case START_ARRAY:
                if (type.externalKind() != ARRAY && type.externalKind() != LAZY_ARRAY) {
                    throw new JsonException(format(JSON_ARRAY_FIELD_EXCEPTION, field, type));
                }
                return transformer.fromArray(parseArray(type, parser));
            case VALUE_STRING:
                return transformer.fromString(parser.getText());
            case VALUE_NUMBER_INT:
                return transformer.fromLong(parser.getLongValue());
            case VALUE_NUMBER_FLOAT:
                return transformer.fromFloat(parser.getFloatValue());
            case VALUE_TRUE:
            case VALUE_FALSE:
                return transformer.fromBoolean(parser.getBooleanValue());
        }
        throw new ImpossibleSituationException();
    }

    private static List<?> parseArray(MetaType<?> type, JsonParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        MetaType<?> valueType = type.parameters().get(1);
        if (valueType.externalKind() == LAZY) {
            valueType = valueType.parameters().get(0);
        }
        List<Object> array = linkedList();
        do {
            if (currentToken == END_ARRAY) {
                return array;
            }
            switch (currentToken = parser.nextToken()) {
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case VALUE_EMBEDDED_OBJECT:
                case END_ARRAY:
                    break;
                case VALUE_NULL:
                    array.add(null);
                    break;
                default:
                    array.add(parseValue(valueType, parser));
                    break;
            }
        } while (!parser.isClosed());
        return array;
    }

    private static Object parseValue(MetaType<?> type, JsonParser parser) throws IOException {
        MetaTransformer<?> transformer = type.inputTransformer();
        if (type.externalKind() == LAZY) {
            return transformer.fromLazy(() -> wrapExceptionCall(() -> parseValue(type.parameters().get(0), parser), JsonException::new));
        }
        switch (parser.nextToken()) {
            case VALUE_NULL:
                return null;
            case START_OBJECT:
                if (type.externalKind() == ENTITY) {
                    return parseEntity(type, parser);
                }
                if (type.externalKind() != MAP && type.externalKind() != LAZY_MAP) {
                    throw new JsonException(format(JSON_OBJECT_IN_ARRAY_EXCEPTION, type));
                }
                return transformer.fromMap(parseMap(type, parser));
            case START_ARRAY:
                if (type.externalKind() != ARRAY && type.externalKind() != LAZY_ARRAY) {
                    throw new JsonException(format(JSON_ARRAY_IN_ARRAY_EXCEPTION, type));
                }
                return transformer.fromArray(parseArray(type, parser));
            case VALUE_STRING:
                return transformer.fromString(parser.getText());
            case VALUE_NUMBER_INT:
                return transformer.fromLong(parser.getLongValue());
            case VALUE_NUMBER_FLOAT:
                return transformer.fromFloat(parser.getFloatValue());
            case VALUE_TRUE:
            case VALUE_FALSE:
                return transformer.fromBoolean(parser.getBooleanValue());
        }
        throw new ImpossibleSituationException();
    }
}
