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

@UtilityClass
@UsedByGenerator
public class ArrayMapping {
    public ArrayToModelMapper<int[]> toIntArray = ArrayValue::intArray;
    public ArrayToModelMapper<long[]> toLongArray = ArrayValue::longArray;
    public ArrayToModelMapper<short[]> toShortArray = ArrayValue::shortArray;
    public ArrayToModelMapper<double[]> toDoubleArray = ArrayValue::doubleArray;
    public ArrayToModelMapper<float[]> toFloatArray = ArrayValue::floatArray;
    public ArrayToModelMapper<byte[]> toByteArray = ArrayValue::byteArray;
    public ArrayToModelMapper<char[]> toCharArray = ArrayValue::charArray;
    public ArrayToModelMapper<boolean[]> toBoolArray = ArrayValue::boolArray;

    public static <T> ArrayToModelMapper<Collection<T>> toCollection(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsList(elementMapper));
    }

    public static <T> ArrayToModelMapper<T[]> toArray(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> cast(notNull.mapAsList(elementMapper).toArray()));
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


    public ArrayFromModelMapper<int[]> fromIntArray = ArrayValueFactory::intArray;
    public ArrayFromModelMapper<long[]> fromLongArray = ArrayValueFactory::longArray;
    public ArrayFromModelMapper<short[]> fromShortArray = ArrayValueFactory::shortArray;
    public ArrayFromModelMapper<double[]> fromDoubleArray = ArrayValueFactory::doubleArray;
    public ArrayFromModelMapper<float[]> fromFloatArray = ArrayValueFactory::floatArray;
    public ArrayFromModelMapper<byte[]> fromByteArray = ArrayValueFactory::byteArray;
    public ArrayFromModelMapper<char[]> fromCharArray = ArrayValueFactory::charArray;
    public ArrayFromModelMapper<boolean[]> fromBoolArray = ArrayValueFactory::boolArray;

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
