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
import io.art.core.stream.*;
import io.art.entity.immutable.*;
import io.art.json.exception.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.BufferConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.immutable.Value.*;
import static io.art.json.module.JsonModule.*;
import static java.nio.ByteBuffer.*;
import java.io.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class JsonEntityWriter {
    public static byte[] writeJsonToBytes(Value value) {
        ByteBuffer byteBuffer = allocateDirect(DEFAULT_BUFFER_SIZE);
        try {
            try (NioByteBufferOutputStream outputStream = new NioByteBufferOutputStream(byteBuffer)) {
                writeJson(value, outputStream);
            } catch (IOException ioException) {
                throw new JsonException(ioException);
            }
            return NioBufferExtensions.toByteArray(byteBuffer);
        } finally {
            byteBuffer.clear();
        }
    }

    public static void writeJson(Value value, Path path) {
        try (OutputStream outputStream = fileOutputStream(path)) {
            writeJson(value, outputStream);
        } catch (IOException ioException) {
            throw new JsonException(ioException);
        }
    }

    public static String writeJson(Value value) {
        return new String(writeJsonToBytes(value), context().configuration().getCharset());
    }

    public static void writeJson(Value value, OutputStream outputStream) {
        writeJson(jsonModule().configuration().getObjectMapper().getFactory(), value, outputStream);
    }

    public static void writeJson(JsonFactory jsonFactory, Value value, OutputStream outputStream) {
        if (valueIsNull(value)) {
            return;
        }
        try (JsonGenerator generator = jsonFactory.createGenerator(outputStream)) {
            switch (value.getType()) {
                case ENTITY:
                    writeJsonEntity(generator, asEntity(value));
                    break;
                case ARRAY:
                    writeArray(generator, asArray(value));
                    break;
                case BINARY:
                    outputStream.write(Arrays.toString(asBinary(value).getContent()).getBytes());
                case STRING:
                    outputStream.write(asPrimitive(value).getString().getBytes());
                case LONG:
                    outputStream.write(asPrimitive(value).getLong().toString().getBytes());
                case DOUBLE:
                    outputStream.write(asPrimitive(value).getDouble().toString().getBytes());
                case FLOAT:
                    outputStream.write(asPrimitive(value).getFloat().toString().getBytes());
                case INT:
                    outputStream.write(asPrimitive(value).getInt().toString().getBytes());
                case BOOL:
                    outputStream.write(asPrimitive(value).getBool().toString().getBytes());
                case BYTE:
                    outputStream.write(asPrimitive(value).getByte().toString().getBytes());
            }
        } catch (IOException ioException) {
            throw new JsonException(ioException);
        }
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
        if (valueIsNull(value)) return;
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
