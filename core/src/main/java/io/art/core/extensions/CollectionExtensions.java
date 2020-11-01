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

package io.art.core.extensions;

import lombok.experimental.*;
import static io.art.core.factory.CollectionsFactory.*;
import static java.util.Collections.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public final class CollectionExtensions {
    public static <T, R> List<R> orEmptyList(T value, Predicate<T> condition, Function<T, List<R>> action) {
        return condition.test(value) ? action.apply(value) : emptyList();
    }

    public static boolean areAllUnique(Collection<?> collection) {
        return collection.stream().allMatch(new HashSet<>()::add);
    }

    public static <T> List<T> addFirstToList(T element, Collection<T> source) {
        List<T> list = dynamicArrayOf(element);
        list.add(element);
        list.addAll(source);
        return list;
    }

    public static <T> List<T> addLastToList(Collection<T> source, T element) {
        List<T> list = dynamicArrayOf(element);
        list.addAll(source);
        list.add(element);
        return list;
    }

    public static <T> List<T> combine(List<T> first, Collection<T> second) {
        List<T> list = dynamicArrayOf();
        list.addAll(first);
        list.addAll(second);
        return list;
    }

    public static <T> Set<T> combine(Set<T> first, Collection<T> second) {
        Set<T> set = setOf();
        set.addAll(first);
        set.addAll(second);
        return set;
    }
}
