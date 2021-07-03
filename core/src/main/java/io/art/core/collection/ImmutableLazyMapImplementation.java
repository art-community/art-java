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

import io.art.core.property.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.property.LazyProperty.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import javax.annotation.*;
import java.util.*;
import java.util.function.*;

public class ImmutableLazyMapImplementation<K, V> implements ImmutableMap<K, V> {
    private final Set<K> keys;
    private final Function<K, V> provider;
    private final LazyProperty<ImmutableMap<K, V>> evaluated;

    private final static ImmutableLazyMapImplementation<?, ?> EMPTY = new ImmutableLazyMapImplementation<>(emptySet(), key -> null);

    public ImmutableLazyMapImplementation(Set<K> keys, Function<K, V> provider) {
        this.keys = keys;
        this.provider = provider;
        this.evaluated = lazy(this::collect);
    }

    private ImmutableMap<K, V> collect() {
        Builder<K, V> builder = immutableMapBuilder(size());
        for (K key : keys) {
            let(provider.apply(key), notNull -> builder.put(key, notNull));
        }
        return builder.build();
    }

    @Override
    public int size() {
        return keys.size();
    }

    @Override
    public boolean isEmpty() {
        return keys.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (isNull(key)) {
            return false;
        }
        return keys.contains(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (isNull(value)) {
            return false;
        }
        return evaluated.get().containsValue(value);
    }

    @Override
    public V get(Object key) {
        if (isNull(key)) {
            return null;
        }
        return provider.apply(cast(key));
    }

    @Override
    @Nonnull
    public Set<K> keySet() {
        return keys;
    }

    @Override
    @Nonnull
    public Collection<V> values() {
        return evaluated.get().values();
    }

    @Override
    @Nonnull
    public Set<Map.Entry<K, V>> entrySet() {
        return evaluated.get().entrySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return orElse(get(key), defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        entrySet().forEach(entry -> action.accept(entry.getKey(), entry.getValue()));
    }

    @Override
    public Map<K, V> toMutable() {
        return evaluated.get().toMutable();
    }

    @Override
    public int hashCode() {
        return collect().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (isNull(other)) return false;
        if (!(other instanceof ImmutableLazyMapImplementation)) return false;
        ImmutableLazyMapImplementation<?, ?> otherMap = (ImmutableLazyMapImplementation<?, ?>) other;
        if (size() != otherMap.size()) return false;
        for (K key : keys) {
            if (!Objects.equals(provider.apply(key), otherMap.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static ImmutableLazyMapImplementation<?, ?> emptyImmutableLazyMap() {
        return EMPTY;
    }
}
