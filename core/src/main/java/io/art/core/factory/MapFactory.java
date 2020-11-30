package io.art.core.factory;

import io.art.core.collection.*;
import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import java.util.*;
import java.util.concurrent.*;

@UtilityClass
public class MapFactory {
    public static <K, V> Map<K, V> map() {
        return new LinkedHashMap<>();
    }

    public static <K, V> Map<K, V> mapOf(K key, V value) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>();
        map.put(key, value);
        return map;
    }

    public static <K, V> Map<K, V> mapOf(Map<K, V> map) {
        if (isEmpty(map)) return new LinkedHashMap<>();
        return new LinkedHashMap<>(map);
    }

    public static <K, V> Map<K, V> mapOf(int size) {
        return new LinkedHashMap<>(size);
    }


    public static <K, V> ImmutableMap<K, V> immutableMapOf(Map<K, V> map) {
        return new ImmutableMap<>(map);
    }


    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMapOf(K key, V value) {
        return new ConcurrentHashMap<>(mapOf(key, value));
    }

    public static <K, V> ConcurrentHashMap<K, V> concurrentHashMapOf(Map<K, V> map) {
        if (isEmpty(map)) return concurrentHashMap();
        return new ConcurrentHashMap<>(map);
    }
}
