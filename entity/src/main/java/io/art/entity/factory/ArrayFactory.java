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

import com.google.common.collect.*;
import io.art.core.checker.*;
import io.art.entity.immutable.*;
import lombok.experimental.*;
import static com.google.common.collect.ImmutableList.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.ArrayValue.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class ArrayFactory {
    public static ArrayValue stringArray(Collection<String> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<String>> list = of(value);
        return new ArrayValue(index -> stringPrimitive(cast(list.get(index))), lazy(value::size));
    }

    public static ArrayValue longArray(Collection<Long> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<Long>> list = of(value);
        return new ArrayValue(index -> longPrimitive(cast(list.get(index))), lazy(value::size));
    }

    public static ArrayValue intArray(Collection<Integer> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<Integer>> list = of(value);
        return new ArrayValue(index -> intPrimitive(cast(list.get(index))), lazy(value::size));
    }

    public static ArrayValue boolArray(Collection<Boolean> value) {
        ImmutableList<Collection<Boolean>> list = of(value);
        return new ArrayValue(index -> boolPrimitive(cast(list.get(index))), lazy(value::size));
    }

    public static ArrayValue doubleArray(Collection<Double> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<Double>> list = of(value);
        return new ArrayValue(index -> doublePrimitive(cast(list.get(index))), lazy(value::size));
    }

    public static ArrayValue floatArray(Collection<Float> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<Float>> list = of(value);
        return new ArrayValue(index -> floatPrimitive(cast(list.get(index))), lazy(value::size));
    }

    public static ArrayValue byteArray(Collection<Byte> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<Byte>> list = of(value);
        return new ArrayValue(index -> bytePrimitive(cast(list.get(index))), lazy(value::size));
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


    public static <T extends Value> ArrayValue array(Collection<T> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<T>> list = of(value);
        return new ArrayValue(index -> cast(list.get(index)), lazy(value::size));
    }

    public static <T extends Value> ArrayValue array(Function<Integer, T> valueProvider, Supplier<Integer> sizeProvider) {
        return new ArrayValue(valueProvider, lazy(sizeProvider));
    }

    public static <T extends Entity> ArrayValue entityArray(Collection<T> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<T>> list = of(value);
        return new ArrayValue(index -> cast(list.get(index)), lazy(value::size));
    }

    public static <T extends ArrayValue> ArrayValue arrayOfArrays(Collection<T> value) {
        if (EmptinessChecker.isEmpty(value)) return EMPTY;
        ImmutableList<Collection<T>> list = of(value);
        return new ArrayValue(index -> cast(list.get(index)), lazy(value::size));
    }

    public static ArrayValue emptyArray() {
        return EMPTY;
    }
}
