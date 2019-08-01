package ru.adk.rocks.db.dao;

import static java.util.Objects.isNull;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.core.constants.ArrayConstants.*;
import static ru.adk.core.constants.StringConstants.TRUE_NUMERIC;
import static ru.adk.rocks.db.constants.RocksDbModuleConstants.ROCKS_DB_LIST_DELIMITER;
import static ru.adk.rocks.db.dao.RocksDbPrimitiveDao.delete;
import static ru.adk.rocks.db.dao.RocksDbPrimitiveDao.getString;
import java.util.Optional;

@SuppressWarnings("Duplicates")
public interface RocksDbArraysDao {
    static void add(String key, String[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (String element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (byte element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, char[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (char element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, int[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (int element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (double element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (boolean element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (long element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }


    static void add(String key, Byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (Byte element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, Character[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (char element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, Integer[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (int element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, Double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (double element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, Boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (boolean element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void add(String key, Long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        for (long element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }


    static void put(String key, String[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (String element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (byte element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, char[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (char element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, int[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (int element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (double element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (boolean element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (long element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }


    static void put(String key, Byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (Byte element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, Character[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (char element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, Integer[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (int element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, Double[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (double element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, Boolean[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (boolean element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }

    static void put(String key, Long[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        delete(key);
        for (long element : value) {
            RocksDbPrimitiveDao.add(key, element);
        }
    }


    static String[] getStrings(String key) {
        if (isEmpty(key)) return EMPTY_STRINGS;
        Optional<String> string = getString(key);
        if (!string.isPresent()) {
            return EMPTY_STRINGS;
        }
        String elements = string.get();
        if (isEmpty(elements)) return EMPTY_STRINGS;
        return elements.split(ROCKS_DB_LIST_DELIMITER);
    }

    static int[] getInts(String key) {
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
            values[i] = Integer.valueOf(strings[i]);
        }
        return values;
    }

    static double[] getDoubles(String key) {
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
            values[i] = Double.valueOf(strings[i]);
        }
        return values;
    }

    static char[] getChars(String key) {
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

    static long[] getLongs(String key) {
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
            values[i] = Long.valueOf(strings[i]);
        }
        return values;
    }

    static boolean[] getBools(String key) {
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


    static Integer[] getIntegerArray(String key) {
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

    static Double[] getDoubleArray(String key) {
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

    static Character[] getCharArray(String key) {
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

    static Long[] getLongArray(String key) {
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

    static Boolean[] getBoolArray(String key) {
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
