/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.json.descriptor;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.art.entity.*;
import ru.art.entity.constants.ValueType.CollectionElementsType;
import ru.art.json.exception.JsonMappingException;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.FileExtensions.writeFileQuietly;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.entity.Value.*;
import static ru.art.entity.constants.ValueType.asCollectionElementsType;
import static ru.art.json.constants.JsonLoggingMessages.JSON_GENERATOR_CLOSING_ERROR;
import static ru.art.json.constants.JsonMappingExceptionMessages.JSON_FACTORY_IS_NULL;
import static ru.art.json.module.JsonModule.jsonModule;
import static ru.art.logging.LoggingModule.loggingModule;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonEntityWriter {
    public static String writeJson(Value value) {
        return writeJson(jsonModule().getObjectMapper().getFactory(), value);
    }

    public static void writeJson(Value value, Path path) {
        writeFileQuietly(path, writeJson(value));
    }

    public static String writeJson(JsonFactory jsonFactory, Value value) {
        if (isNull(jsonFactory)) throw new JsonMappingException(JSON_FACTORY_IS_NULL);
        if (isNull(value)) {
            return BRACES;
        }
        if (isEmpty(value)) {
            if (isCollection(value)) {
                return SQUARE_BRACES;
            }
            return BRACES;
        }
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = null;
        try {
            generator = jsonFactory.createGenerator(stringWriter);
            switch (value.getType()) {
                case ENTITY:
                    writeJsonEntity(generator, asEntity(value));
                    break;
                case COLLECTION:
                    writeArray(generator, asCollection(value));
                    break;
                case STRING:
                    return DOUBLE_QUOTES + emptyIfNull(asPrimitive(value).getString()) + DOUBLE_QUOTES;
                case LONG:
                    return emptyIfNull(asPrimitive(value).getLong());
                case DOUBLE:
                    return emptyIfNull(asPrimitive(value).getDouble());
                case FLOAT:
                    return emptyIfNull(asPrimitive(value).getFloat());
                case INT:
                    return emptyIfNull(asPrimitive(value).getInt());
                case BOOL:
                    return emptyIfNull(asPrimitive(value).getBool());
                case BYTE:
                    return emptyIfNull(asPrimitive(value).getByte());
                case MAP:
                    writeJsonMap(generator, asMap(value));
            }
        } catch (IOException e) {
            throw new JsonMappingException(e);
        } finally {
            if (nonNull(generator)) {
                try {
                    generator.flush();
                    generator.close();
                } catch (IOException e) {
                    loggingModule().getLogger(JsonEntityWriter.class).error(JSON_GENERATOR_CLOSING_ERROR, e);
                }
            }
        }
        return stringWriter.toString();
    }


    private static void writeJsonMap(JsonGenerator generator, MapValue map) {
        if (isNull(map)) return;
        map.getElements()
                .entrySet()
                .stream()
                .filter(entry -> isPrimitive(entry.getKey()))
                .forEach(entry -> writeJsonMapEntry(generator, entry));
    }

    private static void writeJsonMap(JsonGenerator jsonGenerator, String name, MapValue mapValue) {
        if (isNull(mapValue)) return;
        try {
            jsonGenerator.writeObjectFieldStart(name);
            writeJsonMap(jsonGenerator, mapValue);
            jsonGenerator.writeEndObject();
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }


    private static void writeJsonMapEntry(JsonGenerator generator, Map.Entry<? extends Value, ? extends Value> entry) {
        try {
            writeField(generator, emptyIfNull(entry.getKey()), entry.getValue());
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }

    private static void writeJsonEntity(JsonGenerator generator, Entity entity) throws IOException {
        if (isNull(entity)) return;
        generator.writeStartObject();
        Map<String, ? extends Value> fields = getEntityFields(entity);
        for (String field : fields.keySet()) {
            writeField(generator, field, fields.get(field));
        }
        generator.writeEndObject();
    }

    private static void writeJsonEntity(JsonGenerator jsonGenerator, String name, Entity entity) throws IOException {
        if (isNull(entity)) return;
        jsonGenerator.writeObjectFieldStart(name);
        Map<String, ? extends Value> fields = getEntityFields(entity);
        for (String field : fields.keySet()) {
            writeField(jsonGenerator, field, fields.get(field));
        }
        jsonGenerator.writeEndObject();

    }


    private static void writeArray(JsonGenerator jsonGenerator, String fieldName, CollectionValue<?> array) throws IOException {
        if (isNull(array)) return;
        jsonGenerator.writeArrayFieldStart(fieldName);
        switch (array.getCollectionMode()) {
            case PRIMITIVE_ARRAY:
                writePrimitiveElements(jsonGenerator, array);
                break;
            case COLLECTION:
                writeCollectionElements(jsonGenerator, array);
                break;
        }
        jsonGenerator.writeEndArray();
    }

    private static void writeArray(JsonGenerator jsonGenerator, CollectionValue<?> array) throws IOException {
        if (isNull(array)) return;
        jsonGenerator.writeStartArray();
        switch (array.getCollectionMode()) {
            case PRIMITIVE_ARRAY:
                writePrimitiveElements(jsonGenerator, array);
                break;
            case COLLECTION:
                writeCollectionElements(jsonGenerator, array);
                break;
        }
        jsonGenerator.writeEndArray();
    }


    private static void writeField(JsonGenerator jsonGenerator, String name, Value value) throws IOException {
        if (isNull(value)) return;
        switch (value.getType()) {
            case ENTITY:
                writeJsonEntity(jsonGenerator, name, asEntity(value));
                return;
            case COLLECTION:
                writeArray(jsonGenerator, name, asCollection(value));
                return;
            case MAP:
                writeJsonMap(jsonGenerator, name, asMap(value));
                return;
            case STRING:
            case INT:
            case DOUBLE:
            case LONG:
            case BOOL:
            case BYTE:
            case FLOAT:
                writeField(jsonGenerator, name, asPrimitive(value));
        }
    }

    private static void writeField(JsonGenerator jsonGenerator, String name, Primitive value) throws IOException {
        if (isEmpty(value)) return;
        switch (value.getType()) {
            case STRING:
                jsonGenerator.writeStringField(name, value.getString());
                return;
            case INT:
                jsonGenerator.writeNumberField(name, value.getInt());
                return;
            case DOUBLE:
                jsonGenerator.writeNumberField(name, value.getDouble());
                return;
            case LONG:
                jsonGenerator.writeNumberField(name, value.getLong());
                return;
            case BOOL:
                jsonGenerator.writeBooleanField(name, value.getBool());
                return;
            case BYTE:
                jsonGenerator.writeNumberField(name, value.getByte());
                return;
            case FLOAT:
                jsonGenerator.writeNumberField(name, value.getFloat());
        }
    }


    private static void writeCollectionValue(JsonGenerator jsonGenerator, CollectionElementsType type, Object value) throws IOException {
        if (isNull(value)) return;
        switch (type) {
            case COLLECTION:
                writeArray(jsonGenerator, asCollection(cast(value)));
                return;
            case ENTITY:
                writeJsonEntity(jsonGenerator, asEntity(cast(value)));
                return;
            case BYTE:
            case STRING:
            case INT:
            case BOOL:
            case DOUBLE:
            case LONG:
            case FLOAT:
                writeCollectionElement(jsonGenerator, type, value);
                return;
            case VALUE:
                writeCollectionValue(jsonGenerator, asCollectionElementsType(((Value) value).getType()), value);
        }
    }

    private static void writeCollectionElement(JsonGenerator jsonGenerator, CollectionElementsType type, Object value) throws IOException {
        if (isNull(value)) return;
        switch (type) {
            case STRING:
                jsonGenerator.writeString((String) value);
                return;
            case INT:
                jsonGenerator.writeNumber((Integer) value);
                return;
            case BOOL:
                jsonGenerator.writeBoolean((Boolean) value);
                return;
            case DOUBLE:
                jsonGenerator.writeNumber((Double) value);
                return;
            case LONG:
                jsonGenerator.writeNumber((Long) value);
                return;
            case BYTE:
                jsonGenerator.writeNumber((Byte) value);
                return;
            case FLOAT:
                jsonGenerator.writeNumber((Float) value);
        }
    }

    private static Map<String, ? extends Value> getEntityFields(Entity entity) {
        return entity.getFields();
    }


    private static void writeCollectionElements(JsonGenerator jsonGenerator, CollectionValue<?> array) throws IOException {
        CollectionElementsType valueType = array.getElementsType();
        for (Object value : array.getElements()) {
            writeCollectionValue(jsonGenerator, valueType, value);
        }
    }

    private static void writePrimitiveElements(JsonGenerator jsonGenerator, CollectionValue<?> array) throws IOException {
        CollectionElementsType valueType = array.getElementsType();
        switch (valueType) {
            case INT:
                for (int value : array.getIntArray()) {
                    jsonGenerator.writeNumber(value);
                }
                return;
            case BOOL:
                for (boolean value : array.getBoolArray()) {
                    jsonGenerator.writeBoolean(value);
                }
                return;
            case DOUBLE:
                for (double value : array.getDoubleArray()) {
                    jsonGenerator.writeNumber(value);
                }
                return;
            case LONG:
                for (long value : array.getIntArray()) {
                    jsonGenerator.writeNumber(value);
                }
                return;
            case BYTE:
                for (byte value : array.getByteArray()) {
                    jsonGenerator.writeNumber(value);
                }
                return;
            case FLOAT:
                for (float value : array.getFloatArray()) {
                    jsonGenerator.writeNumber(value);
                }
        }
    }
}