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

package io.art.value.mapping;

import io.art.core.annotation.*;
import io.art.core.caster.*;
import io.art.value.factory.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.value.factory.ArrayValueFactory.*;
import static java.util.Arrays.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
@UsedByGenerator
public class ArrayMapping {
    public ArrayToModelMapper<int[]> toIntArray = array -> let(array, ArrayValue::intArray);
    public ArrayToModelMapper<long[]> toLongArray = array -> let(array, ArrayValue::longArray);
    public ArrayToModelMapper<short[]> toShortArray = array -> let(array, ArrayValue::shortArray);
    public ArrayToModelMapper<double[]> toDoubleArray = array -> let(array, ArrayValue::doubleArray);
    public ArrayToModelMapper<float[]> toFloatArray = array -> let(array, ArrayValue::floatArray);
    public ArrayToModelMapper<byte[]> toByteArray = array -> let(array, ArrayValue::byteArray);
    public ArrayToModelMapper<char[]> toCharArray = array -> let(array, ArrayValue::charArray);
    public ArrayToModelMapper<boolean[]> toBoolArray = array -> let(array, ArrayValue::boolArray);

    public static <T> ArrayToModelMapper<Collection<T>> toCollection(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsList(elementMapper));
    }

    public static <T> ArrayToModelMapper<T[]> toArray(Function<Integer, T[]> factory, ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsList(elementMapper).toArray(factory.apply(array.size())));
    }

    public static <T> ArrayToModelMapper<T[]> toArray(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> Caster.cast(let(array, notNull -> notNull.mapAsList(elementMapper).toArray()));
    }

    public static <T> ArrayToModelMapper<List<T>> toList(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsList(elementMapper));
    }

    public static <T> ArrayToModelMapper<Set<T>> toSet(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsSet(elementMapper));
    }

    public static <T> ArrayToModelMapper<Queue<T>> toQueue(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsQueue(elementMapper));
    }

    public static <T> ArrayToModelMapper<Deque<T>> toDeque(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsDeque(elementMapper));
    }


    public static <T> ArrayToModelMapper<Collection<T>> toMutableCollection(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToList(elementMapper));
    }

    public static <T> ArrayToModelMapper<List<T>> toMutableList(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToList(elementMapper));
    }

    public static <T> ArrayToModelMapper<Set<T>> toMutableSet(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToSet(elementMapper));
    }

    public static <T> ArrayToModelMapper<Queue<T>> toMutableQueue(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToQueue(elementMapper));
    }

    public static <T> ArrayToModelMapper<Deque<T>> toMutableDeque(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToDeque(elementMapper));
    }


    public ArrayFromModelMapper<int[]> fromIntArray = array -> let(array, ArrayValueFactory::intArray);
    public ArrayFromModelMapper<long[]> fromLongArray = array -> let(array, ArrayValueFactory::longArray);
    public ArrayFromModelMapper<short[]> fromShortArray = array -> let(array, ArrayValueFactory::shortArray);
    public ArrayFromModelMapper<double[]> fromDoubleArray = array -> let(array, ArrayValueFactory::doubleArray);
    public ArrayFromModelMapper<float[]> fromFloatArray = array -> let(array, ArrayValueFactory::floatArray);
    public ArrayFromModelMapper<byte[]> fromByteArray = array -> let(array, ArrayValueFactory::byteArray);
    public ArrayFromModelMapper<char[]> fromCharArray = array -> let(array, ArrayValueFactory::charArray);
    public ArrayFromModelMapper<boolean[]> fromBoolArray = array -> let(array, ArrayValueFactory::boolArray);

    public static <T> ArrayFromModelMapper<Collection<T>> fromCollection(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return list -> let(list, notNull -> array(list, elementMapper));
    }

    public static <T> ArrayFromModelMapper<T[]> fromArray(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> array(asList(array), elementMapper));
    }

    public static <T> ArrayFromModelMapper<List<T>> fromList(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return list -> let(list, notNull -> array(list, elementMapper));
    }

    public static <T> ArrayFromModelMapper<Set<T>> fromSet(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNull -> array(collection, elementMapper));
    }

    public static <T> ArrayFromModelMapper<Queue<T>> fromQueue(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNull -> array(collection, elementMapper));
    }

    public static <T> ArrayFromModelMapper<Deque<T>> fromDeque(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNull -> array(collection, elementMapper));
    }
}
