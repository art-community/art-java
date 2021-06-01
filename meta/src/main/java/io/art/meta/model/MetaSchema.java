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

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableHashTable.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.HashExtensions.*;
import static io.art.core.factory.PairFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.value.factory.PrimitivesFactory.*;
import java.nio.charset.*;
import java.util.*;

public class MetaSchema<T> {
    private final MetaConstructor<T> allArgumentsConstructor;
    private final Creator[] primitiveCreators;
    private final Creator[] objectCreators;
    private final ImmutableHashTable<String, Getter> getters;
    private final Set<Primitive> fieldSet;
    private final Charset charset = context().configuration().getCharset();


    @AllArgsConstructor
    @RequiredArgsConstructor
    private static class Creator {
        final int index;
        final Primitive name;
        final ValueToModelMapper<Object, Value> mapper;
        PrimitiveType primitiveType;
    }

    @AllArgsConstructor
    private static class Getter {
        final String name;
        final InstanceMetaMethod<Object, ?> method;
        final ValueFromModelMapper<Object, Value> mapper;
    }

    @Builder
    public MetaSchema(MetaConstructor<T> allArgumentsConstructor, List<MetaProperty<?>> gettableProperties, List<MetaProperty<?>> objectProperties, List<MetaProperty<?>> primitiveProperties) {
        this.allArgumentsConstructor = allArgumentsConstructor;
        primitiveCreators = new Creator[primitiveProperties.size()];
        objectCreators = new Creator[objectProperties.size()];
        fieldSet = set();

        for (int index = 0; index < primitiveProperties.size(); index++) {
            MetaProperty<?> property = primitiveProperties.get(index);
            Primitive name = stringPrimitive(property.name());
            primitiveCreators[index] = new Creator(property.index(), name, property.type().toModel(), property.type().primitiveType());
            fieldSet.add(name);
        }

        for (int index = 0; index < objectProperties.size(); index++) {
            MetaProperty<?> property = objectProperties.get(index);
            Primitive name = stringPrimitive(property.name());
            objectCreators[index] = new Creator(property.index(), name, property.type().toModel());
            fieldSet.add(name);
        }

        Pair<String, Getter>[] gettersContent = cast(gettableProperties
                .stream()
                .peek(property -> fieldSet.add(stringPrimitive(property.name())))
                .map(property -> pairOf(property.name(), new Getter(property.name(), property.getter(), property.type().fromModel())))
                .toArray(Pair[]::new));
        getters = immutableHashTable(key -> xx32(key.getBytes(charset)), gettersContent);
    }

    private Value map(Object model, Primitive key) {
        Getter getter = getters.get(key.getString());
        return getter.mapper.map(getter.method.invoke(model));
    }

    public T toModel(Value value) {
        Entity entity = Value.asEntity(value);
        Object[] arguments = new Object[objectCreators.length + primitiveCreators.length];
        for (Creator creator : primitiveCreators) {
            arguments[creator.index] = entity.mapOrDefault(creator.name, creator.primitiveType, creator.mapper);
        }
        for (Creator creator : objectCreators) {
            arguments[creator.index] = entity.map(creator.name, creator.mapper);
        }
        return allArgumentsConstructor.invoke(arguments);
    }

    public Value fromModel(Object model) {
        return new Entity(fieldSet, key -> map(model, key));
    }
}
