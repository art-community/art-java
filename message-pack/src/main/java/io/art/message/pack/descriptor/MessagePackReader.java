/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.message.pack.exception.MessagePackException;
import io.art.meta.descriptor.Reader;
import io.art.meta.model.*;
import io.art.meta.schema.MetaCreatorTemplate.*;
import io.art.meta.transformer.*;
import org.msgpack.core.*;
import org.msgpack.value.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableLazyArrayImplementation.*;
import static io.art.core.collection.ImmutableLazyMapImplementation.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.message.pack.constants.MessagePackConstants.Errors.*;
import static io.art.meta.constants.MetaConstants.MetaTypeExternalKind.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static org.msgpack.core.MessagePack.*;
import java.io.*;
import java.nio.*;
import java.util.*;


@Public
public class MessagePackReader implements Reader {
    @Override
    public <T> T read(MetaType<T> type, InputStream inputStream) {
        try (MessageUnpacker unpacker = newDefaultUnpacker(inputStream)) {
            return read(type, unpacker.unpackValue());
        } catch (Throwable throwable) {
            throw new MessagePackException(throwable);
        }
    }

    @Override
    public <T> T read(MetaType<T> type, ByteBuffer nioBuffer) {
        try (MessageUnpacker unpacker = newDefaultUnpacker(nioBuffer)) {
            return read(type, unpacker.unpackValue());
        } catch (Throwable throwable) {
            throw new MessagePackException(throwable);
        }
    }

    public <T> T read(MetaType<T> type, org.msgpack.value.Value value) {
        if (isNull(value) || value.isNilValue()) return null;
        MetaTransformer<T> transformer = type.inputTransformer();
        if (type.externalKind() == LAZY) {
            return transformer.fromLazy(() -> read(type.parameters().get(0), value));
        }
        switch (value.getValueType()) {
            case NIL:
            case EXTENSION:
                return null;
            case BOOLEAN:
                return transformer.fromBoolean(value.asBooleanValue().getBoolean());
            case INTEGER:
                IntegerValue integerValue = value.asIntegerValue();
                if (integerValue.isInByteRange()) {
                    return transformer.fromByte(integerValue.toByte());
                }
                if (integerValue.isInIntRange()) {
                    return transformer.fromInteger(integerValue.toInt());
                }
                if (integerValue.isInLongRange()) {
                    return transformer.fromLong(integerValue.toLong());
                }
            case FLOAT:
                return transformer.fromDouble(value.asFloatValue().toDouble());
            case STRING:
                return transformer.fromString(value.asStringValue().toString());
            case BINARY:
                return transformer.fromByteArray(value.asBinaryValue().asByteArray());
            case ARRAY:
                if (type.externalKind() != ARRAY && type.externalKind() != LAZY_ARRAY) {
                    throw new MessagePackException(format(MESSAGE_PACK_ARRAY_EXCEPTION, value, type));
                }
                return transformer.fromLazyArray(readArray(orElse(type.arrayComponentType(), () -> type.parameters().get(0)), value.asArrayValue()));
            case MAP:
                if (type.externalKind() == MAP || type.externalKind() == LAZY_MAP) {
                    return transformer.fromLazyMap(readMap(type.parameters().get(0), type.parameters().get(1), value.asMapValue()));
                }
                if (type.externalKind() != ENTITY) {
                    throw new MessagePackException(format(MESSAGE_PACK_MAP_EXCEPTION, value, type));
                }
                return readEntity(type, value.asMapValue());
        }
        throw new MessagePackException(format(VALUE_TYPE_NOT_SUPPORTED, value.getValueType()));
    }


    private ImmutableLazyMapImplementation<?, ?> readMap(MetaType<?> keyType, MetaType<?> valueType, MapValue mapValue) {
        if (isNull(mapValue) || mapValue.isNilValue()) return emptyImmutableLazyMap();
        Map<?, Value> mapping = map(mapValue.size());
        Map<Value, Value> rawMap = mapValue.map();
        for (Value key : mapValue.keySet()) {
            if (isNull(key) || key.isNilValue()) continue;
            Value value = rawMap.get(key);
            if (isNull(value) || value.isNilValue()) continue;
            mapping.put(cast(read(keyType, key)), value);
        }
        return cast(immutableLazyMap(mapping.keySet(), key -> read(valueType, mapping.get(key))));
    }

    private <T> T readEntity(MetaType<T> type, MapValue map) {
        if (isNull(map) || map.isNilValue()) return null;
        MetaCreatorInstance creator = type.declaration().creator().validate(MessagePackException::new).instantiate();
        ImmutableMap<String, MetaProperty<?>> properties = creator.properties();
        for (Map.Entry<org.msgpack.value.Value, org.msgpack.value.Value> entry : map.entrySet()) {
            org.msgpack.value.Value key = entry.getKey();
            if (isNull(key) || !key.isStringValue()) continue;
            org.msgpack.value.Value value = entry.getValue();
            if (isNull(value) || value.isNilValue()) continue;
            MetaProperty<?> property = properties.get(key.asStringValue().toString());
            creator.putValue(property, read(property.type(), value));
        }
        return cast(creator.create());
    }

    private ImmutableLazyArrayImplementation<?> readArray(MetaType<?> elementsType, org.msgpack.value.ArrayValue array) {
        if (isNull(array) || array.isNilValue()) return emptyImmutableLazyArray();
        return cast(immutableLazyArray(index -> read(elementsType, array.get(index)), array.size()));
    }
}
