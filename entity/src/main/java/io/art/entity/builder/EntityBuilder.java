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
                entity.toMap().forEach((key, value) -> entityBuilder.fields.put(key, () -> value));
            }
        }
        return entityBuilder.build();
    }

    public EntityBuilder lazyPut(String key, Supplier<? extends Value> value) {
        return lazyPut(stringPrimitive(key), value);
    }

    public EntityBuilder lazyPut(Integer key, Supplier<? extends Value> value) {
        return lazyPut(intPrimitive(key), value);
    }

    public EntityBuilder lazyPut(Byte key, Supplier<? extends Value> value) {
        return lazyPut(bytePrimitive(key), value);
    }

    public EntityBuilder lazyPut(Boolean key, Supplier<? extends Value> value) {
        return lazyPut(boolPrimitive(key), value);
    }

    public EntityBuilder lazyPut(Long key, Supplier<? extends Value> value) {
        return lazyPut(longPrimitive(key), value);
    }

    public EntityBuilder lazyPut(Double key, Supplier<? extends Value> value) {
        return lazyPut(doublePrimitive(key), value);
    }

    public EntityBuilder lazyPut(Float key, Supplier<? extends Value> value) {
        return lazyPut(floatPrimitive(key), value);
    }

    public EntityBuilder lazyPut(Primitive primitive, Supplier<? extends Value> value) {
        if (!isEmpty(primitive) && nonNull(value)) {
            fields.put(primitive, value);
        }
        return this;
    }


    public EntityBuilder put(String key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Integer key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Byte key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Boolean key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Long key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Double key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Float key, Value value) {
        return lazyPut(key, () -> value);
    }

    public EntityBuilder put(Primitive key, Value value) {
        return lazyPut(key, () -> value);
    }


    public EntityBuilder lazyPutAll(Map<Primitive, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllStrings(Map<String, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllLongs(Map<Long, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllInts(Map<Integer, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllBools(Map<Boolean, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllBytes(Map<Byte, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllFloats(Map<Float, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }

    public EntityBuilder lazyPutAllDoubles(Map<Double, Supplier<? extends Value>> map) {
        map.forEach(this::lazyPut);
        return this;
    }


    public EntityBuilder putAll(Map<Primitive, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllStrings(Map<String, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllLongs(Map<Long, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllInts(Map<Integer, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllBools(Map<Boolean, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllBytes(Map<Byte, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllFloats(Map<Float, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }

    public EntityBuilder putAllDoubles(Map<Double, ? extends Value> map) {
        map.forEach(this::put);
        return this;
    }


    public <T, V extends Value> EntityBuilder lazyPut(String key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(stringPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Integer key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(intPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Long key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(longPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Byte key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(bytePrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Boolean key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(boolPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Double key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(doublePrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Float key, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        return lazyPut(floatPrimitive(key), value, mapper);
    }

    public <T, V extends Value> EntityBuilder lazyPut(Primitive primitive, Supplier<T> value, ValueFromModelMapper<T, V> mapper) {
        if (!isEmpty(primitive)) {
            fields.put(primitive, () -> mapper.map(value.get()));
        }
        return this;
    }


    public <T, V extends Value> EntityBuilder lazyPutAllStrings(Map<String, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder lazyPutAllLongs(Map<Long, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder lazyPutAllInts(Map<Integer, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder lazyPutAllBools(Map<Boolean, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder lazyPutAllBytes(Map<Byte, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder lazyPutAllFloats(Map<Float, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
        return this;
    }

    public <T, V extends Value> EntityBuilder lazyPutAllDoubles(Map<Double, Supplier<T>> map, ValueFromModelMapper<T, V> mapper) {
        map.forEach((key, value) -> lazyPut(key, value, mapper));
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
