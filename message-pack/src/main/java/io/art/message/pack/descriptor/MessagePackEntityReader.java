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
import static io.art.entity.immutable.Entity.*;
import static io.art.message.pack.constants.MessagePackConstants.ExceptionMessages.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
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
        if (isNull(value) || value.isNilValue()) return null;
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
        if (isNull(map) || map.isNilValue()) return null;
        EntityBuilder entityBuilder = entityBuilder();
        for (Map.Entry<org.msgpack.value.Value, org.msgpack.value.Value> entry : map.entrySet()) {
            org.msgpack.value.Value key = entry.getKey();
            if (isNull(key) || key.isNilValue()) continue;
            org.msgpack.value.Value value = entry.getValue();
            if (isNull(value) || value.isNilValue()) continue;
            switch (key.getValueType()) {
                case NIL:
                case BINARY:
                case ARRAY:
                case MAP:
                case EXTENSION:
                    continue;
                case BOOLEAN:
                    entityBuilder.lazyPut(key.asBooleanValue().getBoolean(), () -> readEntityField(value));
                    continue;
                case INTEGER:
                    IntegerValue integerValue = value.asIntegerValue();
                    if (integerValue.isInByteRange()) {
                        entityBuilder.lazyPut(bytePrimitive(integerValue.toByte()), () -> readEntityField(value));
                    }
                    if (integerValue.isInIntRange()) {
                        entityBuilder.lazyPut(intPrimitive(integerValue.toInt()), () -> readEntityField(value));
                    }
                    if (integerValue.isInLongRange()) {
                        entityBuilder.lazyPut(longPrimitive(integerValue.toLong()), () -> readEntityField(value));
                    }
                    continue;
                case FLOAT:
                    entityBuilder.lazyPut(key.asFloatValue().toFloat(), () -> readEntityField(value));
                    continue;
                case STRING:
                    entityBuilder.lazyPut(key.asStringValue().toString(), () -> readEntityField(value));
            }
        }
        return entityBuilder.build();
    }

    private static Value readEntityField(org.msgpack.value.Value value) {
        switch (value.getValueType()) {
            case BOOLEAN:
                return boolPrimitive(value.asBooleanValue().getBoolean());
            case INTEGER:
                IntegerValue integerValue = value.asIntegerValue();
                if (integerValue.isInByteRange()) {
                    return bytePrimitive(integerValue.asByte());
                }
                if (integerValue.isInIntRange()) {
                    return intPrimitive(integerValue.asInt());
                }
                if (integerValue.isInLongRange()) {
                    return longPrimitive(integerValue.asLong());
                }
            case FLOAT:
                return floatPrimitive(value.asFloatValue().toFloat());
            case STRING:
                return stringPrimitive(value.asStringValue().asString());
            case BINARY:
                return binary(value.asBinaryValue().asByteArray());
            case ARRAY:
                return readArray(value.asArrayValue());
            case MAP:
                return readMap(value.asMapValue());
            case EXTENSION:
            case NIL:
                return null;
        }
        throw new MessagePackMappingException(format(VALUE_TYPE_NOT_SUPPORTED, value.getValueType()));
    }

    private static ArrayValue readArray(org.msgpack.value.ArrayValue array) {
        if (isNull(array) || array.isNilValue()) return null;
        return array(index -> readMessagePack(array.get(index)), array::size);
    }
}
