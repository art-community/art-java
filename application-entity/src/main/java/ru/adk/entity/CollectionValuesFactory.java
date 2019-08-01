package ru.adk.entity;

import ru.adk.entity.constants.ValueType.CollectionElementsType;
import static java.util.Objects.isNull;
import static ru.adk.entity.constants.ValueType.CollectionElementsType.*;
import java.util.Collection;

public interface CollectionValuesFactory {
    static CollectionValue<String> stringCollection(Collection<String> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(STRING, value);
    }

    static CollectionValue<Long> longCollection(Collection<Long> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(LONG, value);
    }

    static CollectionValue<Integer> intCollection(Collection<Integer> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(INT, value);
    }

    static CollectionValue<Boolean> boolCollection(Collection<Boolean> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(BOOL, value);
    }

    static CollectionValue<Double> doubleCollection(Collection<Double> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(DOUBLE, value);
    }

    static CollectionValue<Float> floatCollection(Collection<Float> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(FLOAT, value);
    }

    static CollectionValue<Byte> byteCollection(Collection<Byte> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(BYTE, value);
    }


    static CollectionValue<Long> longCollection(long[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    static CollectionValue<Integer> intCollection(int[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    static CollectionValue<Boolean> boolCollection(boolean[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    static CollectionValue<Double> doubleCollection(double[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    static CollectionValue<Float> floatCollection(float[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    static CollectionValue<Byte> byteCollection(byte[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }


    static <T extends Value> CollectionValue<T> collectionValue(CollectionElementsType type, Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(type, value);
    }

    static <T extends Entity> CollectionValue<T> entityCollection(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(ENTITY, value);
    }

    static <T extends Value> CollectionValue<T> valueCollection(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(VALUE, value);
    }

    static <T extends CollectionValue<?>> CollectionValue<T> collectionOfCollections(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(COLLECTION, value);
    }

    static <T extends StringParametersMap> CollectionValue<T> stringParametersCollection(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(STRING_PARAMETERS_MAP, value);
    }

    static <T extends MapValue> CollectionValue<T> mapCollection(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(MAP, value);
    }

    static CollectionValue<?> emptyCollection() {
        return new CollectionValue<>();
    }
}