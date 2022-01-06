package io.art.tarantool.descriptor;

import io.art.core.collection.*;
import io.art.message.pack.exception.*;
import io.art.meta.model.*;
import io.art.meta.schema.*;
import io.art.meta.transformer.*;
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
import java.util.*;

public class TarantoolModelReader {
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
                if (type.externalKind().isArray()) {
                    return transformer.fromLazyArray(readArray(orElse(type.arrayComponentType(), () -> type.parameters().get(0)), value.asArrayValue()));
                }
                if (type.externalKind() == ENTITY) {
                    return readEntity(type, value.asArrayValue());
                }
                throw new MessagePackException(format(MESSAGE_PACK_ARRAY_EXCEPTION, value, type));
            case MAP:
                if (type.externalKind() == MAP || type.externalKind() == LAZY_MAP) {
                    return transformer.fromLazyMap(readMap(type.parameters().get(0), type.parameters().get(1), value.asMapValue()));
                }
                throw new MessagePackException(format(MESSAGE_PACK_MAP_EXCEPTION, value, type));
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

    private <T> T readEntity(MetaType<T> type, ArrayValue values) {
        if (isNull(values) || values.isNilValue()) return null;
        MetaCreatorTemplate.MetaCreatorInstance creator = type.declaration().creator().validate(MessagePackException::new).instantiate();
        ImmutableArray<MetaProperty<?>> properties = creator.propertyArray();
        for (int index = 0; index < values.size(); index++) {
            Value value = values.get(index);
            if (isNull(value) || value.isNilValue()) continue;
            MetaProperty<?> property = properties.get(index);
            creator.putValue(property, read(property.type(), value));
        }
        return cast(creator.create());
    }

    private ImmutableLazyArrayImplementation<?> readArray(MetaType<?> elementsType, org.msgpack.value.ArrayValue array) {
        if (isNull(array) || array.isNilValue()) return emptyImmutableLazyArray();
        return cast(immutableLazyArray(index -> read(elementsType, array.get(index)), array.size()));
    }
}
