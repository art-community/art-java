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

package io.art.entity;

import lombok.experimental.*;
import io.art.entity.constants.ValueType.*;
import static java.util.Objects.*;
import static io.art.entity.constants.ValueType.CollectionElementsType.BOOL;
import static io.art.entity.constants.ValueType.CollectionElementsType.BYTE;
import static io.art.entity.constants.ValueType.CollectionElementsType.COLLECTION;
import static io.art.entity.constants.ValueType.CollectionElementsType.DOUBLE;
import static io.art.entity.constants.ValueType.CollectionElementsType.ENTITY;
import static io.art.entity.constants.ValueType.CollectionElementsType.FLOAT;
import static io.art.entity.constants.ValueType.CollectionElementsType.INT;
import static io.art.entity.constants.ValueType.CollectionElementsType.LONG;
import static io.art.entity.constants.ValueType.CollectionElementsType.STRING;
import static io.art.entity.constants.ValueType.CollectionElementsType.*;
import java.util.*;

@UtilityClass
public class CollectionValuesFactory {
    public static ArrayValue<String> stringCollection(Collection<String> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(STRING, value);
    }

    public static ArrayValue<Long> longCollection(Collection<Long> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(LONG, value);
    }

    public static ArrayValue<Integer> intCollection(Collection<Integer> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(INT, value);
    }

    public static ArrayValue<Boolean> boolCollection(Collection<Boolean> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(BOOL, value);
    }

    public static ArrayValue<Double> doubleCollection(Collection<Double> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(DOUBLE, value);
    }

    public static ArrayValue<Float> floatCollection(Collection<Float> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(FLOAT, value);
    }

    public static ArrayValue<Byte> byteCollection(Collection<Byte> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(BYTE, value);
    }


    public static ArrayValue<Long> longCollection(long[] value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(value);
    }

    public static ArrayValue<Integer> intCollection(int[] value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(value);
    }

    public static ArrayValue<Boolean> boolCollection(boolean[] value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(value);
    }

    public static ArrayValue<Double> doubleCollection(double[] value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(value);
    }

    public static ArrayValue<Float> floatCollection(float[] value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(value);
    }

    public static ArrayValue<Byte> byteCollection(byte[] value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(value);
    }


    public static <T extends Value> ArrayValue<T> collectionValue(CollectionElementsType type, Collection<T> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(type, value);
    }

    public static <T extends Entity> ArrayValue<T> entityCollection(Collection<T> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(ENTITY, value);
    }

    public static <T extends Value> ArrayValue<T> valueCollection(Collection<T> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(VALUE, value);
    }

    public static <T extends ArrayValue<?>> ArrayValue<T> collectionOfCollections(Collection<T> value) {
        if (isNull(value)) return new ArrayValue<>();
        return new ArrayValue<>(COLLECTION, value);
    }

    public static ArrayValue<?> emptyCollection() {
        return new ArrayValue<>();
    }
}
