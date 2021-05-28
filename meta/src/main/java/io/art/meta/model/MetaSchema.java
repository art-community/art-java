/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.meta.model;

import io.art.value.builder.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapping.*;
import lombok.*;
import static io.art.value.immutable.Entity.*;
import java.util.*;

@Builder
public class MetaSchema<T> {
    private final MetaConstructor<T> allArgumentsConstructor;
    private final List<MetaProperty<?>> gettableProperties;
    private final List<MetaProperty<?>> objectProperties;
    private final List<MetaProperty<?>> primitiveProperties;

    public T toModel(Value value) {
        Entity entity = Value.asEntity(value);
        EntityMapping mapping = entity.mapping();

        Object[] arguments = new Object[objectProperties.size() + primitiveProperties.size()];

        for (MetaProperty<?> property : primitiveProperties) {
            String name = property.name();
            MetaType<?> type = property.type();
            arguments[property.index()] = mapping.mapOrDefault(name, type.primitiveType(), type::toModel);
        }

        for (MetaProperty<?> property : objectProperties) {
            String name = property.name();
            MetaType<?> type = property.type();
            arguments[property.index()] = mapping.map(name, type::toModel);
        }

        return allArgumentsConstructor.invoke(arguments);
    }

    public Value fromModel(Object model) {
        EntityBuilder entityBuilder = entityBuilder();
        for (MetaProperty<?> property : gettableProperties) {
            InstanceMetaMethod<Object, ?> getter = property.getter();
            entityBuilder.put(property.name(), getter.invoke(model), getter.returnType()::fromModel);
        }
        return entityBuilder.build();
    }
}
