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

package io.art.rocks.db.storage;

import lombok.experimental.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.*;
import static io.art.rocks.db.storage.RocksDbPrimitiveStorage.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class RocksDbCollectionStorage {
    public <T> void add(String key, Collection<T> value, Function<T, byte[]> mapper) {
        if (isEmpty(mapper)) return;
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (T element : value) {
            RocksDbPrimitiveStorage.put(key, element, mapper);
        }
    }

    public void addStrings(String key, Collection<String> value) {
        if (isEmpty(key)) return;
        for (String element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void addInts(String key, Collection<Integer> value) {
        if (isEmpty(key)) return;
        for (Integer element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void addDoubles(String key, Collection<Double> value) {
        if (isEmpty(key)) return;
        for (Double element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void addLongs(String key, Collection<Long> value) {
        if (isEmpty(key)) return;
        for (Long element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void addChars(String key, Collection<Character> value) {
        if (isEmpty(key)) return;
        for (Character element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void addBools(String key, Collection<Boolean> value) {
        if (isEmpty(key)) return;
        for (Boolean element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void addBytes(String key, Collection<Byte> value) {
        if (isEmpty(key)) return;
        for (Byte element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }


    public <T> void put(String key, Collection<T> value, Function<T, byte[]> mapper) {
        if (isEmpty(mapper)) return;
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (T element : value) {
            RocksDbPrimitiveStorage.put(key, element, mapper);
        }
    }

    public void putStrings(String key, Collection<String> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (String element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void putInts(String key, Collection<Integer> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Integer element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void putDoubles(String key, Collection<Double> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Double element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void putLongs(String key, Collection<Long> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Long element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void putChars(String key, Collection<Character> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Character element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void putBools(String key, Collection<Boolean> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Boolean element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void putBytes(String key, Collection<Byte> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Byte element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }


    public void put(String key, int index, Byte value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        List<Character> charList = getCharList(key);
        charList.set(index, value.toString().charAt(0));
        putChars(key, charList);
    }

    public void put(String key, int index, Character value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        List<Character> charList = getCharList(key);
        charList.set(index, value);
        putChars(key, charList);
    }

    public void put(String key, int index, Object value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        List<String> list = getStringList(key);
        list.set(index, value.toString());
        putStrings(key, list);
    }


    public <T> List<T> getList(String key, Function<String, T> mapper) {
        if (isEmpty(mapper)) return emptyList();
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        List<T> values = dynamicArrayOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(mapper.apply(element));
        }
        return values;
    }

    public List<String> getStringList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        return fixedArrayOf(elements.split(ROCKS_DB_LIST_DELIMITER));
    }

    public List<Integer> getIntList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        List<Integer> values = dynamicArrayOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(Integer.valueOf(element));
        }
        return values;
    }

    public List<Double> getDoubleList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        List<Double> values = dynamicArrayOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(Double.valueOf(element));
        }
        return values;
    }

    public List<Character> getCharList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        List<Character> values = dynamicArrayOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            if (isEmpty(element)) continue;
            values.add(element.charAt(0));
        }
        return values;
    }

    public List<Long> getLongList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        List<Long> values = dynamicArrayOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            if (isEmpty(element)) continue;
            values.add(Long.valueOf(element));
        }
        return values;
    }

    public List<Boolean> getBoolList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        List<Boolean> values = dynamicArrayOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(TRUE_NUMERIC.equals(element));
        }
        return values;
    }

    public void removeAt(String collectionKey, int index) {
        List<String> values = getStringList(collectionKey);
        values.remove(index);
        putStrings(collectionKey, values);
    }

    public void removeStringElement(String collectionKey, String element) {
        Set<String> values = getStringSet(collectionKey);
        values.remove(element);
        putStrings(collectionKey, values);
    }

    public void removeIntElement(String collectionKey, Integer element) {
        Set<Integer> values = getIntSet(collectionKey);
        values.remove(element);
        putInts(collectionKey, values);
    }

    public void removeDoubleElement(String collectionKey, Double element) {
        Set<Double> values = getDoubleSet(collectionKey);
        values.remove(element);
        putDoubles(collectionKey, values);
    }

    public void removeCharElement(String collectionKey, Character element) {
        Set<Character> values = getCharSet(collectionKey);
        values.remove(element);
        putChars(collectionKey, values);
    }

    public void removeBoolElement(String collectionKey, Boolean element) {
        Set<Boolean> values = getBoolSet(collectionKey);
        values.remove(element);
        putBools(collectionKey, values);
    }

    public void removeLongElement(String collectionKey, Long element) {
        Set<Long> values = getLongSet(collectionKey);
        values.remove(element);
        putLongs(collectionKey, values);
    }


    public <T> Set<T> getSet(String key, Function<String, T> mapper) {
        if (isEmpty(mapper)) return emptySet();
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        Set<T> values = setOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(mapper.apply(element));
        }
        return values;
    }

    public Set<String> getStringSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        return setOf(elements.split(ROCKS_DB_LIST_DELIMITER));
    }

    public Set<Integer> getIntSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        Set<Integer> values = setOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(Integer.valueOf(element));
        }
        return values;
    }

    public Set<Double> getDoubleSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        Set<Double> values = setOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(Double.valueOf(element));
        }
        return values;
    }

    public Set<Character> getCharSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        Set<Character> values = setOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            if (isEmpty(element)) continue;
            values.add(element.charAt(0));
        }
        return values;
    }

    public Set<Long> getLongSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        Set<Long> values = setOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            if (isEmpty(element)) continue;
            values.add(Long.valueOf(element));
        }
        return values;
    }

    public Set<Boolean> getBoolSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        Set<Boolean> values = setOf();
        for (String element : elements.split(ROCKS_DB_LIST_DELIMITER)) {
            values.add(TRUE_NUMERIC.equals(element));
        }
        return values;

    }
}
