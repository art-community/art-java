/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.rocks.db.dao;

import ru.art.entity.CollectionValue;
import ru.art.entity.Entity;
import ru.art.entity.Primitive;
import ru.art.entity.Value;
import ru.art.rocks.db.bucket.Bucket;
import ru.art.rocks.db.exception.RocksDbOperationException;
import static java.util.Collections.emptyMap;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.entity.Value.*;
import static ru.art.protobuf.descriptor.ProtobufEntityReader.readProtobuf;
import static ru.art.protobuf.descriptor.ProtobufEntityWriter.writeProtobuf;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue;
import static ru.art.protobuf.entity.ProtobufValueMessage.ProtobufValue.parseFrom;
import static ru.art.rocks.db.constants.RocksDbExceptionMessages.PROTOBUF_PARSING_ERROR;
import static ru.art.rocks.db.constants.RocksDbModuleConstants.ROCKS_DB_KEY_DELIMITER;
import static ru.art.rocks.db.dao.RocksDbCollectionDao.*;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.add;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.getChar;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.getDouble;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.getLong;
import static ru.art.rocks.db.dao.RocksDbPrimitiveDao.*;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public interface RocksDbValueDao {
    static void putAsProtobuf(String name, String id, Value value) {
        if (isEmpty(name)) return;
        if (isEmpty(id)) return;
        if (isEmpty(value)) return;
        if (getValueIdentifiers(name).contains(id)) {
            putAsProtobuf(name + ROCKS_DB_KEY_DELIMITER + id, value);
            return;
        }
        add(name, id);
        putAsProtobuf(name + ROCKS_DB_KEY_DELIMITER + id, value);
    }

    static void putAsProtobuf(String entityKey, Value value) {
        if (isEmpty(entityKey)) return;
        if (isEmpty(value)) return;
        ProtobufValue protobufValue = writeProtobuf(value);
        byte[] keyBytes = entityKey.getBytes();
        byte[] valueBytes = protobufValue.toByteArray();
        RocksDbPrimitiveDao.put(keyBytes, valueBytes);
    }

    static Optional<Value> getAsProtobuf(String entityKey) {
        if (isEmpty(entityKey)) return empty();
        byte[] bytes = get(entityKey);
        if (isEmpty(bytes)) return empty();
        try {
            return ofNullable(readProtobuf(parseFrom(bytes)));
        } catch (Exception e) {
            throw new RocksDbOperationException(PROTOBUF_PARSING_ERROR, e);
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
            case COLLECTION:
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
        for (Entry<String, ? extends Value> entry : entity.getFields().entrySet()) {
            put(entityKey + ROCKS_DB_KEY_DELIMITER + entry.getKey(), entry.getValue());
        }
    }

    static void put(String entityKey, CollectionValue<?> collectionValue) {
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