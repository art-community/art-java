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

import io.art.value.immutable.*;
import io.art.value.mapper.*;
import io.art.value.mapper.ValueFromModelMapper.*;
import io.art.value.mapper.ValueToModelMapper.*;
import lombok.experimental.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.value.factory.ArrayFactory.*;
import java.util.*;

@UtilityClass
public class ArrayMapping {
    public static <T> ArrayToModelMapper<Collection<T>> toCollection(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsList(elementMapper));
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


    public static <T> ArrayFromModelMapper<Collection<T>> fromCollection(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return list -> let(list, notNull -> array(list, elementMapper));
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
