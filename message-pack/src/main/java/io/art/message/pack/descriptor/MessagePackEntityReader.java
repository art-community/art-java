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

import io.art.entity.builder.*;
import io.art.entity.immutable.ArrayValue;
import io.art.entity.immutable.Value;
import io.art.message.pack.exception.*;
import lombok.experimental.*;
import org.msgpack.value.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.entity.factory.ArrayFactory.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.BinaryValue.*;
import static io.art.entity.immutable.Entity.asPrimitive;
import static io.art.entity.immutable.Entity.isPrimitive;
import static io.art.entity.immutable.Entity.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.msgpack.core.MessagePack.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;


@UtilityClass
public class MessagePackEntityReader {
    public static Value readMessagePack(InputStream inputStream) {
        return readMessagePack(toByteArray(inputStream));
    }

    public static Value readMessagePack(Path path) {
        return readMessagePack(readFileBytes(path));
    }

    public static Value readMessagePack(byte[] bytes) {
        try {
            return readMessagePack(newDefaultUnpacker(bytes).unpackValue());
        } catch (Throwable throwable) {
            throw new MessagePackMappingException(throwable);
        }
    }

    public static Value readMessagePack(org.msgpack.value.Value value) {
        if (isNull(value)) {
            return null;
        }
        switch (value.getValueType()) {
            case NIL:
            case EXTENSION:
                return null;
            case BOOLEAN:
                return boolPrimitive(value.asBooleanValue().getBoolean());
            case INTEGER:
                IntegerValue integerValue = value.asIntegerValue();
                if (integerValue.isInByteRange()) {
                    return bytePrimitive(integerValue.toByte());
                }
                if (integerValue.isInIntRange()) {
                    return intPrimitive(integerValue.toInt());
                }
                if (integerValue.isInLongRange()) {
                    return longPrimitive(integerValue.toLong());
                }
            case FLOAT:
                return doublePrimitive(value.asFloatValue().toDouble());
            case STRING:
                return stringPrimitive(value.asStringValue().toString());
            case BINARY:
                return byteArray(value.asBinaryValue().asByteArray());
            case ARRAY:
                return readArray(value.asArrayValue());
            case MAP:
                return readMap(value.asMapValue());
        }
        return null;
    }

    private static Value readMap(org.msgpack.value.MapValue map) {
        if (map.size() == 0) return entityBuilder().build();
        if (map.keySet().stream().filter(Objects::nonNull).allMatch(org.msgpack.value.Value::isStringValue)) {
            EntityBuilder entityBuilder = entityBuilder();
            for (Map.Entry<org.msgpack.value.Value, org.msgpack.value.Value> entry : map.map().entrySet()) {
                if (isNull(entry.getKey())) continue;
                org.msgpack.value.Value value = entry.getValue();
                String key = entry.getKey().asStringValue().toString();
                switch (value.getValueType()) {
                    case BOOLEAN:
                        entityBuilder.lazyPut(key, () -> boolPrimitive(value.asBooleanValue().getBoolean()));
                        break;
                    case INTEGER:
                        IntegerValue integerValue = value.asIntegerValue();
                        if (integerValue.isInByteRange()) {
                            entityBuilder.lazyPut(key, () -> bytePrimitive(integerValue.asByte()));
                        }
                        if (integerValue.isInIntRange()) {
                            entityBuilder.lazyPut(key, () -> intPrimitive(integerValue.asInt()));
                        }
                        if (integerValue.isInLongRange()) {
                            entityBuilder.lazyPut(key, () -> longPrimitive(integerValue.asLong()));
                        }
                        break;
                    case FLOAT:
                        entityBuilder.lazyPut(key, () -> floatPrimitive(value.asFloatValue().toFloat()));
                        break;
                    case STRING:
                        entityBuilder.lazyPut(key, () -> stringPrimitive(value.asStringValue().asString()));
                        break;
                    case BINARY:
                        entityBuilder.lazyPut(key, () -> binary(value.asBinaryValue().asByteArray()));
                        break;
                    case ARRAY:
                        entityBuilder.lazyPut(key, () -> readArray(value.asArrayValue()));
                        break;
                    case MAP:
                        entityBuilder.lazyPut(key, () -> readMap(value.asMapValue()));
                }
            }
            return entityBuilder.build();
        }
        EntityBuilder valueBuilder = entityBuilder();
        for (Map.Entry<org.msgpack.value.Value, org.msgpack.value.Value> entry : map.map().entrySet()) {
            org.msgpack.value.Value key = entry.getKey();
            org.msgpack.value.Value value = entry.getValue();
            if (isNull(key)) continue;
            Value keyValue = readMessagePack(key);
            if (!isPrimitive(keyValue)) continue;
            valueBuilder.lazyPut(asPrimitive(keyValue), () -> readMessagePack(value));
        }
        return valueBuilder.build();
    }

    private static ArrayValue readArray(org.msgpack.value.ArrayValue array) {
        if (array.size() == 0) return emptyArray();
        return array(array.list()
                .stream()
                .filter(element -> !element.isExtensionValue())
                .map(MessagePackEntityReader::readMessagePack)
                .collect(toList()));
    }
}
