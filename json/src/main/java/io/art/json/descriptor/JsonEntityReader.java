/*
 * ART
 *
 * Copyright 2020 ART
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
import io.art.core.extensions.*;
import io.art.entity.builder.*;
import io.art.entity.immutable.*;
import io.art.json.exception.*;
import io.netty.buffer.*;
import lombok.experimental.*;
import static com.fasterxml.jackson.core.JsonToken.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.mapping.PrimitiveMapping.*;
import static io.art.json.constants.JsonMappingExceptionMessages.*;
import static io.art.json.module.JsonModule.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;


@UtilityClass
public class JsonEntityReader {
    public static Value readJson(byte[] jsonBytes) {
        return readJson(jsonModule().getConfiguration().getObjectMapper().getFactory(), new String(jsonBytes, contextConfiguration().getCharset()));
    }

    public static Value readJson(ByteBuffer byteBuf) {
        return readJson(NioBufferExtensions.toByteArray(byteBuf));
    }

    public static Value readJson(ByteBuf byteBuf) {
        return readJson(NettyBufferExtensions.toByteArray(byteBuf));
    }

    public static Value readJson(InputStream inputStream) {
        return readJson(InputStreamExtensions.toByteArray(inputStream));
    }

    public static Value readJson(Path path) {
        return readJson(readFile(path));
    }

    public static Value readJson(String json) {
        return readJson(jsonModule().getConfiguration().getObjectMapper().getFactory(), json);
    }

    public static Value readJson(JsonFactory jsonFactory, String json) {
        if (isNull(jsonFactory)) throw new JsonMappingException(JSON_FACTORY_IS_NULL);
        if (isEmpty(json)) return null;
        try {
            JsonParser parser = jsonFactory.createParser(json);
            JsonToken nextToken = parser.nextToken();
            if (isNull(nextToken)) return null;
            switch (nextToken) {
                case START_OBJECT:
                    return parseJsonEntity(parser);
                case START_ARRAY:
                    return parseArray(parser);
                case VALUE_STRING:
                    return stringPrimitive(parser.getText());
                case VALUE_NUMBER_INT:
                    return longPrimitive(parser.getLongValue());
                case VALUE_NUMBER_FLOAT:
                    return floatPrimitive(parser.getFloatValue());
                case VALUE_TRUE:
                    return boolPrimitive(true);
                case VALUE_FALSE:
                    return boolPrimitive(false);
                case FIELD_NAME:
                case VALUE_NULL:
                case NOT_AVAILABLE:
                case END_OBJECT:
                case VALUE_EMBEDDED_OBJECT:
                case END_ARRAY:
                    return null;
            }
            return null;
        } catch (IOException ioException) {
            throw new JsonMappingException(ioException);
        }
    }


    private static Entity parseJsonEntity(JsonParser parser) throws IOException {
        EntityBuilder entityBuilder = entityBuilder();
        JsonToken currentToken = parser.nextToken();
        do {
            String currentName = parser.getCurrentName();
            if (isNull(currentToken)) {
                currentToken = parser.nextToken();
                continue;
            }
            currentName = emptyIfNull(currentName);
            switch (currentToken) {
                case NOT_AVAILABLE:
                case END_ARRAY:
                case END_OBJECT:
                    return entityBuilder.build();
                case START_OBJECT:
                    entityBuilder.put(currentName, parseJsonEntity(parser));
                    break;
                case START_ARRAY:
                    parseArray(entityBuilder, parser);
                    break;
                case VALUE_STRING:
                    entityBuilder.put(currentName, parser.getText(), fromString);
                    break;
                case VALUE_NUMBER_INT:
                    entityBuilder.put(currentName, parser.getLongValue(), fromLong);
                    break;
                case VALUE_NUMBER_FLOAT:
                    entityBuilder.put(currentName, parser.getFloatValue(), fromFloat);
                    break;
                case VALUE_TRUE:
                    entityBuilder.put(currentName, true, fromBool);
                    break;
                case VALUE_FALSE:
                    entityBuilder.put(currentName, false, fromBool);
                    break;
                case VALUE_NULL:
                    break;
            }
            currentToken = parser.nextToken();
        } while (!parser.isClosed());
        return entityBuilder.build();
    }

    private static void parseArray(EntityBuilder entityBuilder, JsonParser parser) throws IOException {
        String currentName = emptyIfNull(parser.getCurrentName());
        JsonToken currentToken = parser.nextToken();
        switch (currentToken) {
            case NOT_AVAILABLE:
            case END_OBJECT:
            case END_ARRAY:
            case FIELD_NAME:
            case VALUE_EMBEDDED_OBJECT:
            case VALUE_NULL:
                return;
            case START_ARRAY:
                parseArray(entityBuilder, parser);
                return;
            case START_OBJECT:
            case VALUE_STRING:
            case VALUE_NUMBER_INT:
            case VALUE_NUMBER_FLOAT:
            case VALUE_TRUE:
            case VALUE_FALSE:
                entityBuilder.put(currentName, parseArray(parser));
        }
    }


    private static ArrayValue parseArray(JsonParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        switch (currentToken) {
            case NOT_AVAILABLE:
            case END_OBJECT:
            case END_ARRAY:
            case FIELD_NAME:
            case VALUE_EMBEDDED_OBJECT:
            case VALUE_NULL:
                return emptyArray();
            case START_OBJECT:
                return entityArray(parseEntityArray(parser));
            case START_ARRAY:
                return parseArray(parser);
            case VALUE_STRING:
                return stringArray(parseStringArray(parser));
            case VALUE_NUMBER_INT:
                return longArray(parseLongArray(parser));
            case VALUE_NUMBER_FLOAT:
                return doubleArray(parseDoubleArray(parser));
            case VALUE_TRUE:
            case VALUE_FALSE:
                return boolArray(parseBooleanArray(parser));
        }
        return emptyArray();
    }


    private static Collection<String> parseStringArray(JsonParser parser) throws IOException {
        List<String> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_STRING) return array;
            array.add(parser.getText());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static Collection<Double> parseDoubleArray(JsonParser parser) throws IOException {
        List<Double> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_NUMBER_FLOAT) return array;
            array.add(parser.getDoubleValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static Collection<Boolean> parseBooleanArray(JsonParser parser) throws IOException {
        List<Boolean> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_FALSE && currentToken != VALUE_TRUE) return array;
            array.add(parser.getBooleanValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static Collection<Long> parseLongArray(JsonParser parser) throws IOException {
        List<Long> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_NUMBER_INT) return array;
            array.add(parser.getLongValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static Collection<Float> parseFloatArray(JsonParser parser) throws IOException {
        List<Float> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_NUMBER_FLOAT) return array;
            array.add(parser.getFloatValue());
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static Collection<Entity> parseEntityArray(JsonParser parser) throws IOException {
        List<Entity> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != START_OBJECT) return array;
            array.add(parseJsonEntity(parser));
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }
}
