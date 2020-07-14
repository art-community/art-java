/*
 * ART Java
 *
 * Copyright 2019 ART
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
    public static CollectionValue<String> stringCollection(Collection<String> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(STRING, value);
    }

    public static CollectionValue<Long> longCollection(Collection<Long> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(LONG, value);
    }

    public static CollectionValue<Integer> intCollection(Collection<Integer> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(INT, value);
    }

    public static CollectionValue<Boolean> boolCollection(Collection<Boolean> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(BOOL, value);
    }

    public static CollectionValue<Double> doubleCollection(Collection<Double> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(DOUBLE, value);
    }

    public static CollectionValue<Float> floatCollection(Collection<Float> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(FLOAT, value);
    }

    public static CollectionValue<Byte> byteCollection(Collection<Byte> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(BYTE, value);
    }


    public static CollectionValue<Long> longCollection(long[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    public static CollectionValue<Integer> intCollection(int[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    public static CollectionValue<Boolean> boolCollection(boolean[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    public static CollectionValue<Double> doubleCollection(double[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    public static CollectionValue<Float> floatCollection(float[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }

    public static CollectionValue<Byte> byteCollection(byte[] value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(value);
    }


    public static <T extends Value> CollectionValue<T> collectionValue(CollectionElementsType type, Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(type, value);
    }

    public static <T extends Entity> CollectionValue<T> entityCollection(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(ENTITY, value);
    }

    public static <T extends Value> CollectionValue<T> valueCollection(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(VALUE, value);
    }

    public static <T extends CollectionValue<?>> CollectionValue<T> collectionOfCollections(Collection<T> value) {
        if (isNull(value)) return new CollectionValue<>();
        return new CollectionValue<>(COLLECTION, value);
    }

    public static CollectionValue<?> emptyCollection() {
        return new CollectionValue<>();
    }
}
