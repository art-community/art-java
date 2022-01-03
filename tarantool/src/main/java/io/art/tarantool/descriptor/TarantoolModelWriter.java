package io.art.tarantool.descriptor;

import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.meta.model.*;
import io.art.meta.schema.*;
import io.art.meta.transformer.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.Objects.*;
import static org.msgpack.value.ValueFactory.*;
import java.util.*;

public class TarantoolModelWriter {
    public org.msgpack.value.Value write(MetaType<?> type, Object value) {
        if (isNull(value)) return null;
        MetaTransformer<?> transformer = type.outputTransformer();
        switch (type.externalKind()) {
            case MAP:
            case LAZY_MAP:
                return writeMap(type.parameters().get(0), type.parameters().get(1), transformer.toMap(cast(value)));
            case ARRAY:
            case LAZY_ARRAY:
                return writeArray(orElse(type.arrayComponentType(), () -> type.parameters().get(0)), transformer.toArray(cast(value)));
            case LAZY:
                return write(type.parameters().get(0), transformer.toLazy(cast(value)).get());
            case STRING:
                return newString(transformer.toString(cast(value)));
            case LONG:
                return newInteger(transformer.toLong(cast(value)));
            case DOUBLE:
                return newFloat(transformer.toDouble(cast(value)));
            case FLOAT:
                return newFloat(transformer.toFloat(cast(value)));
            case INTEGER:
                return newInteger(transformer.toInteger(cast(value)));
            case BOOLEAN:
                return newBoolean(transformer.toBoolean(cast(value)));
            case CHARACTER:
                return newString(EMPTY_STRING + transformer.toCharacter(cast(value)));
            case SHORT:
                return newInteger(transformer.toShort(cast(value)));
            case BYTE:
                return newInteger(transformer.toByte(cast(value)));
            case BINARY:
                return newBinary(transformer.toByteArray(cast(value)));
            case ENTITY:
                return writeEntity(type, cast(value));
        }
        throw new ImpossibleSituationException();
    }


    private org.msgpack.value.ArrayValue writeArray(MetaType<?> elementType, List<?> array) {
        List<org.msgpack.value.Value> values = dynamicArray(array.size());
        for (Object element : array) {
            if (isNull(element)) {
                values.add(newNil());
                continue;
            }
            values.add(write(elementType, element));
        }
        return newArray(values);
    }

    private org.msgpack.value.ArrayValue writeEntity(MetaType<?> type, Object value) {
        MetaProviderTemplate.MetaProviderInstance provider = type.declaration().provider().instantiate(value);
        ImmutableArray<MetaProperty<?>> properties = provider.propertyArray();
        List<org.msgpack.value.Value> values = dynamicArray(properties.size());
        for (MetaProperty<?> property : properties) {
            Object propertyValue = provider.getValue(property);
            if (isNull(propertyValue)) {
                values.add(newNil());
                continue;
            }
            values.add(write(property.type(), propertyValue));
        }
        return newArray(values);
    }

    private org.msgpack.value.MapValue writeMap(MetaType<?> keyType, MetaType<?> valueType, Map<?, ?> value) {
        MapBuilder mapBuilder = newMapBuilder();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            Object entryKey = entry.getKey();
            Object entryValue = entry.getValue();
            if (isNull(entryKey) || isNull(entryValue)) continue;
            mapBuilder.put(write(keyType, entryKey), write(valueType, entryValue));
        }
        return mapBuilder.build();
    }
}
