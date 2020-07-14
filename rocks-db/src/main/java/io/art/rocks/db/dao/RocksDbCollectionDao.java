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

package io.art.rocks.db.dao;

import static java.util.Collections.*;
import static java.util.Objects.*;
import static io.art.core.checker.CheckerForEmptiness.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.*;
import static io.art.rocks.db.dao.RocksDbPrimitiveDao.*;
import java.util.*;
import java.util.function.*;

@SuppressWarnings("Duplicates")
public interface RocksDbCollectionDao {
    static <T> void add(String key, Collection<T> value, Function<T, byte[]> mapper) {
        if (isEmpty(mapper)) return;
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (T element : value) {
            RocksDbPrimitiveDao.put(key, element, mapper);
        }
    }

    static void addStrings(String key, Collection<String> value) {
        if (isEmpty(key)) return;
        for (String element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void addInts(String key, Collection<Integer> value) {
        if (isEmpty(key)) return;
        for (Integer element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void addDoubles(String key, Collection<Double> value) {
        if (isEmpty(key)) return;
        for (Double element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void addLongs(String key, Collection<Long> value) {
        if (isEmpty(key)) return;
        for (Long element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void addChars(String key, Collection<Character> value) {
        if (isEmpty(key)) return;
        for (Character element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void addBools(String key, Collection<Boolean> value) {
        if (isEmpty(key)) return;
        for (Boolean element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void addBytes(String key, Collection<Byte> value) {
        if (isEmpty(key)) return;
        for (Byte element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }


    static <T> void put(String key, Collection<T> value, Function<T, byte[]> mapper) {
        if (isEmpty(mapper)) return;
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (T element : value) {
            RocksDbPrimitiveDao.put(key, element, mapper);
        }
    }

    static void putStrings(String key, Collection<String> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (String element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void putInts(String key, Collection<Integer> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Integer element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void putDoubles(String key, Collection<Double> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Double element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void putLongs(String key, Collection<Long> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Long element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void putChars(String key, Collection<Character> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Character element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void putBools(String key, Collection<Boolean> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Boolean element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void putBytes(String key, Collection<Byte> value) {
        if (isEmpty(key)) return;
        delete(key);
        for (Byte element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }


    static void put(String key, int index, Byte value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        List<Character> charList = getCharList(key);
        charList.set(index, value.toString().charAt(0));
        putChars(key, charList);
    }

    static void put(String key, int index, Character value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        List<Character> charList = getCharList(key);
        charList.set(index, value);
        putChars(key, charList);
    }

    static void put(String key, int index, Object value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        List<String> list = getStringList(key);
        list.set(index, value.toString());
        putStrings(key, list);
    }


    static <T> List<T> getList(String key, Function<String, T> mapper) {
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

    static List<String> getStringList(String key) {
        if (isEmpty(key)) return emptyList();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptyList();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptyList();
        return fixedArrayOf(elements.split(ROCKS_DB_LIST_DELIMITER));
    }

    static List<Integer> getIntList(String key) {
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

    static List<Double> getDoubleList(String key) {
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

    static List<Character> getCharList(String key) {
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

    static List<Long> getLongList(String key) {
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

    static List<Boolean> getBoolList(String key) {
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

    static void removeAt(String collectionKey, int index) {
        List<String> values = getStringList(collectionKey);
        values.remove(index);
        putStrings(collectionKey, values);
    }

    static void removeStringElement(String collectionKey, String element) {
        Set<String> values = getStringSet(collectionKey);
        values.remove(element);
        putStrings(collectionKey, values);
    }

    static void removeIntElement(String collectionKey, Integer element) {
        Set<Integer> values = getIntSet(collectionKey);
        values.remove(element);
        putInts(collectionKey, values);
    }

    static void removeDoubleElement(String collectionKey, Double element) {
        Set<Double> values = getDoubleSet(collectionKey);
        values.remove(element);
        putDoubles(collectionKey, values);
    }

    static void removeCharElement(String collectionKey, Character element) {
        Set<Character> values = getCharSet(collectionKey);
        values.remove(element);
        putChars(collectionKey, values);
    }

    static void removeBoolElement(String collectionKey, Boolean element) {
        Set<Boolean> values = getBoolSet(collectionKey);
        values.remove(element);
        putBools(collectionKey, values);
    }

    static void removeLongElement(String collectionKey, Long element) {
        Set<Long> values = getLongSet(collectionKey);
        values.remove(element);
        putLongs(collectionKey, values);
    }


    static <T> Set<T> getSet(String key, Function<String, T> mapper) {
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

    static Set<String> getStringSet(String key) {
        if (isEmpty(key)) return emptySet();
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return emptySet();
        }
        String elements = string.get();
        if (isEmpty(elements)) return emptySet();
        return setOf(elements.split(ROCKS_DB_LIST_DELIMITER));
    }

    static Set<Integer> getIntSet(String key) {
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

    static Set<Double> getDoubleSet(String key) {
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

    static Set<Character> getCharSet(String key) {
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

    static Set<Long> getLongSet(String key) {
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

    static Set<Boolean> getBoolSet(String key) {
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
