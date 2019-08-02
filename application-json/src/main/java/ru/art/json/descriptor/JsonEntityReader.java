/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.json.descriptor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.art.entity.CollectionValue;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.json.exception.JsonMappingException;
import static com.fasterxml.jackson.core.JsonToken.*;
import static java.util.Objects.isNull;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.constants.SizesConstants.INT_MAX_CHARACTER_SIZE;
import static ru.art.core.constants.SizesConstants.LONG_MAX_CHARACTER_SIZE;
import static ru.art.core.extension.FileExtensions.readFile;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.entity.CollectionValuesFactory.*;
import static ru.art.entity.Entity.EntityBuilder;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.PrimitivesFactory.*;
import static ru.art.json.constants.JsonMappingExceptionMessages.JSON_FACTORY_IS_NULL;
import static ru.art.json.module.JsonModule.jsonModule;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonEntityReader {
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
                    if (nextIsInt(parser)) {
                        return intPrimitive(parser.getIntValue());
                    }
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
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }

    public static Value readJson(String json) {
        return readJson(jsonModule().getObjectMapper().getFactory(), json);
    }

    public static Value readJson(Path path) {
        return readJson(readFile(path));
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
                    entityBuilder.entityField(currentName, parseJsonEntity(parser));
                    break;
                case START_ARRAY:
                    parseArray(entityBuilder, parser);
                    break;
                case VALUE_STRING:
                    entityBuilder.stringField(currentName, parser.getText());
                    break;
                case VALUE_NUMBER_INT:
                    parseNumber(entityBuilder, parser, currentName);
                    break;
                case VALUE_NUMBER_FLOAT:
                    entityBuilder.floatField(currentName, parser.getFloatValue());
                    break;
                case VALUE_TRUE:
                    entityBuilder.boolField(currentName, true);
                    break;
                case VALUE_FALSE:
                    entityBuilder.boolField(currentName, false);
                    break;
                case VALUE_NULL:
                    break;
            }
            currentToken = parser.nextToken();
        } while (!parser.isClosed());
        return entityBuilder.build();
    }

    private static void parseNumber(EntityBuilder entityBuilder, JsonParser parser, String currentName) throws IOException {
        if (nextIsInt(parser)) {
            entityBuilder.intField(currentName, parser.getIntValue());
            return;
        }
        if (nextIsLong(parser)) {
            entityBuilder.longField(currentName, parser.getLongValue());
        }
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
            case START_OBJECT:
                entityBuilder.entityCollectionField(currentName, parseEntityArray(parser));
                return;
            case START_ARRAY:
                parseArray(entityBuilder, parser);
                return;
            case VALUE_STRING:
                entityBuilder.stringCollectionField(currentName, parseStringArray(parser));
                return;
            case VALUE_NUMBER_INT:
                if (nextIsInt(parser)) {
                    entityBuilder.intCollectionField(currentName, parseIntArray(parser));
                    return;
                }
                if (nextIsLong(parser)) {
                    entityBuilder.longCollectionField(currentName, parseLongArray(parser));
                    return;
                }
                return;
            case VALUE_NUMBER_FLOAT:
                entityBuilder.floatCollectionField(currentName, parseFloatArray(parser));
                return;
            case VALUE_TRUE:
                entityBuilder.boolCollectionField(currentName, parseBooleanArray(parser));
                return;
            case VALUE_FALSE:
                entityBuilder.boolCollectionField(currentName, parseBooleanArray(parser));
        }
    }


    private static CollectionValue<?> parseArray(JsonParser parser) throws IOException {
        JsonToken currentToken = parser.nextToken();
        switch (currentToken) {
            case NOT_AVAILABLE:
            case END_OBJECT:
            case END_ARRAY:
            case FIELD_NAME:
            case VALUE_EMBEDDED_OBJECT:
            case VALUE_NULL:
                return emptyCollection();
            case START_OBJECT:
                return entityCollection(parseEntityArray(parser));
            case START_ARRAY:
                return parseArray(parser);
            case VALUE_STRING:
                return stringCollection(parseStringArray(parser));
            case VALUE_NUMBER_INT:
                if (nextIsInt(parser)) {
                    return intCollection(parseIntArray(parser));
                }
                if (nextIsLong(parser)) {
                    return longCollection(parseLongArray(parser));
                }
            case VALUE_NUMBER_FLOAT:
                return doubleCollection(parseDoubleArray(parser));
            case VALUE_TRUE:
            case VALUE_FALSE:
                return boolCollection(parseBooleanArray(parser));
        }
        return emptyCollection();
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

    private static Collection<Integer> parseIntArray(JsonParser parser) throws IOException {
        List<Integer> array = dynamicArrayOf();
        JsonToken currentToken = parser.currentToken();
        do {
            if (currentToken != VALUE_NUMBER_INT) return array;
            array.add(parser.getIntValue());
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


    private static boolean nextIsInt(JsonParser parser) throws IOException {
        return parser.getTextLength() <= INT_MAX_CHARACTER_SIZE;
    }

    private static boolean nextIsLong(JsonParser parser) throws IOException {
        return parser.getTextLength() <= LONG_MAX_CHARACTER_SIZE;
    }
}
