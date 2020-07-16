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
import org.msgpack.core.buffer.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.entity.immutable.Value.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.core.MessagePack.*;
import static org.msgpack.value.ValueFactory.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

@UtilityClass
public class MessagePackEntityWriter {
    public static void writeMessagePack(Value value, OutputStream outputStream) {
        if (isNull(outputStream)) {
            return;
        }
        try {
            outputStream.write(writeMessagePackToBytes(value));
        } catch (Throwable throwable) {
            throw new MessagePackMappingException(throwable);
        }
    }

    public static void writeMessagePack(Value value, Path path) {
        writeFileQuietly(path, writeMessagePackToBytes(value));
    }

    public static byte[] writeMessagePackToBytes(Value value) {
        if (Value.isEmpty(value)) {
            return EMPTY_BYTES;
        }
        ArrayBufferOutput output = new ArrayBufferOutput();
        try {
            newDefaultPacker(output).packValue(writeMessagePack(value)).close();
            return output.toByteArray();
        } catch (Throwable throwable) {
            throw new MessagePackMappingException(throwable);
        }
    }

    public static org.msgpack.value.Value writeMessagePack(Value value) {
        if (isPrimitive(value)) {
            return writePrimitive(asPrimitive(value));
        }
        switch (value.getType()) {
            case ENTITY:
                return writeEntity(asEntity(value));
            case ARRAY:
                return writeArray(asArray(value));
        }
        return newNil();
    }

    private static org.msgpack.value.Value writePrimitive(Primitive primitive) {
        if (Value.isEmpty(primitive)) {
            return newNil();
        }
        switch (primitive.getPrimitiveType()) {
            case STRING:
                return newString(primitive.getString());
            case LONG:
                return newInteger(primitive.getLong());
            case INT:
                return newInteger(primitive.getInt());
            case DOUBLE:
                return newFloat(primitive.getDouble());
            case FLOAT:
                return newFloat(primitive.getFloat());
            case BOOL:
                return newBoolean(primitive.getBool());
            case BYTE:
                return newBinary(new byte[]{primitive.getByte()});
        }
        return newNil();
    }

    private static org.msgpack.value.Value writeArray(ArrayValue array) {
        if (Value.isEmpty(array)) {
            return newArray();
        }
        return newArray(array.asStream().map(MessagePackEntityWriter::writeMessagePack).collect(toList()));
    }

    private static org.msgpack.value.Value writeEntity(Entity entity) {
        if (Value.isEmpty(entity)) {
            return emptyMap();
        }
        MapBuilder mapBuilder = newMapBuilder();
        entity.asMap().entrySet().forEach(entry -> writeEntityField(mapBuilder, entry));
        return mapBuilder.build();
    }

    private static void writeEntityField(MapBuilder mapBuilder, Map.Entry<? extends Value, ? extends Value> entry) {
        final Value key = entry.getKey();
        if (isEmpty(key) || !isPrimitive(key) || isEmpty(entry.getValue())) {
            return;
        }
        Value value = entry.getValue();
        switch (value.getType()) {
            case STRING:
            case INT:
            case BOOL:
            case LONG:
            case BYTE:
            case DOUBLE:
            case FLOAT:
                mapBuilder.put(newString(key.toString()), writePrimitive(asPrimitive(value)));
                return;
            case ARRAY:
                mapBuilder.put(newString(key.toString()), writeArray(asArray(value)));
                return;
            case ENTITY:
                mapBuilder.put(newString(key.toString()), writeEntity(asEntity(value)));
        }
    }
}
