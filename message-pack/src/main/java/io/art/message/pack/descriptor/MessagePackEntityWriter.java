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

import lombok.experimental.*;
import org.msgpack.core.buffer.*;
import org.msgpack.value.*;
import io.art.core.checker.*;
import io.art.entity.Value;
import io.art.entity.*;
import io.art.message.pack.exception.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.core.MessagePack.*;
import static org.msgpack.value.ValueFactory.MapBuilder;
import static org.msgpack.value.ValueFactory.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.Value.*;
import static io.art.entity.constants.CollectionMode.*;
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
            case COLLECTION:
                return writeCollectionValue(asCollection(value));
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

    private static org.msgpack.value.Value writeCollectionValue(CollectionValue<?> collectionValue) {
        if (Value.isEmpty(collectionValue)) {
            return newArray();
        }
        switch (collectionValue.getElementsType()) {
            case STRING:
                return newArray(collectionValue.getStringList()
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(ValueFactory::newString)
                        .collect(toList()));
            case LONG:
                return newArray((collectionValue.getCollectionMode() == PRIMITIVE_ARRAY
                        ? fixedArrayOf(collectionValue.getLongArray())
                        : collectionValue.getLongList())
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(primitive -> newInteger((Long) primitive))
                        .collect(toList()));
            case DOUBLE:
                return newArray((collectionValue.getCollectionMode() == PRIMITIVE_ARRAY
                        ? fixedArrayOf(collectionValue.getDoubleArray())
                        : collectionValue.getDoubleList())
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(primitive -> newFloat((Double) primitive))
                        .collect(toList()));
            case FLOAT:
                return newArray((collectionValue.getCollectionMode() == PRIMITIVE_ARRAY
                        ? fixedArrayOf(collectionValue.getFloatArray())
                        : collectionValue.getFloatList())
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(primitive -> newFloat((Float) primitive))
                        .collect(toList()));
            case INT:
                return newArray((collectionValue.getCollectionMode() == PRIMITIVE_ARRAY
                        ? fixedArrayOf(collectionValue.getIntArray())
                        : collectionValue.getIntList())
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(primitive -> newInteger((Integer) primitive))
                        .collect(toList()));
            case BOOL:
                return newArray((collectionValue.getCollectionMode() == PRIMITIVE_ARRAY
                        ? fixedArrayOf(collectionValue.getBoolArray())
                        : collectionValue.getBoolList())
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(primitive -> newBoolean((Boolean) primitive))
                        .collect(toList()));
            case BYTE:
                if (collectionValue.getCollectionMode() == PRIMITIVE_ARRAY) {
                    return newBinary(collectionValue.getByteArray());
                }
                return newArray(collectionValue.getByteList()
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(ValueFactory::newInteger)
                        .collect(toList()));
            case COLLECTION:
                return newArray(collectionValue.getCollectionsList()
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(MessagePackEntityWriter::writeCollectionValue)
                        .collect(toList()));
            case ENTITY:
                return newArray(collectionValue.getEntityList()
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(MessagePackEntityWriter::writeEntity)
                        .collect(toList()));
            case VALUE:
                return newArray(collectionValue.getValueList()
                        .stream()
                        .filter(EmptinessChecker::isNotEmpty)
                        .map(MessagePackEntityWriter::writeMessagePack)
                        .collect(toList()));

        }
        return emptyArray();
    }

    private static org.msgpack.value.Value writeEntity(Entity entity) {
        if (Value.isEmpty(entity)) {
            return emptyMap();
        }
        MapBuilder mapBuilder = newMapBuilder();
        entity.getFields().entrySet().forEach(entry -> writeEntityEntry(mapBuilder, entry));
        return mapBuilder.build();
    }

    private static void writeEntityEntry(MapBuilder mapBuilder, Map.Entry<? extends Value, ? extends Value> entry) {
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
            case COLLECTION:
                mapBuilder.put(newString(key.toString()), writeCollectionValue(asCollection(value)));
                return;
            case ENTITY:
                mapBuilder.put(newString(key.toString()), writeEntity(asEntity(value)));
        }
    }
}
