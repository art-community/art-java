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

package io.art.entity.factory;

import io.art.core.checker.*;
import io.art.entity.immutable.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.ArrayValue.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class ArrayFactory {
    public static ArrayValue stringArray(List<String> value) {
        if (isNull(value)) return null;
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> stringPrimitive(cast(value.get(index))), lazy(value::size));
    }

    public static ArrayValue longArray(List<Long> value) {
        if (isNull(value)) return null;
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> longPrimitive(cast(value.get(index))), lazy(value::size));
    }

    public static ArrayValue intArray(List<Integer> value) {
        if (isNull(value)) return null;
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> intPrimitive(cast(value.get(index))), lazy(value::size));
    }

    public static ArrayValue boolArray(List<Boolean> value) {
        return new ArrayValue(index -> boolPrimitive(cast(value.get(index))), lazy(value::size));
    }

    public static ArrayValue doubleArray(List<Double> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> doublePrimitive(cast(value.get(index))), lazy(value::size));
    }

    public static ArrayValue floatArray(List<Float> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> floatPrimitive(cast(value.get(index))), lazy(value::size));
    }

    public static ArrayValue byteArray(List<Byte> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> bytePrimitive(cast(value.get(index))), lazy(value::size));
    }


    public static ArrayValue longArray(long[] value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> longPrimitive(value[index]), lazy(() -> value.length));
    }

    public static ArrayValue intArray(int[] value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> intPrimitive(value[index]), lazy(() -> value.length));
    }

    public static ArrayValue boolArray(boolean[] value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> boolPrimitive(value[index]), lazy(() -> value.length));
    }

    public static ArrayValue doubleArray(double[] value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> doublePrimitive(value[index]), lazy(() -> value.length));
    }

    public static ArrayValue floatArray(float[] value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> floatPrimitive(value[index]), lazy(() -> value.length));
    }

    public static ArrayValue byteArray(byte[] value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(index -> bytePrimitive(value[index]), lazy(() -> value.length));
    }


    public static <T extends Value> ArrayValue array(List<T> value) {
        if (isNull(value)) return null;
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(value::get, lazy(value::size));
    }

    public static <T extends Value> ArrayValue array(Function<Integer, T> valueProvider, Supplier<Integer> sizeProvider) {
        return new ArrayValue(valueProvider, lazy(sizeProvider));
    }

    public static <T extends Entity> ArrayValue entityArray(List<T> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(value::get, lazy(value::size));
    }

    public static <T extends ArrayValue> ArrayValue arrayOfArrays(List<T> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        return new ArrayValue(value::get, lazy(value::size));
    }

    public static ArrayValue emptyArray() {
        return EMPTY;
    }
}
