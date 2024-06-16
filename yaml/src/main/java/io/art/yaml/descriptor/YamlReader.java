/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.yaml.descriptor;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.dataformat.yaml.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.core.extensions.*;
import io.art.meta.descriptor.Reader;
import io.art.meta.model.*;
import io.art.meta.schema.MetaCreatorTemplate.*;
import io.art.meta.transformer.*;
import io.art.yaml.exception.*;
import lombok.*;
import static com.fasterxml.jackson.core.JsonToken.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static io.art.yaml.constants.YamlModuleConstants.Errors.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.io.*;
import java.util.*;


@Public
@AllArgsConstructor
public class YamlReader implements Reader {
    private final YAMLFactory yamlFactory;

    @Override
    public <T> T read(MetaType<T> type, InputStream yaml) {
        if (isNull(yaml)) return null;
        MetaTransformer<T> transformer = type.inputTransformer();
        if (type.externalKind() == LAZY) {
            Object lazy = read(type.parameters().get(0), yaml);
            return transformer.fromLazy(() -> lazy);
        }
        switch (type.externalKind()) {
            case STRING:
            case LONG:
            case DOUBLE:
            case FLOAT:
            case INTEGER:
            case BOOLEAN:
            case CHARACTER:
            case SHORT:
            case BYTE:
                return transformer.fromString(InputStreamExtensions.toString(yaml));
            case BINARY:
                return transformer.fromByteArray(InputStreamExtensions.toByteArray(yaml));
        }
        try (YAMLParser parser = yamlFactory.createParser(yaml)) {
            JsonToken nextToken = parser.nextToken();
            if (isNull(nextToken) || nextToken == VALUE_NULL) return null;
            return switch (type.externalKind()) {
                case MAP, LAZY_MAP -> transformer.fromMap(parseMap(type, parser));
                case ARRAY, LAZY_ARRAY -> transformer.fromArray(parseArray(type, parser));
                case ENTITY -> cast(parseEntity(type, parser));
                default -> throw new ImpossibleSituationException();
            };
        } catch (Throwable throwable) {
            throw new YamlException(throwable);
        }
    }


    private static Object parseEntity(MetaType<?> type, YAMLParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        MetaClass<?> declaration = type.declaration();
        MetaCreatorInstance creator = declaration.creator().validate(YamlException::new).instantiate();
        ImmutableMap<String, MetaProperty<?>> properties = creator.propertyMap();
        do {
            if (currentToken == END_OBJECT) {
                return creator.create();
            }
            if (currentToken != FIELD_NAME) {
                currentToken = parser.nextToken();
                continue;
            }
            String field = parser.currentName();
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
            switch (currentToken = parser.nextToken()) {
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case VALUE_NULL:
                case END_ARRAY:
                    break;
                default:
                    creator.putValue(property, parseField(propertyType, field, parser));
                    break;
            }
        } while (!parser.isClosed());
        return creator.create();
    }

    private static Map<?, ?> parseMap(MetaType<?> type, YAMLParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        MetaType<?> keyType = type.parameters().get(0);
        MetaType<?> valueType = type.parameters().get(1);
        Map<Object, Object> map = map();
        do {
            if (currentToken == END_OBJECT) {
                return map;
            }
            if (currentToken != FIELD_NAME) {
                currentToken = parser.nextToken();
                continue;
            }
            String field = parser.currentName();
            if (isEmpty(field)) {
                currentToken = parser.nextToken();
                continue;
            }
            MetaTransformer<?> keyTransformer = keyType.inputTransformer();
            switch (currentToken = parser.nextToken()) {
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case END_ARRAY:
                    break;
                case VALUE_NULL:
                    map.put(keyTransformer.fromString(field), null);
                    break;
                default:
                    map.put(keyTransformer.fromString(field), parseField(valueType, field, parser));
                    break;
            }
        } while (!parser.isClosed());
        return map;
    }

    private static List<?> parseArray(MetaType<?> type, YAMLParser parser) throws IOException {
        JsonToken currentToken = parser.currentToken();
        MetaType<?> elementsType = orElse(type.arrayComponentType(), () -> type.parameters().get(0));
        List<Object> array = linkedList();
        do {
            if (currentToken == END_ARRAY) {
                return array;
            }
            switch (currentToken = parser.nextToken()) {
                case NOT_AVAILABLE:
                case END_OBJECT:
                case FIELD_NAME:
                case END_ARRAY:
                    break;
                case VALUE_NULL:
                    array.add(null);
                    break;
                default:
                    array.add(parseValue(elementsType, parser));
                    break;
            }
        } while (!parser.isClosed());
        return array;
    }

    private static Object parseField(MetaType<?> type, String field, YAMLParser parser) throws IOException {
        MetaTransformer<?> transformer = type.inputTransformer();
        if (type.externalKind() == LAZY) {
            Object value = parseField(type.parameters().get(0), field, parser);
            return transformer.fromLazy(() -> value);
        }
        switch (parser.currentToken()) {
            case VALUE_NULL:
                return null;
            case START_OBJECT:
                if (type.externalKind() == ENTITY) {
                    return parseEntity(type, parser);
                }
                if (type.externalKind() != MAP && type.externalKind() != LAZY_MAP) {
                    throw new YamlException(format(YAML_OBJECT_FIELD_EXCEPTION, field, type));
                }
                return transformer.fromMap(parseMap(type, parser));
            case START_ARRAY:
                if (type.externalKind() != ARRAY && type.externalKind() != LAZY_ARRAY) {
                    throw new YamlException(format(YAML_ARRAY_FIELD_EXCEPTION, field, type));
                }
                return transformer.fromArray(parseArray(type, parser));
            case VALUE_EMBEDDED_OBJECT:
                if (type.externalKind() != BINARY) {
                    throw new YamlException(format(YAML_OBJECT_FIELD_EXCEPTION, field, type));
                }
                return transformer.fromByteArray(parser.getBinaryValue());
            case VALUE_STRING:
                if (type.externalKind() == BINARY) {
                    return transformer.fromByteArray(parser.getBinaryValue());
                }
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

    private static Object parseValue(MetaType<?> type, YAMLParser parser) throws IOException {
        MetaTransformer<?> transformer = type.inputTransformer();
        if (type.externalKind() == LAZY) {
            Object value = parseValue(type.parameters().get(0), parser);
            return transformer.fromLazy(() -> value);
        }
        switch (parser.currentToken()) {
            case VALUE_NULL:
                return null;
            case START_OBJECT:
                if (type.externalKind() == ENTITY) {
                    return parseEntity(type, parser);
                }
                if (type.externalKind() != MAP && type.externalKind() != LAZY_MAP) {
                    throw new YamlException(format(YAML_OBJECT_IN_ARRAY_EXCEPTION, type));
                }
                return transformer.fromMap(parseMap(type, parser));
            case START_ARRAY:
                if (type.externalKind() != ARRAY && type.externalKind() != LAZY_ARRAY) {
                    throw new YamlException(format(YAML_ARRAY_IN_ARRAY_EXCEPTION, type));
                }
                return transformer.fromArray(parseArray(type, parser));
            case VALUE_EMBEDDED_OBJECT:
                if (type.externalKind() != BINARY) {
                    throw new YamlException(format(YAML_ARRAY_IN_ARRAY_EXCEPTION, type));
                }
                return transformer.fromByteArray(parser.getBinaryValue());
            case VALUE_STRING:
                if (type.externalKind() == BINARY) {
                    return transformer.fromByteArray(parser.getBinaryValue());
                }
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
