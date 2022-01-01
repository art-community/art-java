package io.art.core.builder;

import io.art.core.factory.*;
import lombok.*;
import lombok.experimental.Delegate;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public class MapBuilder<K, V> implements Map<K, V> {
    @Delegate
    private Map<K, V> map = MapFactory.map();

    private MapBuilder(Map<K, V> map) {
        this.map = MapFactory.mapOf(map);
    }

    private MapBuilder(int size) {
        this.map = MapFactory.mapOf(size);
    }

    public MapBuilder<K, V> with(K key, V value) {
        map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> withAll(Map<K, V> map) {
        this.map.putAll(map);
        return this;
    }

    public Map<K, V> build() {
        return map;
    }

    public static <K, V> MapBuilder<K, V> mapBuilder() {
        return new MapBuilder<>();
    }

    public static <K, V> MapBuilder<K, V> mapBuilder(K key, V value) {
        return new MapBuilder<K, V>().with(key, value);
    }

    public static <K, V> MapBuilder<K, V> mapBuilder(int size, K key, V value) {
        return new MapBuilder<K, V>(size).with(key, value);
    }
}
