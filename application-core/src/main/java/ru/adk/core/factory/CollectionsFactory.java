package ru.adk.core.factory;


import lombok.NoArgsConstructor;
import ru.adk.core.model.Pair;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

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
