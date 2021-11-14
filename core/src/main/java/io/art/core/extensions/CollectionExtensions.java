/*
 * ART
 *
 * Copyright 2019-2021 ART
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

import io.art.core.collection.*;
import io.art.core.exception.*;
import lombok.experimental.*;
import static io.art.core.collector.SetCollector.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static java.lang.System.*;
import static java.util.Arrays.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public final class CollectionExtensions {
    public static <T, R> List<R> orEmptyList(T value, Predicate<T> condition, Function<T, List<R>> action) {
        return condition.test(value) ? action.apply(value) : emptyList();
    }


    public static <T, R> ImmutableArray<R> orEmptyImmutableArray(T value, Predicate<T> condition, Function<T, ImmutableArray<R>> action) {
        return condition.test(value) ? immutableArrayOf(action.apply(value)) : ImmutableArray.emptyImmutableArray();
    }

    public static <T, R> ImmutableArray<R> orEmptyImmutableArray(T value, Function<T, ImmutableArray<R>> action) {
        return nonNull(value) ? immutableArrayOf(action.apply(value)) : ImmutableArray.emptyImmutableArray();
    }

    public static <K, V, R> ImmutableMap<K, V> orEmptyImmutableMap(R value, Function<R, ImmutableMap<K, V>> action) {
        return nonNull(value) ? immutableMapOf(action.apply(value)) : ImmutableMap.emptyImmutableMap();
    }


    public static boolean areAllUnique(Collection<?> collection) {
        return duplicates(collection, identity()).isEmpty();
    }


    public static <T> boolean hasDuplicates(Collection<T> collection, Function<T, Object> keyExtractor) {
        return !duplicates(collection, keyExtractor).isEmpty();
    }

    public static <T> boolean hasDuplicates(T[] array, Function<T, Object> keyExtractor) {
        return !duplicates(array, keyExtractor).isEmpty();
    }


    public static <T, K> Set<K> duplicates(T[] array, Function<T, K> keyExtractor) {
        return duplicates(fixedArrayOf(array), keyExtractor);
    }

    public static <T, K> Set<K> duplicates(Collection<T> array, Function<T, K> keyExtractor) {
        Map<K, List<T>> collect = array.stream().collect(groupingBy(keyExtractor));
        return collect.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .collect(setCollector());
    }


    public static <T> List<T> addFirstToList(T element, Collection<T> source) {
        List<T> list = dynamicArrayOf(element);
        list.addAll(source);
        return list;
    }

    public static <T> List<T> addLastToList(Collection<T> source, T element) {
        List<T> list = dynamicArrayOf(source);
        list.add(element);
        return list;
    }


    public static <T> Set<T> addFirstToSet(T element, Collection<T> source) {
        Set<T> set = setOf(element);
        set.addAll(source);
        return set;
    }

    public static <T> Set<T> addLastToSet(Collection<T> source, T element) {
        Set<T> set = setOf(source);
        set.add(element);
        return set;
    }


    @SafeVarargs
    public static <T> Set<T> addToSet(Set<T> source, T... elements) {
        Set<T> set = setOf(elements);
        set.addAll(source);
        return set;
    }

    @SafeVarargs
    public static <T> List<T> addToList(List<T> source, T... elements) {
        List<T> list = dynamicArrayOf(elements);
        list.addAll(source);
        return list;
    }


    @SafeVarargs
    public static <T> List<T> combineToList(Collection<T>... collections) {
        List<T> list = dynamicArrayOf();
        stream(collections).forEach(list::addAll);
        return list;
    }

    @SafeVarargs
    public static <T> Set<T> combineToSet(Collection<T>... collections) {
        Set<T> set = setOf();
        stream(collections).forEach(set::addAll);
        return set;
    }


    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Supplier<V> value) {
        return map.computeIfAbsent(key, ignore -> value.get());
    }

    public static <K, V> V putIfAbsent(Map<K, V> map, K key, Supplier<V> value) {
        V current = map.get(key);
        if (nonNull(current)) return current;
        map.put(key, current = value.get());
        return current;
    }


    public static <V> V computeIfAbsent(Set<V> set, V value) {
        if (set.contains(value)) {
            return set.stream()
                    .filter(value::equals)
                    .findFirst()
                    .orElseThrow(ImpossibleSituationException::new);
        }
        set.add(value);
        return value;
    }


    public static <T> void erase(Queue<T> queue, Consumer<T> elementConsumer) {
        T element;
        while (nonNull(element = queue.poll())) {
            elementConsumer.accept(element);
        }
    }

    public static <T> void erase(List<T> list, Consumer<T> elementConsumer) {
        for (T element : list) {
            elementConsumer.accept(element);
        }
        list.clear();
    }

    public static <K, V> ImmutableMap<K, V> merge(ImmutableMap<K, V> first, ImmutableMap<K, V> second) {
        if (isNull(first)) return second;
        if (isNull(second)) return first;
        Map<K, V> firstMutable = first.toMutable();
        second.forEach(firstMutable::put);
        return immutableMapOf(firstMutable);
    }

    public static <T> ImmutableArray<T> merge(ImmutableArray<T> first, ImmutableArray<T> second) {
        if (isNull(first)) return second;
        if (isNull(second)) return first;
        List<T> firstMutable = first.toMutable();
        second.forEach(firstMutable::add);
        return immutableArrayOf(firstMutable);
    }

    public static <T> ImmutableSet<T> merge(ImmutableSet<T> first, ImmutableSet<T> second) {
        if (isNull(first)) return second;
        if (isNull(second)) return first;
        Set<T> firstMutable = first.toMutable();
        second.forEach(firstMutable::add);
        return immutableSetOf(firstMutable);
    }

    public <T> T[] skip(int count, T[] array) {
        arraycopy(array, count, array, 0, array.length - 1);
        array = Arrays.copyOf(array, array.length - 1);
        return array;
    }
}
