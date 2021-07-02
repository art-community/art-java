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
import io.art.meta.constants.MetaConstants.*;
import io.art.meta.model.*;
import io.art.meta.schema.MetaCreatorTemplate.*;
import io.art.meta.transformer.*;
import lombok.*;
import static com.fasterxml.jackson.core.JsonToken.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.io.*;
import java.util.*;


@AllArgsConstructor
public class JsonModelReader {
    private final JsonFactory jsonFactory;

    // TODO: Add type validation

    public <T> T read(MetaType<T> type, InputStream json) {
        if (isEmpty(json)) return null;
        MetaTransformer<T> transformer = type.inputTransformer();
        try (JsonParser parser = jsonFactory.createParser(json)) {
            JsonToken nextToken = parser.nextToken();
            if (isNull(nextToken)) return null;
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
                    return transformer.fromCharacter(parser.getText().charAt(0));
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


    private static Object parseEntity(MetaType<?> type, JsonParser parser) {
        if (type.externalKind() == LAZY) {
            return type.outputTransformer().fromLazy(() -> parseEntity(type.parameters().get(0), parser));
        }
        try {
            JsonToken currentToken = parser.nextToken();
            MetaClass<?> definition = type.definition();
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
                MetaTransformer<?> outputTransformer = propertyType.outputTransformer();
                switch (currentToken = parser.nextToken()) {
                    case NOT_AVAILABLE:
                    case END_OBJECT:
                    case FIELD_NAME:
                    case VALUE_EMBEDDED_OBJECT:
                    case END_ARRAY:
                        break;
                    case START_OBJECT:
                        if (propertyType.externalKind() == MetaTypeExternalKind.ENTITY) {
                            creator.put(property, parseEntity(propertyType, parser));
                            break;
                        }
                        creator.put(property, outputTransformer.fromMap(parseMap(propertyType, parser)));
                        break;
                    case START_ARRAY:
                        creator.put(property, outputTransformer.fromArray(parseArray(propertyType, parser)));
                        break;
                    case VALUE_STRING:
                        creator.put(property, outputTransformer.fromString(parser.getText()));
                        break;
                    case VALUE_NUMBER_INT:
                        creator.put(property, outputTransformer.fromLong(parser.getLongValue()));
                        break;
                    case VALUE_NUMBER_FLOAT:
                        creator.put(property, outputTransformer.fromFloat(parser.getFloatValue()));
                        break;
                    case VALUE_TRUE:
                    case VALUE_FALSE:
                        creator.put(property, outputTransformer.fromBoolean(parser.getBooleanValue()));
                        break;
                }
            } while (!parser.isClosed());
            return creator.create();
        } catch (Throwable throwable) {
            throw new JsonException(throwable);
        }
    }


    private static Map<?, ?> parseMap(MetaType<?> type, JsonParser parser) {
        if (type.externalKind() == LAZY) {
            return cast(type.outputTransformer().fromLazy(() -> parseMap(type.parameters().get(0), parser)));
        }
        try {
            JsonToken currentToken = parser.nextToken();
            MetaType<?> valueType = type.parameters().get(1);
            MetaTransformer<?> valueTransformer = valueType.outputTransformer();
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
                    case START_OBJECT:
                        if (valueType.externalKind() == MetaTypeExternalKind.ENTITY) {
                            map.put(field, parseEntity(valueType, parser));
                            break;
                        }
                        map.put(field, valueTransformer.fromMap(parseMap(valueType, parser)));
                        break;
                    case START_ARRAY:
                        map.put(field, valueTransformer.fromArray(parseArray(valueType, parser)));
                        break;
                    case VALUE_STRING:
                        map.put(field, valueTransformer.fromString(parser.getText()));
                        break;
                    case VALUE_NUMBER_INT:
                        map.put(field, valueTransformer.fromLong(parser.getLongValue()));
                        break;
                    case VALUE_NUMBER_FLOAT:
                        map.put(field, valueTransformer.fromFloat(parser.getFloatValue()));
                        break;
                    case VALUE_TRUE:
                    case VALUE_FALSE:
                        map.put(field, valueTransformer.fromBoolean(parser.getBooleanValue()));
                        break;
                }
            } while (!parser.isClosed());
            return map;
        } catch (Throwable throwable) {
            throw new JsonException(throwable);
        }
    }

    private static List<?> parseArray(MetaType<?> type, JsonParser parser) {
        if (type.externalKind() == LAZY) {
            return cast(type.outputTransformer().fromLazy(() -> parseArray(type.parameters().get(0), parser)));
        }
        try {
            MetaType<?> elementType = orElse(type.arrayComponentType(), () -> type.parameters().get(0));
            JsonToken currentToken = parser.nextToken();
            switch (currentToken) {
                case END_ARRAY:
                    return emptyList();
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case VALUE_EMBEDDED_OBJECT:
                case VALUE_NULL:
                    return emptyList();
                case START_OBJECT:
                    return parseEntityArray(elementType, parser);
                case START_ARRAY:
                    return parseInnerArray(elementType, parser);
                case VALUE_STRING:
                    return parseStringArray(parser);
                case VALUE_NUMBER_INT:
                    return parseLongArray(parser);
                case VALUE_NUMBER_FLOAT:
                    return parseFloatArray(parser);
                case VALUE_TRUE:
                case VALUE_FALSE:
                    return parseBooleanArray(parser);
            }
            return emptyList();
        } catch (Throwable throwable) {
            throw new JsonException(throwable);
        }
    }


    private static List<String> parseStringArray(JsonParser parser) throws IOException {
        List<String> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_STRING) return array;
            array.add(parser.getText());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static List<Boolean> parseBooleanArray(JsonParser parser) throws IOException {
        List<Boolean> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_FALSE && currentToken != VALUE_TRUE) return array;
            array.add(parser.getBooleanValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static List<Long> parseLongArray(JsonParser parser) throws IOException {
        List<Long> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_NUMBER_INT) return array;
            array.add(parser.getLongValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static List<Float> parseFloatArray(JsonParser parser) throws IOException {
        List<Float> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_NUMBER_FLOAT) return array;
            array.add(parser.getFloatValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static List<Object> parseEntityArray(MetaType<?> type, JsonParser parser) throws Throwable {
        List<Object> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != START_OBJECT) return array;
            array.add(parseEntity(type, parser));
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static List<Object> parseInnerArray(MetaType<?> type, JsonParser parser) throws Throwable {
        List<Object> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != START_ARRAY) return array;
            array.add(parseArray(type, parser));
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }
}
