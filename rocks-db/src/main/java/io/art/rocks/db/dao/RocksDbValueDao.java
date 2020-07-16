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

import io.art.entity.immutable.*;
import io.art.rocks.db.bucket.*;
import io.art.rocks.db.exception.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.context.Context.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.immutable.Value.*;
import static io.art.message.pack.descriptor.MessagePackEntityReader.*;
import static io.art.message.pack.descriptor.MessagePackEntityWriter.*;
import static io.art.rocks.db.constants.RocksDbExceptionMessages.*;
import static io.art.rocks.db.constants.RocksDbModuleConstants.*;
import static io.art.rocks.db.dao.RocksDbCollectionDao.*;
import static io.art.rocks.db.dao.RocksDbPrimitiveDao.add;
import static io.art.rocks.db.dao.RocksDbPrimitiveDao.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;

public interface RocksDbValueDao {
    static void putBinary(String name, String id, Value value) {
        if (isEmpty(name)) return;
        if (isEmpty(id)) return;
        if (isEmpty(value)) return;
        if (getValueIdentifiers(name).contains(id)) {
            putBinary(name + ROCKS_DB_KEY_DELIMITER + id, value);
            return;
        }
        add(name, id);
        putBinary(name + ROCKS_DB_KEY_DELIMITER + id, value);
    }

    static void putBinary(String entityKey, Value value) {
        if (isEmpty(entityKey)) return;
        if (isEmpty(value)) return;
        byte[] keyBytes = entityKey.getBytes(contextConfiguration().getCharset());
        byte[] valueBytes = writeMessagePackToBytes(value);
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static Optional<Value> getBinary(String entityKey) {
        if (isEmpty(entityKey)) return empty();
        byte[] bytes = get(entityKey);
        if (isEmpty(bytes)) return empty();
        try {
            return ofNullable(readMessagePack(bytes));
        } catch (Throwable throwable) {
            throw new RocksDbOperationException(MESSAGE_PACK_PARSING_ERROR, throwable);
        }
    }


    static void put(String name, String id, Value value) {
        if (isEmpty(name)) return;
        if (isEmpty(id)) return;
        if (isEmpty(value)) return;
        if (getValueIdentifiers(name).contains(id)) {
            put(name + ROCKS_DB_KEY_DELIMITER + id, value);
            return;
        }
        add(name, id);
        put(name + ROCKS_DB_KEY_DELIMITER + id, value);
    }

    static void put(String entityKey, Value value) {
        if (isEmpty(entityKey)) return;
        if (isEmpty(value)) return;
        switch (value.getType()) {
            case ENTITY:
                put(entityKey, asEntity(value));
                break;
            case ARRAY:
                put(entityKey, asCollection(value));
                break;
            case STRING:
            case LONG:
            case DOUBLE:
            case INT:
            case BOOL:
            case BYTE:
                put(entityKey, asPrimitive(value));
        }
    }

    static void put(String entityKey, Primitive primitive) {
        if (isEmpty(entityKey)) return;
        if (isEmpty(primitive)) return;
        switch (primitive.getPrimitiveType()) {
            case STRING:
                RocksDbPrimitiveDao.put(entityKey, primitive.getString());
                break;
            case LONG:
                RocksDbPrimitiveDao.put(entityKey, primitive.getLong());
                break;
            case DOUBLE:
                RocksDbPrimitiveDao.put(entityKey, primitive.getDouble());
                break;
            case INT:
                RocksDbPrimitiveDao.put(entityKey, primitive.getInt());
                break;
            case BOOL:
                RocksDbPrimitiveDao.put(entityKey, primitive.getBool());
                break;
            case BYTE:
                RocksDbPrimitiveDao.put(entityKey, primitive.getByte());
                break;
        }
    }

    static void put(String entityKey, Entity entity) {
        if (isEmpty(entityKey)) return;
        if (isEmpty(entity)) return;
        for (Entry<? extends Value, ? extends Value> entry : entity.getFields().entrySet()) {
            if (!isPrimitive(entry.getKey())) continue;
            put(entityKey + ROCKS_DB_KEY_DELIMITER + entry.getKey(), entry.getValue());
        }
    }

    static void put(String entityKey, ArrayValue<?> collectionValue) {
        if (isEmpty(entityKey)) return;
        if (isEmpty(collectionValue)) return;
        switch (collectionValue.getElementsType()) {
            case ENTITY:
            case COLLECTION: {
                int index = 0;
                for (Object element : collectionValue.getElements()) {
                    put(entityKey + ROCKS_DB_KEY_DELIMITER + index, (Value) element);
                }
            }
            break;
            case STRING:
                putStrings(entityKey, collectionValue.getStringList());
                break;
            case LONG: {
                switch (collectionValue.getCollectionMode()) {
                    case PRIMITIVE_ARRAY:
                        RocksDbArraysDao.put(entityKey, collectionValue.getLongArray());
                        break;
                    case COLLECTION:
                        putLongs(entityKey, collectionValue.getLongList());
                        break;
                }
            }
            break;
            case DOUBLE: {
                switch (collectionValue.getCollectionMode()) {
                    case PRIMITIVE_ARRAY:
                        RocksDbArraysDao.put(entityKey, collectionValue.getDoubleArray());
                        break;
                    case COLLECTION:
                        putDoubles(entityKey, collectionValue.getDoubleList());
                        break;
                }
            }
            break;
            case INT: {
                switch (collectionValue.getCollectionMode()) {
                    case PRIMITIVE_ARRAY:
                        RocksDbArraysDao.put(entityKey, collectionValue.getIntArray());
                        break;
                    case COLLECTION:
                        putInts(entityKey, collectionValue.getIntList());
                        break;
                }
            }
            break;
            case BOOL: {
                switch (collectionValue.getCollectionMode()) {
                    case PRIMITIVE_ARRAY:
                        RocksDbArraysDao.put(entityKey, collectionValue.getBoolArray());
                        break;
                    case COLLECTION:
                        putBools(entityKey, collectionValue.getBoolList());
                        break;
                }
            }
            break;
            case BYTE: {
                switch (collectionValue.getCollectionMode()) {
                    case PRIMITIVE_ARRAY:
                        RocksDbArraysDao.put(entityKey, collectionValue.getByteArray());
                        break;
                    case COLLECTION:
                        putBytes(entityKey, collectionValue.getByteList());
                        break;
                }
            }
            break;
        }
    }


    static Optional<String> getEntityStringField(String entityKey, String fieldName) {
        return getString(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static Optional<Integer> getEntityIntField(String entityKey, String fieldName) {
        return getInt(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static Optional<Double> getEntityDoubleField(String entityKey, String fieldName) {
        return getDouble(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static Optional<Long> getEntityLongField(String entityKey, String fieldName) {
        return getLong(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static Optional<Character> getEntityCharField(String entityKey, String fieldName) {
        return getChar(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static Optional<Boolean> getEntityBoolField(String entityKey, String fieldName) {
        return getBoolean(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static Optional<Byte> getEntityByteField(String entityKey, String fieldName) {
        return getByte(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }


    static void putEntityField(String entityKey, String fieldName, String value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName, value);
    }

    static void putEntityField(String entityKey, String fieldName, int value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName, value);
    }

    static void putEntityField(String entityKey, String fieldName, double value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName, value);
    }

    static void putEntityField(String entityKey, String fieldName, char value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName, value);
    }

    static void putEntityField(String entityKey, String fieldName, long value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName, value);
    }

    static void putEntityField(String entityKey, String fieldName, boolean value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName, value);
    }


    static List<String> getEntityStringList(String entityKey, String fieldName) {
        return getStringList(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static List<Integer> getEntityIntStringList(String entityKey, String fieldName) {
        return getIntList(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static List<Double> getEntityDoubleList(String entityKey, String fieldName) {
        return getDoubleList(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static List<Long> getEntityLongList(String entityKey, String fieldName) {
        return getLongList(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static List<Character> getEntityCharList(String entityKey, String fieldName) {
        return getCharList(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }

    static List<Boolean> getEntityBoolList(String entityKey, String fieldName) {
        return getBoolList(entityKey + ROCKS_DB_KEY_DELIMITER + fieldName);
    }


    static Optional<String> getEntityStringField(Bucket bucket) {
        return getString(bucket.dbKey());
    }

    static Optional<Integer> getEntityIntField(Bucket bucket) {
        return getInt(bucket.dbKey());
    }

    static Optional<Double> getEntityDoubleField(Bucket bucket) {
        return getDouble(bucket.dbKey());
    }

    static Optional<Long> getEntityLongField(Bucket bucket) {
        return getLong(bucket.dbKey());
    }

    static Optional<Character> getEntityCharField(Bucket bucket) {
        return getChar(bucket.dbKey());
    }

    static Optional<Boolean> getEntityBoolField(Bucket bucket) {
        return getBoolean(bucket.dbKey());
    }

    static Optional<Byte> getEntityByteField(Bucket bucket) {
        return getByte(bucket.dbKey());
    }


    static void putEntityField(Bucket bucket, String value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(bucket.dbKey(), value);
    }

    static void putEntityField(Bucket bucket, int value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(bucket.dbKey(), value);
    }

    static void putEntityField(Bucket bucket, double value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(bucket.dbKey(), value);
    }

    static void putEntityField(Bucket bucket, char value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(bucket.dbKey(), value);
    }

    static void putEntityField(Bucket bucket, long value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(bucket.dbKey(), value);
    }

    static void putEntityField(Bucket bucket, boolean value) {
        if (isEmpty(value)) return;
        RocksDbPrimitiveDao.put(bucket.dbKey(), value);
    }


    static List<String> getEntityStringList(Bucket bucket) {
        return getStringList(bucket.dbKey());
    }

    static List<Integer> getEntityIntStringList(Bucket bucket) {
        return getIntList(bucket.dbKey());
    }

    static List<Double> getEntityDoubleList(Bucket bucket) {
        return getDoubleList(bucket.dbKey());
    }

    static List<Character> getEntityCharList(Bucket bucket) {
        return getCharList(bucket.dbKey());
    }

    static List<Boolean> getEntityBoolList(Bucket bucket) {
        return getBoolList(bucket.dbKey());
    }

    static void deleteValue(String name, String id) {
        String dbKey = name + ROCKS_DB_KEY_DELIMITER + id;
        deleteByPrefix(dbKey);
        delete(dbKey);
        Set<String> valueIdentifiers = getValueIdentifiers(name);
        valueIdentifiers.remove(id);
        putStrings(name, valueIdentifiers);
    }


    static void deleteValues(String name) {
        deleteByPrefix(name);
        delete(name);
    }

    static Set<String> getValueIdentifiers(String name) {
        return getStringSet(name);
    }

    static <T> Map<String, T> getValues(String name, Function<String, T> getter) {
        if (isEmpty(name)) return emptyMap();
        if (isNull(getter)) return emptyMap();
        Map<String, T> values = mapOf();
        Set<String> valueIdentifiers = getValueIdentifiers(name);
        for (String identifier : valueIdentifiers) {
            values.put(identifier, getter.apply(identifier));
        }
        return values;
    }
}
