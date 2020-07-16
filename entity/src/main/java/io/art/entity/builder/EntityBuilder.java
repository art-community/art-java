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

import com.google.common.collect.*;
import io.art.core.checker.*;
import io.art.entity.immutable.*;
import io.art.entity.mapper.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.entity.factory.PrimitivesFactory.*;
import static io.art.entity.immutable.Entity.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.function.*;

public class EntityBuilder {
    private final ImmutableMap.Builder<Primitive, Supplier<? extends Value>> fields = ImmutableMap.builder();

    public static Entity merge(Entity... entities) {
        if (EmptinessChecker.isEmpty(entities)) {
            return null;
        }
        EntityBuilder entityBuilder = entityBuilder();
        for (Entity entity : entities) {
            if (nonNull(entity)) {
                entity.copyToMap().forEach((key, value) -> entityBuilder.fields.put(key, () -> value));
            }
        }
        return entityBuilder.build();
    }


    public EntityBuilder put(String key, Value value) {
        return put(stringPrimitive(key), value);
    }

    public EntityBuilder put(Integer key, Value value) {
        return put(intPrimitive(key), value);
    }

    public EntityBuilder put(Byte key, Value value) {
        return put(bytePrimitive(key), value);
    }

    public EntityBuilder put(Boolean key, Value value) {
        return put(boolPrimitive(key), value);
    }

    public EntityBuilder put(Long key, Value value) {
        return put(longPrimitive(key), value);
    }

    public EntityBuilder put(Double key, Value value) {
        return put(doublePrimitive(key), value);
    }

    public EntityBuilder put(Float key, Value value) {
        return put(floatPrimitive(key), value);
    }

    public EntityBuilder put(Primitive primitive, Value value) {
        fields.put(primitive, () -> value);
        return this;
    }


    public EntityBuilder putAllStrings(Map<String, Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllLongs(Map<Long, Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllInts(Map<Integer, Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllBools(Map<Boolean, Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllBytes(Map<Byte, Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllFloats(Map<Float, Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllDoubles(Map<Double, Value> map) {
        map.forEach(this::put);
        return this;
    }


    public <T, V extends Value> EntityBuilder put(String key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(stringPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Integer key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(intPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Long key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(longPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Byte key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(bytePrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Boolean key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(boolPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Double key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(doublePrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Float key, T value, ValueFromModelMapper<T, V> mapper) {
        return put(floatPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Primitive primitive, T value, ValueFromModelMapper<T, V> mapper) {
        fields.put(primitive, () -> mapper.map(value));
        return this;
    }


    public <T, V extends Value> EntityBuilder putAllStrings(Map<String, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder putAllLongs(Map<Long, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder putAllInts(Map<Integer, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder putAllBools(Map<Boolean, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder putAllBytes(Map<Byte, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder putAllFloats(Map<Float, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder putAllDoubles(Map<Double, T> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> put(key, value, mapper));
        return this;
    }


    public Entity build() {
        ImmutableMap<Primitive, Supplier<? extends Value>> map = fields.build();
        return new Entity(map.keySet(), field -> let(map.get(field), Supplier::get));
    }
}
