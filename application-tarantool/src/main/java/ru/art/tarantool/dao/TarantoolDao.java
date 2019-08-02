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

package ru.art.tarantool.dao;

import ru.art.entity.*;
import ru.art.tarantool.model.TarantoolUpdateFieldOperation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TarantoolDao {
    private final TarantoolIndexDao indexDao;
    private final TarantoolValueDao valueDao;

    private TarantoolDao(String instanceId) {
        indexDao = new TarantoolIndexDao(instanceId);
        valueDao = new TarantoolValueDao(instanceId);
    }

    public static TarantoolDao tarantool(String instanceId) {
        return new TarantoolDao(instanceId);
    }

    public Entity put(String spaceName, Entity entity) {
        return valueDao.put(spaceName, entity);
    }

    public Entity put(String spaceName, Long id, Primitive primitive) {
        return valueDao.put(spaceName, id, primitive);
    }

    public Entity put(String spaceName, Long id, CollectionValue<?> collectionValue) {
        return valueDao.put(spaceName, id, collectionValue);
    }

    public Entity put(String spaceName, Long id, StringParametersMap stringParameters) {
        return valueDao.put(spaceName, id, stringParameters);
    }

    public Entity put(String spaceName, Long id, MapValue mapValue) {
        return valueDao.put(spaceName, id, mapValue);
    }

    public Entity put(String spaceName, Primitive primitive) {
        return valueDao.put(spaceName, primitive);
    }

    public Entity put(String spaceName, CollectionValue<?> collectionValue) {
        return valueDao.put(spaceName, collectionValue);
    }

    public Entity put(String spaceName, StringParametersMap stringParameters) {
        return valueDao.put(spaceName, stringParameters);
    }

    public Entity put(String spaceName, MapValue mapValue) {
        return valueDao.put(spaceName, mapValue);
    }

    public Optional<Entity> get(String spaceName, Set<?> keys) {
        return valueDao.get(spaceName, keys);
    }

    public Optional<Entity> get(String spaceName, long id) {
        return valueDao.get(spaceName, id);
    }

    public Optional<Primitive> getPrimitive(String spaceName, long id) {
        return valueDao.getPrimitive(spaceName, id);
    }

    public Optional<Primitive> getPrimitive(String spaceName) {
        return valueDao.getPrimitive(spaceName);
    }

    public Optional<CollectionValue<?>> getCollectionValue(String spaceName, long id) {
        return valueDao.getCollectionValue(spaceName, id);
    }

    public Optional<CollectionValue<?>> getCollectionValue(String spaceName) {
        return valueDao.getCollectionValue(spaceName);
    }

    public Optional<StringParametersMap> getStringParameters(String spaceName, long id) {
        return valueDao.getStringParameters(spaceName, id);
    }

    public Optional<StringParametersMap> getStringParameters(String spaceName) {
        return valueDao.getStringParameters(spaceName);
    }

    public Optional<MapValue> getMapValue(String spaceName, long id) {
        return valueDao.getMapValue(spaceName, id);
    }

    public Optional<MapValue> getMapValue(String spaceName) {
        return valueDao.getMapValue(spaceName);
    }

    public List<Entity> select(String spaceName, Set<?> keys) {
        return valueDao.select(spaceName, keys);
    }

    public List<Entity> select(String spaceName, long id) {
        return valueDao.select(spaceName, id);
    }

    public List<Entity> selectAll(String spaceName) {
        return valueDao.selectAll(spaceName);
    }

    public List<Primitive> selectPrimitives(String spaceName, Set<?> keys) {
        return valueDao.selectPrimitives(spaceName, keys);
    }

    public List<Primitive> selectPrimitives(String spaceName, long id) {
        return valueDao.selectPrimitives(spaceName, id);
    }

    public List<Primitive> selectAllPrimitives(String spaceName) {
        return valueDao.selectAllPrimitives(spaceName);
    }

    public List<CollectionValue<?>> selectCollections(String spaceName, Set<?> keys) {
        return valueDao.selectCollections(spaceName, keys);
    }

    public List<CollectionValue<?>> selectCollections(String spaceName, long id) {
        return valueDao.selectCollections(spaceName, id);
    }

    public List<CollectionValue<?>> selectAllCollections(String spaceName) {
        return valueDao.selectAllCollections(spaceName);
    }

    public List<StringParametersMap> selectStringParameters(String spaceName, Set<?> keys) {
        return valueDao.selectStringParameters(spaceName, keys);
    }

    public List<StringParametersMap> selectStringParameters(String spaceName, long id) {
        return valueDao.selectStringParameters(spaceName, id);
    }

    public List<StringParametersMap> selectAllStringParameters(String spaceName) {
        return valueDao.selectAllStringParameters(spaceName);
    }

    public List<MapValue> selectMaps(String spaceName, Set<?> keys) {
        return valueDao.selectMaps(spaceName, keys);
    }

    public List<MapValue> selectMaps(String spaceName, long id) {
        return valueDao.selectMaps(spaceName, id);
    }

    public List<MapValue> selectAllMaps(String spaceName) {
        return valueDao.selectAllMaps(spaceName);
    }

    public Entity insert(String spaceName, Entity entity) {
        return valueDao.insert(spaceName, entity);
    }

    public Entity insert(String spaceName, Long id, Primitive primitive) {
        return valueDao.insert(spaceName, id, primitive);
    }

    public Entity insert(String spaceName, Long id, CollectionValue<?> collectionValue) {
        return valueDao.insert(spaceName, id, collectionValue);
    }

    public Entity insert(String spaceName, Long id, StringParametersMap stringParameters) {
        return valueDao.insert(spaceName, id, stringParameters);
    }

    public Entity insert(String spaceName, Long id, MapValue mapValue) {
        return valueDao.insert(spaceName, id, mapValue);
    }

    public Entity insert(String spaceName, Primitive primitive) {
        return valueDao.insert(spaceName, primitive);
    }

    public Entity insert(String spaceName, CollectionValue<?> collectionValue) {
        return valueDao.insert(spaceName, collectionValue);
    }

    public Entity insert(String spaceName, StringParametersMap stringParameters) {
        return valueDao.insert(spaceName, stringParameters);
    }

    public Entity insert(String spaceName, MapValue mapValue) {
        return valueDao.insert(spaceName, mapValue);
    }

    public Optional<Entity> delete(String spaceName, Set<?> keys) {
        return valueDao.delete(spaceName, keys);
    }

    public Optional<Entity> delete(String spaceName, long id) {
        return valueDao.delete(spaceName, id);
    }

    public List<Entity> deleteAll(String spaceName) {
        return valueDao.deleteAll(spaceName);
    }

    public Optional<Entity> update(String spaceName, Set<?> keys, TarantoolUpdateFieldOperation... operations) {
        return valueDao.update(spaceName, keys, operations);
    }

    public void upsert(String spaceName, Entity defaultEntity, TarantoolUpdateFieldOperation... operations) {
        valueDao.upsert(spaceName, defaultEntity, operations);
    }

    public long count(String spaceName, Set<?> keys) {
        return valueDao.count(spaceName, keys);
    }

    public long count(String spaceName, long id) {
        return valueDao.count(spaceName, id);
    }

    public long count(String spaceName) {
        return valueDao.count(spaceName);
    }

    public long len(String spaceName) {
        return valueDao.len(spaceName);
    }

    public void truncate(String spaceName) {
        valueDao.truncate(spaceName);
    }

    public void sequencedId() {
        valueDao.sequencedId();
    }

    public void manualId() {
        valueDao.manualId();
    }

    public Optional<Entity> getByIndex(String spaceName, String indexName, Set<?> keys) {
        return indexDao.getByIndex(spaceName, indexName, keys);
    }

    public Optional<Entity> getByIndex(String spaceName, String indexName) {
        return indexDao.getByIndex(spaceName, indexName);
    }

    public List<Entity> selectByIndex(String spaceName, String indexName, Set<?> keys) {
        return indexDao.selectByIndex(spaceName, indexName, keys);
    }

    public List<Entity> selectAllByIndex(String spaceName, String indexName) {
        return indexDao.selectAllByIndex(spaceName, indexName);
    }

    public Optional<Entity> deleteByIndex(String spaceName, String indexName, Set<?> keys) {
        return indexDao.deleteByIndex(spaceName, indexName, keys);
    }

    public long countByIndex(String spaceName, String indexName, Set<?> keys) {
        return indexDao.countByIndex(spaceName, indexName, keys);
    }

    public long countByIndex(String spaceName, String indexName) {
        return indexDao.countByIndex(spaceName, indexName);
    }

    public long lenByIndex(String spaceName, String indexName) {
        return indexDao.lenByIndex(spaceName, indexName);
    }

    public Optional<Entity> updateByIndex(String spaceName, String indexName, Set<?> keys, TarantoolUpdateFieldOperation... operations) {
        return indexDao.updateByIndex(spaceName, indexName, keys, operations);
    }
}
