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
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class ImmutableHashTable<K, V> {
    private final int size;
    private final Function<K, Integer> hash;
    private final Entry<K, V>[] array;

    private ImmutableHashTable(Function<K, Integer> hash, Pair<K, V>[] elements) {
        this.size = elements.length;
        this.hash = hash;
        array = cast(new Entry[size]);
        for (int index = 0; index < size; index++) {
            Pair<K, V> element = elements[index];
            K key = element.getFirst();
            V value = element.getSecond();
            int base = abs(hash.apply(key) % array.length);
            int searcher = base;
            while (searcher >= 0 && nonNull(array[searcher])) {
                --searcher;
            }
            if (searcher < 0) {
                searcher = base;
                while (searcher < array.length && nonNull(array[searcher])) {
                    ++searcher;
                }
                array[searcher] = new Entry<>(key, value);
                return;
            }
            array[searcher] = new Entry<>(key, value);
        }
    }

    public V get(K key) {
        int base = abs(hash.apply(key) % array.length);
        int searcher = base;
        while (searcher >= 0 && !key.equals(array[searcher].getKey())) {
            --searcher;
        }
        if (searcher < 0) {
            searcher = base;
            while (searcher < array.length && !key.equals(array[searcher].getKey())) {
                ++searcher;
            }
            return array[searcher].getValue();
        }
        return array[searcher].getValue();
    }

    public int size() {
        return size;
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
