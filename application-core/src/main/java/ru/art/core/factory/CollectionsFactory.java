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

package ru.art.core.factory;


import lombok.*;
import ru.art.core.model.*;
import java.util.ArrayList;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

import static java.util.Arrays.*;
import static java.util.Collections.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.checker.CheckerForEmptiness.*;

public interface CollectionsFactory {
    static <K, V> MapBuilder<K, V> mapOf(K k, V v) {
        return cast(new MapBuilder<>().add(k, v));
    }

    static <K, V> MapBuilder<K, V> mapOf() {
        return new MapBuilder<>();
    }

    static <K, V> MapBuilder<K, V> mapOf(Map<K, V> map) {
        if (isEmpty(map)) return mapOf();
        return new MapBuilder<>(map);
    }

    static <K, V> MapBuilder<K, V> mapOf(int size) {
        return new MapBuilder<>(size);
    }


    static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    static <K, V> ConcurrentHashMap<K, V> concurrentHashMap(Map<K, V> map) {
        if (isEmpty(map)) return concurrentHashMap();
        return new ConcurrentHashMap<>(map);
    }


    static <T> List<T> fixedArrayOf(Collection<T> elements) {
        return isEmpty(elements) ? emptyList() : new ArrayList<>(elements);
    }

    @SafeVarargs
    static <T> List<T> fixedArrayOf(T... elements) {
        return isEmpty(elements) ? emptyList() : asList(elements);
    }

    static <T> List<T> arrayOf(int size) {
        return size <= 0 ? new ArrayList<>() : new ArrayList<>(size);
    }

    static <T> List<T> fixedArrayOf(Stream<T> stream) {
        return isEmpty(stream) ? emptyList() : asList(cast(stream.toArray()));
    }

    @SafeVarargs
    static <T> List<T> dynamicArrayOf(T... elements) {
        return isEmpty(elements) ? new ArrayList<>() : new ArrayList<>(asList(elements));
    }

    static <T> List<T> dynamicArrayOf(Collection<T> elements) {
        return isEmpty(elements) ? new ArrayList<>() : new ArrayList<>(elements);
    }

    @SafeVarargs
    static <T> List<T> linkedListOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }

    static <T> List<T> linkedListOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements);
    }

    static <T> Deque<T> dequeOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(elements);
    }

    @SafeVarargs
    static <T> Deque<T> dequeOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }

    @SafeVarargs
    static <T> Queue<T> queueOf(T... elements) {
        return isEmpty(elements) ? new LinkedList<>() : new LinkedList<>(asList(elements));
    }

    @SafeVarargs
    static <T> PriorityQueue<T> priorityQueueOf(Comparator<T> comparator, T... elements) {
        PriorityQueue<T> queue = new PriorityQueue<>(comparator);
        queue.addAll(asList(elements));
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }

    static <T> PriorityQueue<T> priorityQueueOf(Comparator<T> comparator, Collection<T> elements) {
        PriorityQueue<T> queue = new PriorityQueue<>(comparator);
        queue.addAll(elements);
        return isEmpty(elements) ? new PriorityQueue<>(comparator) : queue;
    }

    @SafeVarargs
    static <T> Stack<T> stackOf(T... elements) {
        if (isEmpty(elements)) return new Stack<>();
        Stack<T> stack = new Stack<>();
        stack.addAll(asList(elements));
        return stack;
    }

    @SafeVarargs
    static <T> Set<T> setOf(T... elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(asList(elements));
    }

    static <T> Set<T> setOf(Collection<T> elements) {
        return isEmpty(elements) ? new LinkedHashSet<>() : new LinkedHashSet<>(elements);
    }

    @SafeVarargs
    static <T> Set<T> treeOf(Comparator<T> comparator, T... elements) {
        TreeSet<T> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        treeSet.addAll(asList(elements));
        return treeSet;
    }

    @SafeVarargs
    static <T extends Comparable<T>> Set<T> treeOf(T... elements) {
        TreeSet<T> treeSet = new TreeSet<>();
        if (isEmpty(elements)) return treeSet;
        treeSet.addAll(asList(elements));
        return treeSet;
    }

    static <T> Set<T> treeOf(Collection<T> elements, Comparator<T> comparator) {
        TreeSet<T> treeSet = new TreeSet<>(comparator);
        if (isEmpty(elements)) return treeSet;
        treeSet.addAll(elements);
        return treeSet;
    }

    static <K, V> Pair<K, V> pairOf(K k, V v) {
        return new Pair<>(k, v);
    }

    @NoArgsConstructor
    class MapBuilder<K, V> extends LinkedHashMap<K, V> implements Map<K, V> {
        MapBuilder(Map<K, V> map) {
            super(map);
        }

        MapBuilder(int size) {
            super(size);
        }

        public MapBuilder<K, V> add(K key, V value) {
            put(key, value);
            return this;
        }
    }
}
