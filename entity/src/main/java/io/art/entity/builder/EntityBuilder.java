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
                entity.copyToMap().forEach((key, value) -> entityBuilder.fields.put(key, () -> value));
            }
        }
        return entityBuilder.build();
    }

    public EntityBuilder put(String name, Value value) {
        return put(stringPrimitive(name), value);
    }

    public EntityBuilder put(Primitive primitive, Value value) {
        fields.put(primitive, () -> value);
        return this;
    }

    public <T, V extends Value> EntityBuilder put(String name, T value, ValueFromModelMapper<T, V> mapper) {
        return put(stringPrimitive(name), value, mapper);
    }

    public <T, V extends Value> EntityBuilder put(Primitive primitive, T value, ValueFromModelMapper<T, V> mapper) {
        fields.put(primitive, () -> mapper.map(value));
        return this;
    }

    public Entity build() {
        ImmutableMap<Primitive, Supplier<? extends Value>> map = fields.build();
        return new Entity(map.keySet(), field -> let(map.get(field), Supplier::get));
    }
}
