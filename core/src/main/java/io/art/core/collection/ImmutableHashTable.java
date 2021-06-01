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

import io.art.core.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static java.lang.Math.*;
import static java.lang.Runtime.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class ImmutableHashTable<K, V> {
    private final Function<K, Integer> hasher;
    private Bucket<K, V>[] buckets;

    private ImmutableHashTable(Function<K, Integer> hasher, Pair<K, V>[] elements) {
        this.hasher = hasher;
        buckets = cast(new Bucket[elements.length]);
        for (Pair<K, V> element : elements) {
            K key = element.getFirst();
            V value = element.getSecond();
            Integer hash = hasher.apply(key);
            int index = abs(hash % buckets.length);
            if (isNull(buckets[index])) {
                buckets[index] = new Bucket<>(elements.length - 1, new Entry<>(key, value));
                continue;
            }
            buckets[index].add(hash, new Entry<>(key, value));
        }
    }

    public V get(K key) {
        Integer hash = hasher.apply(key);
        int index = abs(hash % buckets.length);
        if (isNull(buckets[index])) {
            return null;
        }
        return buckets[index].get(hash, key);
    }

    public void clear() {
        buckets = null;
        getRuntime().gc();
    }

    private static class Bucket<K, V> {
        private final int size;
        private final Entry<K, V> entry;
        private final Bucket<K, V>[] buckets;

        private Bucket(int size, Entry<K, V> entry) {
            this.size = size;
            this.entry = entry;
            buckets = cast(new Bucket[size]);
        }

        private void add(int hash, Entry<K, V> entry) {
            int next = abs(hash % buckets.length);
            if (isNull(buckets[next])) {
                buckets[next] = new Bucket<>(size - 1, entry);
                return;
            }
            buckets[next].add(hash, entry);
        }

        public V get(int hash, K key) {
            if (key.equals(entry.key)) {
                return entry.value;
            }
            int next = abs(hash % buckets.length);
            if (isNull(buckets[next])) return null;
            return buckets[next].get(hash, key);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Entry<EntryKey, EntryValue> implements Map.Entry<EntryKey, EntryValue> {
        private final EntryKey key;
        private EntryValue value;

        @Override
        public EntryValue setValue(EntryValue value) {
            this.value = value;
            return value;
        }
    }

    public static <K, V> ImmutableHashTable<K, V> immutableHashTable(Function<K, Integer> hash, Pair<K, V>[] elements) {
        return new ImmutableHashTable<>(hash, elements);
    }
}
