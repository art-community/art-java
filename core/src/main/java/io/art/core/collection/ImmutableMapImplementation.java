/*
 * ART
 *
 * Copyright 2019-2022 ART
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

import static java.util.Objects.*;
import java.util.*;

public class ImmutableMapImplementation<K, V> implements ImmutableMap<K, V> {
    private final Map<K, V> map;

    public ImmutableMapImplementation(Map<K, V> map) {
        this.map = com.google.common.collect.ImmutableMap.copyOf(map);
    }

    public ImmutableMapImplementation(ImmutableMap<K, V> map) {
        this.map = com.google.common.collect.ImmutableMap.copyOf(map.entrySet());
    }

    private ImmutableMapImplementation(com.google.common.collect.ImmutableMap<K, V> map) {
        this.map = map;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Map<K, V> toMutable() {
        return new LinkedHashMap<>(map);
    }

    @Override
    public boolean equals(Object object) {
        if (isNull(object)) return false;
        if (object == this) return true;
        if (!(object instanceof ImmutableMapImplementation)) return false;
        return map.equals(((ImmutableMapImplementation<?, ?>) object).map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
