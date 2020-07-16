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

package io.art.entity.builder;

import io.art.core.checker.*;
import io.art.entity.immutable.*;
import static java.util.Objects.nonNull;

public class EntityBuilder {
    private final Map<Value, Value> fields = mapOf();

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
        fields.put(stringPrimitive(name), boolArray(value));
        return this;
    }

    public EntityBuilder intCollectionField(String name, Collection<Integer> value) {
        fields.put(stringPrimitive(name), intArray(value));
        return this;
    }

    public EntityBuilder longCollectionField(String name, Collection<Long> value) {
        fields.put(stringPrimitive(name), longArray(value));
        return this;
    }

    public EntityBuilder stringCollectionField(String name, Collection<String> value) {
        fields.put(stringPrimitive(name), stringArray(value));
        return this;
    }

    public EntityBuilder doubleCollectionField(String name, Collection<Double> value) {
        fields.put(stringPrimitive(name), doubleArray(value));
        return this;
    }

    public EntityBuilder byteCollectionField(String name, Collection<Byte> value) {
        fields.put(stringPrimitive(name), byteArray(value));
        return this;
    }

    public EntityBuilder floatCollectionField(String name, Collection<Float> value) {
        fields.put(stringPrimitive(name), floatArray(value));
        return this;
    }


    public EntityBuilder boolArrayField(String name, boolean[] value) {
        fields.put(stringPrimitive(name), boolArray(value));
        return this;
    }

    public EntityBuilder intArrayField(String name, int[] value) {
        fields.put(stringPrimitive(name), intArray(value));
        return this;
    }

    public EntityBuilder longArrayField(String name, long[] value) {
        fields.put(stringPrimitive(name), longArray(value));
        return this;
    }

    public EntityBuilder doubleArrayField(String name, double[] value) {
        fields.put(stringPrimitive(name), doubleArray(value));
        return this;
    }

    public EntityBuilder byteArrayField(String name, byte[] value) {
        fields.put(stringPrimitive(name), byteArray(value));
        return this;
    }

    public EntityBuilder floatArrayField(String name, float[] value) {
        fields.put(stringPrimitive(name), floatArray(value));
        return this;
    }


    public EntityBuilder entityCollectionField(String name, Collection<Entity> value) {
        fields.put(stringPrimitive(name), array(CollectionElementsType.ENTITY, value));
        return this;
    }

    public EntityBuilder valueCollectionField(String name, Collection<Value> value) {
        fields.put(stringPrimitive(name), array(CollectionElementsType.VALUE, value));
        return this;
    }

    public EntityBuilder collectionValueCollectionField(String name, Collection<ArrayValue<?>> value) {
        fields.put(stringPrimitive(name), array(CollectionElementsType.COLLECTION, value));
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
