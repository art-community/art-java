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

package io.art.core.collection;

import static com.google.common.base.Preconditions.*;
import static io.art.core.caster.Caster.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ImmutableMap<K, V> {
    private final Map<K, V> map;

    private static final ImmutableMap<?, ?> EMPTY = new ImmutableMap<>(emptyMap());

    public ImmutableMap(Map<K, V> map) {
        this.map = com.google.common.collect.ImmutableMap.copyOf(map);
    }

    private ImmutableMap(com.google.common.collect.ImmutableMap<K, V> map) {
        this.map = map;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public V get(Object key) {
        return map.get(key);
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    public V getOrDefault(Object key, V defaultValue) {
        return map.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        map.forEach(action);
    }

    public Map<K, V> toMutable() {
        return new LinkedHashMap<>(map);
    }

    @Override
    public boolean equals(Object object) {
        return map.equals(object);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    public static <K, V> ImmutableMap<K, V> emptyImmutableMap() {
        return cast(EMPTY);
    }

    public static <K, V> Builder<K, V> immutableMapBuilder() {
        return new Builder<>();
    }

    public static Builder<String, String> immutableMapBuilder(int size) {
        return new Builder<>(size);
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> immutableMapCollector(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableMap.Builder<K, V>::new,
                (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)),
                ImmutableMap.Builder::combine,
                ImmutableMap.Builder::build
        );
    }

    public static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> immutableMapCollector(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction,
            BinaryOperator<V> mergeFunction) {
        checkNotNull(keyFunction);
        checkNotNull(valueFunction);
        checkNotNull(mergeFunction);
        Collector<T, ?, LinkedHashMap<K, V>> mapCollector = toMap(keyFunction, valueFunction, mergeFunction, LinkedHashMap::new);
        Function<LinkedHashMap<K, V>, ImmutableMap<K, V>> mapFunction = ImmutableMap::new;
        return collectingAndThen(mapCollector, mapFunction);
    }


    public static class Builder<K, V> {
        private final com.google.common.collect.ImmutableMap.Builder<K, V> builder;

        public Builder() {
            builder = com.google.common.collect.ImmutableMap.builder();
        }

        public Builder(int size) {
            builder = com.google.common.collect.ImmutableMap.builderWithExpectedSize(size);
        }

        public Builder<K, V> put(K key, V value) {
            builder.put(key, value);
            return this;
        }

        public Builder<K, V> put(Map.Entry<? extends K, ? extends V> entry) {
            builder.put(entry);
            return this;
        }

        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            builder.putAll(map);
            return this;
        }

        public Builder<K, V> putAll(ImmutableMap<? extends K, ? extends V> map) {
            map.forEach(builder::put);
            return this;
        }

        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            builder.putAll(entries);
            return this;
        }

        private Builder<K, V> combine(Builder<K, V> builder) {
            this.builder.putAll(builder.build().map);
            return this;
        }

        public ImmutableMap<K, V> build() {
            return new ImmutableMap<>(builder.build());
        }
    }
}
