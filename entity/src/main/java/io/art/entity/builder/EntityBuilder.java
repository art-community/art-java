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


    public <T> EntityBuilder put(String key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(stringPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Integer key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(intPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Long key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(longPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Byte key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(bytePrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Boolean key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(boolPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Double key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(doublePrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Float key, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return put(floatPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder put(Primitive primitive, T value, ValueFromModelMapper<T, ? extends Value> mapper) {
        fields.put(primitive, () -> mapper.map(value));
        return this;
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


    public <T> EntityBuilder lazyPut(String key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(stringPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Integer key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(intPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Long key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(longPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Byte key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(bytePrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Boolean key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(boolPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Double key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(doublePrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Float key, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        return lazyPut(floatPrimitive(key), value, mapper);
    }

    public <T> EntityBuilder lazyPut(Primitive primitive, Supplier<T> value, ValueFromModelMapper<T, ? extends Value> mapper) {
        if (!isEmpty(primitive)) {
            fields.put(primitive, () -> mapper.map(value.get()));
        }
        return this;
    }

    public Entity build() {
        ImmutableMap<Primitive, Supplier<? extends Value>> map = fields.build();
        return new Entity(map.keySet(), field -> let(map.get(field), Supplier::get));
    }
}
