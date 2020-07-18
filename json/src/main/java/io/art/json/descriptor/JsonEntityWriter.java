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
import io.art.core.checker.*;
import io.art.entity.immutable.*;
import io.art.json.exception.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.immutable.Value.*;
import static io.art.json.constants.JsonLoggingMessages.*;
import static io.art.json.module.JsonModule.*;
import static io.art.logging.LoggingModule.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class JsonEntityWriter {
    public static byte[] writeJsonToBytes(Value value) {
        String json = writeJson(value);
        if (EmptinessChecker.isEmpty(json)) {
            return EMPTY_BYTES;
        }
        return json.getBytes(contextConfiguration().getCharset());
    }

    public static void writeJson(Value value, OutputStream outputStream) {
        try {
            String json = writeJson(value);
            if (EmptinessChecker.isEmpty(json)) {
                return;
            }
            outputStream.write(json.getBytes());
        } catch (IOException ioException) {
            throw new JsonMappingException(ioException);
        }
    }

    public static void writeJson(Value value, Path path) {
        String json = writeJson(value);
        if (EmptinessChecker.isEmpty(json)) {
            return;
        }
        writeFileQuietly(path, json);
    }

    public static String writeJson(Value value) {
        return writeJson(jsonModule().configuration().getObjectMapper().getFactory(), value, false);
    }

    public static String writeJson(JsonFactory jsonFactory, Value value, boolean prettyOutput) {
        if (valueIsNull(value)) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        JsonGenerator generator = null;
        try {
            generator = jsonFactory.createGenerator(stringWriter);
            if (prettyOutput) {
                generator.useDefaultPrettyPrinter();
            }
            switch (value.getType()) {
                case ENTITY:
                    writeJsonEntity(generator, asEntity(value));
                    break;
                case ARRAY:
                    writeArray(generator, asArray(value));
                    break;
                case BINARY:
                    return Arrays.toString(asBinary(value).getContent());
                case STRING:
                    return asPrimitive(value).getString();
                case LONG:
                    return asPrimitive(value).getLong().toString();
                case DOUBLE:
                    return asPrimitive(value).getDouble().toString();
                case FLOAT:
                    return asPrimitive(value).getFloat().toString();
                case INT:
                    return asPrimitive(value).getInt().toString();
                case BOOL:
                    return asPrimitive(value).getBool().toString();
                case BYTE:
                    return asPrimitive(value).getByte().toString();
            }
        } catch (IOException ioException) {
            throw new JsonMappingException(ioException);
        } finally {
            if (nonNull(generator)) {
                try {
                    generator.flush();
                    generator.close();
                } catch (IOException ioException) {
                    loggingModule().configuration().getLogger(JsonEntityWriter.class).error(JSON_GENERATOR_CLOSING_ERROR, ioException);
                }
            }
        }
        return stringWriter.toString();
    }


    private static void writeJsonEntity(JsonGenerator generator, Entity entity) throws IOException {
        if (valueIsNull(entity)) return;
        generator.writeStartObject();
        writeJsonFields(generator, entity);
        generator.writeEndObject();
    }

    private static void writeJsonEntity(JsonGenerator jsonGenerator, String name, Entity entity) throws IOException {
        if (valueIsNull(entity)) return;
        jsonGenerator.writeObjectFieldStart(name);
        writeJsonFields(jsonGenerator, entity);
        jsonGenerator.writeEndObject();
    }

    private static void writeJsonFields(JsonGenerator generator, Entity entity) throws IOException {
        Set<Primitive> keys = entity.asMap().keySet();
        for (Primitive key : keys) {
            if (valueIsNull(key)) continue;
            Value value = entity.get(key);
            if (valueIsNull(value)) continue;
            writeField(generator, key.getString(), value);
        }
    }

    private static void writeArray(JsonGenerator jsonGenerator, String fieldName, ArrayValue array) throws IOException {
        if (valueIsNull(array)) return;
        jsonGenerator.writeArrayFieldStart(fieldName);
        for (int index = 0; index < array.size(); index++) {
            Value value = array.get(index);
            if (valueIsNull(value)) continue;
            writeArrayElement(jsonGenerator, value);
        }
        jsonGenerator.writeEndArray();
    }

    private static void writeArray(JsonGenerator jsonGenerator, ArrayValue array) throws IOException {
        if (valueIsNull(array)) return;
        jsonGenerator.writeStartArray();
        for (int index = 0; index < array.size(); index++) {
            Value value = array.get(index);
            if (valueIsNull(value)) continue;
            writeArrayElement(jsonGenerator, value);
        }
        jsonGenerator.writeEndArray();
    }


    private static void writeField(JsonGenerator jsonGenerator, String name, Value value) throws IOException {
        if (valueIsNull(value)) return;
        switch (value.getType()) {
            case ENTITY:
                writeJsonEntity(jsonGenerator, name, asEntity(value));
                return;
            case ARRAY:
                writeArray(jsonGenerator, name, asArray(value));
                return;
            case BINARY:
                jsonGenerator.writeBinaryField(name, asBinary(value).getContent());
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
        if (valueIsEmpty(value) || EmptinessChecker.isEmpty(value.getValue())) return;
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


    private static void writeArrayElement(JsonGenerator jsonGenerator, Value value) throws IOException {
        if (valueIsNull(value)) return;
        switch (value.getType()) {
            case ARRAY:
                writeArray(jsonGenerator, asArray(cast(value)));
                return;
            case ENTITY:
                writeJsonEntity(jsonGenerator, asEntity(cast(value)));
                return;
            case BINARY:
                jsonGenerator.writeBinary(asBinary(value).getContent());
                return;
            case STRING:
                jsonGenerator.writeString(asPrimitive(value).getString());
                return;
            case INT:
                jsonGenerator.writeNumber(asPrimitive(value).getInt());
                return;
            case BOOL:
                jsonGenerator.writeBoolean(asPrimitive(value).getBool());
                return;
            case DOUBLE:
                jsonGenerator.writeNumber(asPrimitive(value).getDouble());
                return;
            case LONG:
                jsonGenerator.writeNumber(asPrimitive(value).getLong());
                return;
            case BYTE:
                jsonGenerator.writeNumber(asPrimitive(value).getByte());
                return;
            case FLOAT:
                jsonGenerator.writeNumber(asPrimitive(value).getFloat());
        }
    }
}
