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
import static io.art.core.constants.ArrayConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.*;
import static io.art.rocks.db.storage.RocksDbPrimitiveStorage.*;
import static java.lang.Double.*;
import static java.lang.Integer.*;
import static java.lang.Long.*;
import static java.util.Objects.*;
import java.util.*;

@UtilityClass
public class RocksDbArraysStorage {
    public void add(String key, String[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (String element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (byte element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, char[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (char element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, int[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (int element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (double element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (boolean element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (long element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }


    public void add(String key, Byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (Byte element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, Character[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (char element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, Integer[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (int element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, Double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (double element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, Boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (boolean element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void add(String key, Long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (long element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }


    public void put(String key, String[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (String element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (byte element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, char[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (char element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, int[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (int element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (double element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (boolean element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (long element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }


    public void put(String key, Byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (Byte element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, Character[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (char element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, Integer[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (int element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, Double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (double element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, Boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (boolean element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }

    public void put(String key, Long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (long element : value) {
            RocksDbPrimitiveStorage.add(key, element);
        }
    }


    public String[] getStrings(String key) {
        if (isEmpty(key)) return EMPTY_STRINGS;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_STRINGS;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_STRINGS;
        return elements.split(ROCKS_DB_LIST_DELIMITER);
    }

    public int[] getInts(String key) {
        if (isEmpty(key)) return EMPTY_INTS;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_INTS;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_INTS;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_INTS;
        int[] values = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = parseInt(strings[i]);
        }
        return values;
    }

    public double[] getDoubles(String key) {
        if (isEmpty(key)) return EMPTY_DOUBLES;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_DOUBLES;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_DOUBLES;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_DOUBLES;
        double[] values = new double[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = parseDouble(strings[i]);
        }
        return values;
    }

    public char[] getChars(String key) {
        if (isEmpty(key)) return EMPTY_CHARS;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_CHARS;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_CHARS;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_CHARS;
        char[] values = new char[strings.length];
        for (int i = 0; i < strings.length; i++) {
            String chars = strings[i];
            if (isEmpty(chars)) continue;
            values[i] = chars.charAt(0);
        }
        return values;
    }

    public long[] getLongs(String key) {
        if (isEmpty(key)) return EMPTY_LONGS;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_LONGS;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_LONGS;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_LONGS;
        long[] values = new long[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = parseLong(strings[i]);
        }
        return values;
    }

    public boolean[] getBools(String key) {
        if (isEmpty(key)) return EMPTY_BOOLEANS;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_BOOLEANS;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_BOOLEANS;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_BOOLEANS;
        boolean[] values = new boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = TRUE_NUMERIC.equals(strings[i]);
        }
        return values;
    }


    public Integer[] getIntegerArray(String key) {
        if (isEmpty(key)) return EMPTY_INTEGER_ARRAY;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_INTEGER_ARRAY;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_INTEGER_ARRAY;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_INTEGER_ARRAY;
        Integer[] values = new Integer[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = Integer.valueOf(strings[i]);
        }
        return values;
    }

    public Double[] getDoubleArray(String key) {
        if (isEmpty(key)) return EMPTY_DOUBLE_ARRAY;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_DOUBLE_ARRAY;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_DOUBLE_ARRAY;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_DOUBLE_ARRAY;
        Double[] values = new Double[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = Double.valueOf(strings[i]);
        }
        return values;
    }

    public Character[] getCharArray(String key) {
        if (isEmpty(key)) return EMPTY_CHAR_ARRAY;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_CHAR_ARRAY;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_CHAR_ARRAY;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_CHAR_ARRAY;
        Character[] values = new Character[strings.length];
        for (int i = 0; i < strings.length; i++) {
            String chars = strings[i];
            if (isEmpty(chars)) continue;
            values[i] = chars.charAt(0);
        }
        return values;
    }

    public Long[] getLongArray(String key) {
        if (isEmpty(key)) return EMPTY_LONG_ARRAY;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_LONG_ARRAY;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_LONG_ARRAY;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_LONG_ARRAY;
        Long[] values = new Long[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = Long.valueOf(strings[i]);
        }
        return values;
    }

    public Boolean[] getBoolArray(String key) {
        if (isEmpty(key)) return EMPTY_BOOLEAN_ARRAY;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_BOOLEAN_ARRAY;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_BOOLEAN_ARRAY;
        String[] strings = elements.split(ROCKS_DB_LIST_DELIMITER);
        if (isEmpty(strings)) return EMPTY_BOOLEAN_ARRAY;
        Boolean[] values = new Boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            values[i] = Boolean.valueOf(strings[i]);
        }
        return values;
    }
}
