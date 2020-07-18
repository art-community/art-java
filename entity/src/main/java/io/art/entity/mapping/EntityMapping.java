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

package io.art.entity.mapping;

import io.art.entity.immutable.*;
import io.art.entity.mapper.*;
import lombok.experimental.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.entity.factory.ArrayFactory.*;
import java.util.*;

@UtilityClass
public class EntityMapping {
    public static <T> ValueToModelMapper<List<T>, ArrayValue> toList(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsList(elementMapper));
    }

    public static <T> ValueToModelMapper<Set<T>, ArrayValue> toSet(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsSet(elementMapper));
    }

    public static <T> ValueToModelMapper<Queue<T>, ArrayValue> toQueue(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsQueue(elementMapper));
    }

    public static <T> ValueToModelMapper<Deque<T>, ArrayValue> toDeque(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapAsDeque(elementMapper));
    }


    public static <T> ValueToModelMapper<List<T>, ArrayValue> toMutableList(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToList(elementMapper));
    }

    public static <T> ValueToModelMapper<Set<T>, ArrayValue> toMutableSet(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToSet(elementMapper));
    }

    public static <T> ValueToModelMapper<Queue<T>, ArrayValue> toMutableQueue(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToQueue(elementMapper));
    }

    public static <T> ValueToModelMapper<Deque<T>, ArrayValue> toMutableDeque(ValueToModelMapper<T, ? extends Value> elementMapper) {
        return array -> let(array, notNull -> notNull.mapToDeque(elementMapper));
    }


    public static <T> ValueFromModelMapper<List<T>, ArrayValue> fromList(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return list -> let(list, notNull -> array(list, elementMapper));
    }

    public static <T> ValueFromModelMapper<Set<T>, ArrayValue> fromSet(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNull -> array(collection, elementMapper));
    }

    public static <T> ValueFromModelMapper<Queue<T>, ArrayValue> fromQueue(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNull -> array(collection, elementMapper));
    }

    public static <T> ValueFromModelMapper<Deque<T>, ArrayValue> fromDeque(ValueFromModelMapper<T, ? extends Value> elementMapper) {
        return collection -> let(collection, notNull -> array(collection, elementMapper));
    }
}
