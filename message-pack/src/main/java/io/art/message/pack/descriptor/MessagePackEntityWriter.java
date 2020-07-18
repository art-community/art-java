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

package io.art.message.pack.descriptor;

import io.art.entity.immutable.*;
import io.art.message.pack.exception.*;
import lombok.experimental.*;
import org.msgpack.core.*;
import org.msgpack.core.buffer.*;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.immutable.Value.*;
import static io.art.message.pack.constants.MessagePackConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static org.msgpack.core.MessagePack.*;
import static org.msgpack.value.ValueFactory.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class MessagePackEntityWriter {
    public static void writeMessagePack(Value value, OutputStream outputStream) {
        try {
            byte[] bytes = writeMessagePackToBytes(value);
            if (bytes == EMPTY_BYTES) {
                return;
            }
            outputStream.write(bytes);
        } catch (Throwable throwable) {
            throw new MessagePackMappingException(throwable);
        }
    }

    public static void writeMessagePack(Value value, Path path) {
        byte[] bytes = writeMessagePackToBytes(value);
        if (bytes == EMPTY_BYTES) {
            return;
        }
        writeFileQuietly(path, bytes);
    }

    public static byte[] writeMessagePackToBytes(Value value) {
        if (Value.valueIsNull(value)) {
            return EMPTY_BYTES;
        }
        try (ArrayBufferOutput output = new ArrayBufferOutput();
             MessagePacker packer = newDefaultPacker(output)) {
            packer.packValue(writeMessagePack(value)).close();
            return output.toByteArray();
        } catch (Throwable throwable) {
            throw new MessagePackMappingException(throwable);
        }
    }

    public static org.msgpack.value.Value writeMessagePack(Value value) {
        if (valueIsNull(value)) return null;
        if (isPrimitive(value)) return writePrimitive(asPrimitive(value));
        switch (value.getType()) {
            case ENTITY:
                return writeEntity(asEntity(value));
            case BINARY:
                return newBinary(asBinary(value).getContent());
            case ARRAY:
                return writeArray(asArray(value));
        }
        return null;
    }

    private static org.msgpack.value.Value writePrimitive(Primitive primitive) {
        if (valueIsNull(primitive)) return null;
        switch (primitive.getPrimitiveType()) {
            case STRING:
                return newString(primitive.getString());
            case LONG:
                return newInteger(primitive.getLong());
            case INT:
                return newInteger(primitive.getInt());
            case BYTE:
                return newInteger(primitive.getByte());
            case DOUBLE:
                return newFloat(primitive.getDouble());
            case FLOAT:
                return newFloat(primitive.getFloat());
            case BOOL:
                return newBoolean(primitive.getBool());
        }
        throw new MessagePackMappingException(format(VALUE_TYPE_NOT_SUPPORTED, primitive.getType()));
    }

    private static org.msgpack.value.Value writeArray(ArrayValue array) {
        if (valueIsNull(array)) return null;
        org.msgpack.value.Value[] values = new org.msgpack.value.Value[array.size()];
        List<Value> list = array.asList();
        int newArrayIndex = 0;
        for (Value value : list) {
            org.msgpack.value.Value element = writeMessagePack(value);
            if (isNull(element)) continue;
            values[newArrayIndex++] = element;
        }
        return newArray(Arrays.copyOf(values, newArrayIndex), true);
    }

    private static org.msgpack.value.Value writeEntity(Entity entity) {
        if (valueIsNull(entity)) return null;
        MapBuilder mapBuilder = newMapBuilder();
        Set<Primitive> keys = entity.asMap().keySet();
        for (Primitive key : keys) {
            if (valueIsNull(key)) continue;
            Value value = entity.get(key);
            if (valueIsNull(value)) continue;
            writeEntityField(mapBuilder, key, value);
        }
        return mapBuilder.build();
    }

    private static void writeEntityField(MapBuilder mapBuilder, Primitive key, Value value) {
        switch (key.getType()) {
            case STRING:
                mapBuilder.put(newString(key.getString()), writeMessagePack(value));
                return;
            case INT:
                mapBuilder.put(newInteger(key.getInt()), writeMessagePack(value));
                return;
            case BOOL:
                mapBuilder.put(newBoolean(key.getBool()), writeMessagePack(value));
                return;
            case LONG:
                mapBuilder.put(newInteger(key.getLong()), writeMessagePack(value));
                return;
            case BYTE:
                mapBuilder.put(newInteger(key.getByte()), writeMessagePack(value));
                return;
            case DOUBLE:
                mapBuilder.put(newFloat(key.getDouble()), writeMessagePack(value));
                return;
            case FLOAT:
                mapBuilder.put(newFloat(key.getFloat()), writeMessagePack(value));
        }
    }
}
