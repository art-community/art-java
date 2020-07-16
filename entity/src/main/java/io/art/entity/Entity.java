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

package io.art.entity;

import lombok.*;
import io.art.core.checker.*;
import io.art.entity.constants.*;
import io.art.entity.exception.*;
import io.art.entity.mapper.*;
import static java.util.Collections.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.entity.CollectionValuesFactory.*;
import static io.art.entity.PrimitivesFactory.*;
import static io.art.entity.Value.*;
import static io.art.entity.constants.ExceptionMessages.*;
import static io.art.entity.constants.ValueType.*;
import java.text.*;
import java.util.*;

@Getter
@ToString
@EqualsAndHashCode
public class Entity implements Value {
    private final Map<? extends Value, ? extends Value> fields;
    private final Set<? extends Value> fieldKeys;

    private final ValueType type = ENTITY;

    public Entity(Map<? extends Value, ? extends Value> fields) {
        this.fields = fields;
        fieldKeys = fields.keySet();
    }

    public static EntityBuilder entityBuilder() {
        return new EntityBuilder();
    }

    public static Entity merge(Entity... entities) {
        if (EmptinessChecker.isEmpty(entities)) {
            return null;
        }
        EntityBuilder entityBuilder = entityBuilder();
        for (Entity entity : entities) {
            if (nonNull(entity)) {
                entityBuilder.fields.putAll(entity.fields);
            }
        }
        return entityBuilder.build();
    }

    public <T, V extends Value> T getValue(String name, ValueToModelMapper<T, V> mapper) {
        if (isNull(mapper)) throw new ValueMappingException(MAPPER_IS_NULL);
        return mapper.map(cast(fields.get(stringPrimitive(name))));
    }

    public Value getValue(String name) {
        return fields.get(stringPrimitive(name));
    }

    public Entity getEntity(String name) {
        return asEntity(fields.get(stringPrimitive(name)));
    }

    public String getString(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getString();
    }

    public Date getDate(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        try {
            return isNull(primitive) ? null : YYYY_MM_DD_T_HH_MM_SS_24H_SSS_Z_DASH_FORMAT.get().parse(primitive.getString());
        } catch (ParseException throwable) {
            throw new ValueMappingException(UNABLE_TO_PARSE_DATE);
        }
    }

    public Long getLong(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getLong();
    }

    public Byte getByte(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getByte();
    }

    public Double getDouble(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getDouble();
    }

    public Boolean getBool(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getBool();
    }

    public Integer getInt(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getInt();
    }

    public Float getFloat(String name) {
        Primitive primitive = asPrimitive(fields.get(stringPrimitive(name)));
        return isNull(primitive) ? null : primitive.getFloat();
    }

    public <K extends Value, V extends Value> Map<K, V> getMap(String name) {
        Entity value = getEntity(name);
        if (isNull(value)) return emptyMap();
        return value
                .fields
                .entrySet()
                .stream()
                .collect(toMap(entry -> cast(entry.getKey()), entry -> cast(entry.getValue())));
    }

    public <K, V> Map<K, V> getMap(String name, ValueToModelMapper<K, ? extends Value> keyMapper, ValueToModelMapper<V, ? extends Value> valueMapper) {
        Map<Value, Value> map = getMap(name);
        if (isNull(map)) return emptyMap();
        return map
                .entrySet()
                .stream()
                .collect(toMap(entry -> keyMapper.map(cast(entry.getKey())), entry -> valueMapper.map(cast(entry.getValue()))));
    }

    public <C> ArrayValue<C> getCollectionValue(String name) {
        ArrayValue<C> collection = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collection) ? null : collection;
    }


    public Value find(String key) {
        if (EmptinessChecker.isEmpty(key)) {
            return null;
        }
        Queue<String> sections = queueOf(key.split(ESCAPED_DOT));
        Entity entity = this;
        Value value = null;
        String section;
        while ((section = sections.poll()) != null) {
            value = entity.getValue(section);
            if (Value.isEmpty(value)) return null;
            if (!isEntity(value)) {
                if (sections.size() > 1) return null;
                return value;
            }
            entity = asEntity(value);
        }
        return value;
    }

    public String findString(String key) {
        Value value = find(key);
        if (Value.isEmpty(value)) return null;
        return asPrimitive(value).getString();
    }

    public Integer findInt(String name) {
        Primitive primitive = asPrimitive(find(name));
        return isNull(primitive) ? null : primitive.getInt();
    }

    public Long findLong(String name) {
        Primitive primitive = asPrimitive(find(name));
        return isNull(primitive) ? null : primitive.getLong();
    }

    public Double findDouble(String name) {
        Primitive primitive = asPrimitive(find(name));
        return isNull(primitive) ? null : primitive.getDouble();
    }

    public Boolean findBool(String name) {
        Primitive primitive = asPrimitive(find(name));
        return isNull(primitive) ? null : primitive.getBool();
    }

    public Float findFloat(String name) {
        Primitive primitive = asPrimitive(find(name));
        return isNull(primitive) ? null : primitive.getFloat();
    }

    public Entity findEntity(String name) {
        return asEntity(find(name));
    }

    public <T> ArrayValue<T> findCollectionValue(String name) {
        return asCollection(find(name));
    }


    public List<String> findStringList(String name) {
        ArrayValue<String> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getStringList();
    }

    public List<Integer> findIntList(String name) {
        ArrayValue<Integer> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getIntList();
    }

    public List<Double> findDoubleList(String name) {
        ArrayValue<Double> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getDoubleList();
    }

    public List<Long> findLongList(String name) {
        ArrayValue<Long> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getLongList();
    }

    public List<Boolean> findBoolList(String name) {
        ArrayValue<Integer> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getBoolList();
    }

    public List<Float> findFloatList(String name) {
        ArrayValue<Float> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getFloatList();
    }

    public List<Entity> findEntityList(String name) {
        ArrayValue<?> collectionValue = findCollectionValue(name);
        return isNull(collectionValue) ? emptyList() : collectionValue.getEntityList();
    }

    public <T> List<ArrayValue<T>> findCollectionValueList(String name) {
        ArrayValue<?> collectionValue = findCollectionValue(name);
        return isNull(collectionValue) ? emptyList() : cast(collectionValue.getCollectionsList());
    }


    public List<String> findStringSet(String name) {
        ArrayValue<String> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getStringList();
    }

    public List<Integer> findIntSet(String name) {
        ArrayValue<Integer> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getIntList();
    }

    public List<Double> findDoubleSet(String name) {
        ArrayValue<Double> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getDoubleList();
    }

    public Set<Long> findLongSet(String name) {
        ArrayValue<Long> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getLongSet();
    }

    public Set<Boolean> findBoolSet(String name) {
        ArrayValue<Integer> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getBoolSet();
    }

    public Set<Float> findFloatSet(String name) {
        ArrayValue<Float> collection = asCollection(find(name));
        return isNull(collection) ? null : collection.getFloatSet();
    }

    public Set<Entity> findEntitySet(String name) {
        ArrayValue<?> collectionValue = findCollectionValue(name);
        return isNull(collectionValue) ? emptySet() : collectionValue.getEntitySet();
    }

    public <T> Set<ArrayValue<T>> findCollectionValueSet(String name) {
        ArrayValue<?> collectionValue = findCollectionValue(name);
        return isNull(collectionValue) ? emptySet() : cast(collectionValue.getCollectionsSet());
    }


    public <T> List<T> getEntityList(String name, ValueToModelMapper<T, Entity> mapper) {
        ArrayValue<Entity> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getEntityList().stream().filter(Objects::nonNull)
                .map(mapper::map)
                .collect(toList());
    }

    public List<Entity> getEntityList(String name) {
        ArrayValue<Entity> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getEntityList();
    }

    public List<Value> getValueList(String name) {
        ArrayValue<Value> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getValueList();
    }

    public <C> List<ArrayValue<C>> getCollectionValueList(String name) {
        ArrayValue<ArrayValue<C>> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : cast(collectionValue.getCollectionsList());
    }

    public List<Long> getLongList(String name) {
        ArrayValue<Long> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getLongList();
    }

    public List<String> getStringList(String name) {
        ArrayValue<String> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getStringList();
    }

    public List<Boolean> getBoolList(String name) {
        ArrayValue<Boolean> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getBoolList();
    }

    public List<Integer> getIntList(String name) {
        ArrayValue<Integer> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getIntList();
    }

    public List<Double> getDoubleList(String name) {
        ArrayValue<Double> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getDoubleList();
    }

    public List<Byte> getByteList(String name) {
        ArrayValue<Byte> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getByteList();
    }

    public List<Float> getFloatList(String name) {
        ArrayValue<Float> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptyList() : collectionValue.getFloatList();
    }


    public <T> Set<T> getEntitySet(String name, ValueToModelMapper<T, Entity> mapper) {
        ArrayValue<Entity> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getEntitySet().stream().filter(Objects::nonNull)
                .map(mapper::map)
                .collect(toSet());
    }

    public Set<Entity> getEntitySet(String name) {
        ArrayValue<Entity> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getEntitySet();
    }

    public Set<Value> getValueSet(String name) {
        ArrayValue<Value> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getValueSet();
    }

    public <C> Set<ArrayValue<C>> getCollectionValueSet(String name) {
        ArrayValue<ArrayValue<C>> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : cast(collectionValue.getCollectionsSet());
    }

    public Set<Long> getLongSet(String name) {
        ArrayValue<Long> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getLongSet();
    }

    public Set<String> getStringSet(String name) {
        ArrayValue<String> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getStringSet();
    }

    public Set<Boolean> getBoolSet(String name) {
        ArrayValue<Boolean> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getBoolSet();

    }

    public Set<Integer> getIntSet(String name) {
        ArrayValue<Integer> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getIntSet();
    }

    public Set<Double> getDoubleSet(String name) {
        ArrayValue<Double> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getDoubleSet();
    }

    public Set<Byte> getByteSet(String name) {
        ArrayValue<Byte> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getByteSet();
    }

    public Set<Float> getFloatSet(String name) {
        ArrayValue<Float> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? emptySet() : collectionValue.getFloatSet();
    }


    public <T> Queue<T> getEntityQueue(String name, ValueToModelMapper<T, Entity> mapper) {
        ArrayValue<Entity> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getEntityQueue().stream().filter(Objects::nonNull)
                .map(mapper::map)
                .collect(toCollection(LinkedList::new));
    }

    public Queue<Entity> getEntityQueue(String name) {
        ArrayValue<Entity> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getEntityQueue();
    }

    public Queue<Value> getValueQueue(String name) {
        ArrayValue<Value> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getValueQueue();
    }

    public <C> Queue<ArrayValue<C>> getCollectionValueQueue(String name) {
        ArrayValue<ArrayValue<C>> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : cast(collectionValue.getCollectionsQueue());
    }

    public Queue<Long> getLongQueue(String name) {
        ArrayValue<Long> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getLongQueue();
    }

    public Queue<String> getStringQueue(String name) {
        ArrayValue<String> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getStringQueue();
    }

    public Queue<Boolean> getBoolQueue(String name) {
        ArrayValue<Boolean> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getBoolQueue();
    }

    public Queue<Integer> getIntQueue(String name) {
        ArrayValue<Integer> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getIntQueue();
    }

    public Queue<Double> getDoubleQueue(String name) {
        ArrayValue<Double> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getDoubleQueue();
    }

    public Queue<Byte> getByteQueue(String name) {
        ArrayValue<Byte> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getByteQueue();
    }

    public Queue<Float> getFloatQueue(String name) {
        ArrayValue<Float> collectionValue = asCollection(fields.get(stringPrimitive(name)));
        return isNull(collectionValue) ? new LinkedList<>() : collectionValue.getFloatQueue();
    }


    public <K, V> Map<K, V> getMap(String name, ValueToModelMapper<Map<K, V>, Entity> mapper) {
        Entity entity = getEntity(name);
        return isNull(entity) ? emptyMap() : mapper.map(entity);
    }

    public Map<String, String> dump() {
        if (isEmpty()) {
            return emptyMap();
        }
        Map<String, String> dump = mapOf();
        fields.forEach((key, value) -> {
            if (isPrimitive(value)) {
                dump.put(key.toString(), value.toString());
                return;
            }
            if (isEntity(value)) {
                asEntity(value).dump().forEach((innerKey, innerField) -> dump.put(key + DOT + innerKey, innerField));
                return;
            }
            if (isCollection(value)) {
                asCollection(value).dump().forEach((innerKey, innerField) -> dump.put(key + innerKey, innerField));
            }
        });
        return dump;
    }

    @Override
    public boolean isEmpty() {
        return EmptinessChecker.isEmpty(fields);
    }

    public static class EntityBuilder {
        private final Map<Value, Value> fields = mapOf();

        public EntityBuilder putFields(Map<? extends Value, ? extends Value> fields) {
            this.fields.putAll(fields);
            return this;
        }

        public EntityBuilder valueField(Value key, Value value) {
            fields.put(key, value);
            return this;
        }

        public EntityBuilder valueField(String name, Value value) {
            fields.put(stringPrimitive(name), value);
            return this;
        }

        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        public EntityBuilder valueField(String name, Optional<Value> optionalValue) {
            optionalValue.ifPresent(value -> fields.put(stringPrimitive(name), value));
            return this;
        }

        public <V> EntityBuilder valueField(String name, V value, ValueFromModelMapper<V, Value> mapper) {
            fields.put(stringPrimitive(name), mapper.map(value));
            return this;
        }

        public EntityBuilder intField(String name, Integer value) {
            fields.put(stringPrimitive(name), intPrimitive(value));
            return this;
        }

        public EntityBuilder longField(String name, Long value) {
            fields.put(stringPrimitive(name), longPrimitive(value));
            return this;
        }

        public EntityBuilder stringField(String name, String value) {
            fields.put(stringPrimitive(name), stringPrimitive(value));
            return this;
        }

        public EntityBuilder dateField(String name, Date value) {
            if (isNotEmpty(value)) {
                fields.put(stringPrimitive(name), stringPrimitive(YYYY_MM_DD_T_HH_MM_SS_24H_SSS_Z_DASH_FORMAT.get().format(value)));
            }
            return this;
        }

        public EntityBuilder boolField(String name, Boolean value) {
            fields.put(stringPrimitive(name), boolPrimitive(value));
            return this;
        }

        public EntityBuilder doubleField(String name, Double value) {
            fields.put(stringPrimitive(name), doublePrimitive(value));
            return this;
        }

        public EntityBuilder floatField(String name, Float value) {
            fields.put(stringPrimitive(name), floatPrimitive(value));
            return this;
        }

        public EntityBuilder byteField(String name, Byte value) {
            fields.put(stringPrimitive(name), bytePrimitive(value));
            return this;
        }

        public EntityBuilder entityField(String name, Entity entity) {
            fields.put(stringPrimitive(name), entity);
            return this;
        }

        public <T> EntityBuilder entityField(String name, T object, ValueFromModelMapper<T, Entity> mapper) {
            if (isNull(object)) {
                fields.put(stringPrimitive(name), null);
                return this;
            }
            return entityField(name, mapper.map(object));
        }

        public EntityBuilder mapField(String name, Map<? extends Value, ? extends Value> map) {
            fields.put(stringPrimitive(name), entityBuilder().putFields(map).build());
            return this;
        }

        public EntityBuilder mapField(String name, Map<?, ?> map, ValueFromModelMapper<?, ? extends Value> keyMapper, ValueFromModelMapper<?, ? extends Value> valueMapper) {
            if (isNull(map)) {
                mapField(name, emptyMap());
            }
            Map<? extends Value, ? extends Value> elements = map.entrySet()
                    .stream()
                    .collect(toMap(entry -> keyMapper.map(cast(entry.getKey())), entry -> valueMapper.map(cast(entry.getValue()))));
            mapField(name, elements);
            return this;
        }

        public EntityBuilder boolCollectionField(String name, Collection<Boolean> value) {
            fields.put(stringPrimitive(name), boolCollection(value));
            return this;
        }

        public EntityBuilder intCollectionField(String name, Collection<Integer> value) {
            fields.put(stringPrimitive(name), intCollection(value));
            return this;
        }

        public EntityBuilder longCollectionField(String name, Collection<Long> value) {
            fields.put(stringPrimitive(name), longCollection(value));
            return this;
        }

        public EntityBuilder stringCollectionField(String name, Collection<String> value) {
            fields.put(stringPrimitive(name), stringCollection(value));
            return this;
        }

        public EntityBuilder doubleCollectionField(String name, Collection<Double> value) {
            fields.put(stringPrimitive(name), doubleCollection(value));
            return this;
        }

        public EntityBuilder byteCollectionField(String name, Collection<Byte> value) {
            fields.put(stringPrimitive(name), byteCollection(value));
            return this;
        }

        public EntityBuilder floatCollectionField(String name, Collection<Float> value) {
            fields.put(stringPrimitive(name), floatCollection(value));
            return this;
        }


        public EntityBuilder boolArrayField(String name, boolean[] value) {
            fields.put(stringPrimitive(name), boolCollection(value));
            return this;
        }

        public EntityBuilder intArrayField(String name, int[] value) {
            fields.put(stringPrimitive(name), intCollection(value));
            return this;
        }

        public EntityBuilder longArrayField(String name, long[] value) {
            fields.put(stringPrimitive(name), longCollection(value));
            return this;
        }

        public EntityBuilder doubleArrayField(String name, double[] value) {
            fields.put(stringPrimitive(name), doubleCollection(value));
            return this;
        }

        public EntityBuilder byteArrayField(String name, byte[] value) {
            fields.put(stringPrimitive(name), byteCollection(value));
            return this;
        }

        public EntityBuilder floatArrayField(String name, float[] value) {
            fields.put(stringPrimitive(name), floatCollection(value));
            return this;
        }


        public EntityBuilder entityCollectionField(String name, Collection<Entity> value) {
            fields.put(stringPrimitive(name), collectionValue(CollectionElementsType.ENTITY, value));
            return this;
        }

        public EntityBuilder valueCollectionField(String name, Collection<Value> value) {
            fields.put(stringPrimitive(name), collectionValue(CollectionElementsType.VALUE, value));
            return this;
        }

        public EntityBuilder collectionValueCollectionField(String name, Collection<ArrayValue<?>> value) {
            fields.put(stringPrimitive(name), collectionValue(CollectionElementsType.COLLECTION, value));
            return this;
        }

        public <T> EntityBuilder entityCollectionField(String name, Collection<T> collection, ValueFromModelMapper<T, Entity> mapper) {
            if (isNull(collection)) {
                entityCollectionField(name, emptyList());
                return this;
            }
            return entityCollectionField(name, collection.stream()
                    .filter(Objects::nonNull)
                    .map(mapper::map)
                    .collect(toList()));
        }


        public Entity build() {
            return new Entity(cast(mapOf(fields)));
        }
    }
}
