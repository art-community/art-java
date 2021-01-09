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

package io.art.value.registry;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.value.mapper.ValueMapper.*;
import java.lang.reflect.*;
import java.util.*;

@UsedByGenerator
public class ValueMapperRegistry {
    private final Map<Type, ValueToModelMapper<?, ? extends Value>> toModel = map();
    private final Map<Type, ValueFromModelMapper<?, ? extends Value>> fromModel = map();

    public ValueMapperRegistry registerToModel(Type type, ValueToModelMapper<?, ? extends Value> mapper) {
        toModel.put(type, mapper);
        return this;
    }

    public ValueMapperRegistry registerFromModel(Type type, ValueFromModelMapper<?, ? extends Value> mapper) {
        fromModel.put(type, mapper);
        return this;
    }

    private ImmutableMap<Type, ValueMapper<?, ? extends Value>> getMappers() {
        return toModel
                .entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, toModel -> mapper(fromModel.get(toModel.getKey()), cast(toModel.getValue()))));
    }

    public ValueMapper<?, ? extends Value> get(Type type) {
        return getMappers().get(type);
    }
}
