/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.rocks.db.dao;

import org.rocksdb.*;
import ru.art.rocks.db.exception.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import static ru.art.core.constants.ArrayConstants.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.rocks.db.constants.RocksDbExceptionMessages.*;
import static ru.art.rocks.db.constants.RocksDbLoggingMessages.*;
import static ru.art.rocks.db.module.RocksDbModule.*;
import java.util.*;
import java.util.function.*;

@SuppressWarnings("Duplicates")
public interface RocksDbPrimitiveDao {
    static <T> void put(String key, T value, Function<T, byte[]> mapper) {
        if (isEmpty(mapper)) return;
        if (isEmpty(key)) return;
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(key.getBytes(contextConfiguration().getCharset()), mapper.apply(value));
    }

    static void put(byte[] key, byte[] value) {
        if (isEmpty(key)) return;
        if (isEmpty(value)) return;
        try {
            if (rocksDbModule().isEnableTracing()) {
                loggingModule()
                        .getLogger(RocksDbPrimitiveDao.class)
                        .trace(format(PUT_OPERATION, new String(key, contextConfiguration().getCharset()), new String(value, contextConfiguration().getCharset())));
            }
            rocksDbModuleState().getDb().put(key, value);
        } catch (RocksDBException throwable) {
            throw new RocksDbOperationException(PUT_ERROR, throwable);
        }
    }

    static void put(String key, String value) {
        if (isEmpty(key)) return;
        if (isEmpty(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, int value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Integer) value).toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, char value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = new byte[]{(byte) value};
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, double value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Double) value).toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, long value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Long) value).toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, byte value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Byte) value).toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, boolean value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Boolean) value).toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }


    static void put(String key, Integer value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, Character value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, Double value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, Long value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, Byte value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static void put(String key, Boolean value) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }


    static <T> void add(String key, T value, Function<T, byte[]> mapper) {
        if (isEmpty(mapper)) return;
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        RocksDbPrimitiveDao.add(key.getBytes(contextConfiguration().getCharset()), mapper.apply(value));
    }

    static void add(byte[] key, byte[] value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        try {
            if (rocksDbModule().isEnableTracing()) {
                String loggingValue = isEmpty(value) ? EMPTY_STRING : new String(value, contextConfiguration().getCharset());
                loggingModule()
                        .getLogger(RocksDbPrimitiveDao.class)
                        .trace(format(MERGE_OPERATION, new String(key, contextConfiguration().getCharset()), loggingValue));
            }
            rocksDbModuleState().getDb().merge(key, value);
        } catch (RocksDBException throwable) {
            throw new RocksDbOperationException(MERGE_ERROR, throwable);
        }
    }

    static void add(String key, String value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, int value) {
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Integer) value).toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, char value) {
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = new byte[]{(byte) value};
        add(keyBytes, valueBytes);
    }

    static void add(String key, double value) {
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Double) value).toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, long value) {
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = ((Long) value).toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, byte value) {
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = new byte[]{value};
        add(keyBytes, valueBytes);
    }

    static void add(String key, boolean value) {
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value ? new byte[]{(byte) 1} : new byte[]{(byte) 0};
        add(keyBytes, valueBytes);
    }

    static void add(String key, Integer value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, Character value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, Double value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, Long value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, Byte value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value.toString().getBytes(contextConfiguration().getCharset());
        add(keyBytes, valueBytes);
    }

    static void add(String key, Boolean value) {
        if (isEmpty(key)) return;
        if (isNull(value)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = value ? new byte[]{(byte) 1} : new byte[]{(byte) 0};
        add(keyBytes, valueBytes);
    }


    static <T> Optional<T> get(String key, Function<byte[], T> mapper) {
        if (isEmpty(key)) return empty();
        if (isEmpty(mapper)) return empty();
        return ofNullable(mapper.apply(RocksDbPrimitiveDao.get(key)));
    }

    static byte[] get(String key) {
        if (isEmpty(key)) return EMPTY_BYTES;
        return get(key.getBytes(contextConfiguration().getCharset()));
    }

    static byte[] get(byte[] key) {
        if (isEmpty(key)) return EMPTY_BYTES;
        try {
            if (rocksDbModule().isEnableTracing()) {
                loggingModule()
                        .getLogger(RocksDbPrimitiveDao.class)
                        .trace(format(GET_START_OPERATION, new String(key, contextConfiguration().getCharset())));
            }
            byte[] valueBytes = rocksDbModuleState().getDb().get(key);
            if (rocksDbModule().isEnableTracing()) {
                String value = isEmpty(valueBytes) ? EMPTY_STRING : new String(valueBytes, contextConfiguration().getCharset());
                loggingModule()
                        .getLogger(RocksDbPrimitiveDao.class)
                        .trace(format(GET_END_OPERATION, value, new String(key, contextConfiguration().getCharset())));
            }
            return valueBytes;
        } catch (RocksDBException throwable) {
            throw new RocksDbOperationException(GET_ERROR, throwable);
        }
    }

    static Optional<String> getString(String key) {
        if (isEmpty(key)) return empty();
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        byte[] bytes = RocksDbPrimitiveDao.get(keyBytes);
        if (isEmpty(bytes)) return empty();
        return of(new String(bytes, contextConfiguration().getCharset()));
    }

    static Optional<Integer> getInt(String key) {
        return getString(key).map(Integer::valueOf);
    }

    static Optional<Character> getChar(String key) {
        return getString(key).map(value -> value.charAt(0));
    }

    static Optional<Double> getDouble(String key) {
        return getString(key).map(Double::valueOf);
    }

    static Optional<Long> getLong(String key) {
        return getString(key).map(Long::valueOf);
    }

    static Optional<Byte> getByte(String key) {
        return getString(key).map(Byte::valueOf);
    }

    static Optional<Boolean> getBoolean(String key) {
        return getString(key).map(Boolean::valueOf);
    }


    static void delete(String key) {
        if (isEmpty(key)) return;
        byte[] keyBytes = key.getBytes(contextConfiguration().getCharset());
        RocksDbPrimitiveDao.delete(keyBytes);
    }

    static void delete(byte[] key) {
        if (isEmpty(key)) return;
        try {
            if (rocksDbModule().isEnableTracing()) {
                loggingModule()
                        .getLogger(RocksDbPrimitiveDao.class)
                        .trace(format(DELETE_OPERATION, new String(key, contextConfiguration().getCharset())));
            }
            rocksDbModuleState().getDb().delete(key);
        } catch (RocksDBException throwable) {
            throw new RocksDbOperationException(DELETE_ERROR, throwable);
        }
    }

    static void deleteByPrefix(String prefix) {
        if (rocksDbModule().isEnableTracing()) {
            loggingModule()
                    .getLogger(RocksDbPrimitiveDao.class)
                    .trace(format(DELETE_BY_PREFIX_OPERATION, prefix));
        }

        RocksIterator iterator = rocksDbModuleState().getDb().newIterator();
        iterator.seek(prefix.getBytes(contextConfiguration().getCharset()));
        while (iterator.isValid()) {
            byte[] keyBytes = iterator.key();
            iterator.next();
            if (isEmpty(keyBytes)) continue;
            if (!new String(keyBytes, contextConfiguration().getCharset()).startsWith(prefix)) continue;
            delete(keyBytes);
        }
    }


    static boolean exists(String key) {
        return isNotEmpty(get(key));
    }

    static boolean exists(byte[] key) {
        return isNotEmpty(get(key));
    }
}