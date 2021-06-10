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

package io.art.core.collection;

import static io.art.core.caster.Caster.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface ImmutableMap<K, V> {
    ImmutableMap<?, ?> EMPTY = new ImmutableMapImplementation<>(emptyMap());

    int size();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    Set<K> keySet();

    Collection<V> values();

    Set<Map.Entry<K, V>> entrySet();

    Map<K, V> toMutable();


    default boolean isEmpty() {
        return size() == 0;
    }

    default V getOrDefault(Object key, V defaultValue) {
        V value;
        return ((nonNull(value = get(key))) || containsKey(key))
                ? value
                : defaultValue;
    }

    default void forEach(BiConsumer<? super K, ? super V> action) {
        entrySet().forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }


    static <K, V> ImmutableMap<K, V> emptyImmutableMap() {
        return cast(EMPTY);
    }

    static <K, V> Builder<K, V> immutableMapBuilder() {
        return new Builder<>();
    }

    static Builder<String, String> immutableMapBuilder(int size) {
        return new Builder<>(size);
    }

    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> immutableMapCollector(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableMap.Builder<K, V>::new,
                (builder, input) -> builder.put(keyFunction.apply(input), valueFunction.apply(input)),
                ImmutableMap.Builder::combine,
                ImmutableMap.Builder::build
        );
    }

    static <T, K, V> Collector<T, ?, ImmutableMap<K, V>> immutableMapCollector(
            Function<? super T, ? extends K> keyFunction,
            Function<? super T, ? extends V> valueFunction,
            BinaryOperator<V> mergeFunction) {
        Collector<T, ?, LinkedHashMap<K, V>> mapCollector = toMap(keyFunction, valueFunction, mergeFunction, LinkedHashMap::new);
        Function<LinkedHashMap<K, V>, ImmutableMap<K, V>> mapFunction = ImmutableMapImplementation::new;
        return collectingAndThen(mapCollector, mapFunction);
    }


    class Builder<K, V> {
        private final com.google.common.collect.ImmutableMap.Builder<K, V> builder;

        public Builder() {
            builder = com.google.common.collect.ImmutableMap.builder();
        }

        @SuppressWarnings(UNSTABLE_API_USAGE)
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

        @SuppressWarnings(UNSTABLE_API_USAGE)
        public Builder<K, V> putAll(Iterable<? extends Map.Entry<? extends K, ? extends V>> entries) {
            builder.putAll(entries);
            return this;
        }

        @SuppressWarnings(UNSTABLE_API_USAGE)
        private Builder<K, V> combine(Builder<K, V> builder) {
            this.builder.putAll(builder.build().entrySet());
            return this;
        }

        public ImmutableMap<K, V> build() {
            return new ImmutableMapImplementation<>(builder.build());
        }
    }
}
