package io.art.core.builder;

import lombok.*;
import lombok.experimental.Delegate;
import static lombok.AccessLevel.*;
import java.util.*;

@NoArgsConstructor(access = PRIVATE)
public class MapBuilder<K, V> implements Map<K, V> {
    @Delegate
    private LinkedHashMap<K, V> map = new LinkedHashMap<>();

    public MapBuilder(Map<K, V> map) {
        this.map = new LinkedHashMap<>(map);
    }

    public MapBuilder(int size) {
        this.map = new LinkedHashMap<>(size);
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
}
