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
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Entity.*;
import static io.art.entity.mapping.PrimitiveMapping.*;
import static io.art.json.module.JsonModule.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;


@UtilityClass
public class JsonEntityReader {
    public static Value readJson(byte[] jsonBytes) {
        return readJson(jsonModule().configuration().getObjectMapper().getFactory(), new String(jsonBytes, contextConfiguration().getCharset()));
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
        return readJson(jsonModule().configuration().getObjectMapper().getFactory(), json);
    }

    public static Value readJson(JsonFactory jsonFactory, String json) {
        if (isEmpty(json)) return null;
        try (JsonParser parser = jsonFactory.createParser(json)) {
            JsonToken nextToken = parser.nextToken();
            if (isNull(nextToken)) return null;
            switch (nextToken) {
                case START_OBJECT:
                    return parseEntity(parser);
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


    private static Entity parseEntity(JsonParser parser) throws IOException {
        EntityBuilder entityBuilder = entityBuilder();
        JsonToken currentToken = parser.nextToken();
        do {
            if (currentToken == END_OBJECT) {
                return entityBuilder.build();
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
                    entityBuilder.put(field, parseEntity(parser));
                    break;
                case START_ARRAY:
                    entityBuilder.put(field, parseArray(parser));
                    break;
                case VALUE_STRING:
                    entityBuilder.put(field, parser.getText(), fromString);
                    break;
                case VALUE_NUMBER_INT:
                    entityBuilder.put(field, parser.getLongValue(), fromLong);
                    break;
                case VALUE_NUMBER_FLOAT:
                    entityBuilder.put(field, parser.getFloatValue(), fromFloat);
                    break;
                case VALUE_TRUE:
                    entityBuilder.put(field, true, fromBool);
                    break;
                case VALUE_FALSE:
                    entityBuilder.put(field, false, fromBool);
                    break;
            }
        } while (!parser.isClosed());
        return entityBuilder.build();
    }

    private static ArrayValue parseArray(JsonParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        switch (currentToken) {
            case END_ARRAY:
                return emptyArray();
            case NOT_AVAILABLE:
            case END_OBJECT:
            case FIELD_NAME:
            case VALUE_EMBEDDED_OBJECT:
            case VALUE_NULL:
                return null;
            case START_OBJECT:
                return entityArray(parseEntityArray(parser));
            case START_ARRAY:
                return innerArray(parseInnerArray(parser));
            case VALUE_STRING:
                return stringArray(parseStringArray(parser));
            case VALUE_NUMBER_INT:
                return longArray(parseLongArray(parser));
            case VALUE_NUMBER_FLOAT:
                return floatArray(parseFloatArray(parser));
            case VALUE_TRUE:
            case VALUE_FALSE:
                return boolArray(parseBooleanArray(parser));
        }
        return null;
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

    private static List<Entity> parseEntityArray(JsonParser parser) throws IOException {
        List<Entity> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != START_OBJECT) return array;
            array.add(parseEntity(parser));
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }

    private static List<ArrayValue> parseInnerArray(JsonParser parser) throws IOException {
        List<ArrayValue> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != START_ARRAY) return array;
            array.add(parseArray(parser));
            currentToken = parser.nextToken();
        } while (!parser.isClosed() && currentToken != END_ARRAY);
        return array;
    }
}
