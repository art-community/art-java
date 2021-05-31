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

import com.google.common.hash.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapper.*;
import lombok.*;
import static com.google.common.hash.Hashing.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.context.Context.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.value.factory.PrimitivesFactory.*;
import java.nio.charset.*;
import java.util.*;

@SuppressWarnings(UNSTABLE_API_USAGE)
public class MetaSchema<T> {
    private final MetaConstructor<T> allArgumentsConstructor;
    private final Creator[] primitiveCreators;
    private final Creator[] objectCreators;
    private final Getter[] getterFromModel;
    private final Set<Primitive> fieldSet;
    private final HashFunction hashFunction = goodFastHash(32);
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
        getterFromModel = new Getter[gettableProperties.size()];
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

        for (int index = 0; index < gettableProperties.size(); index++) {
            MetaProperty<?> property = gettableProperties.get(index);
            String name = property.name();
            int getterIndex = (gettableProperties.size() - 1) & hashFunction.hashString(name, charset).asInt();
            getterFromModel[getterIndex] = new Getter(name, property.getter(), property.type().fromModel());
            fieldSet.add(stringPrimitive(name));
        }
    }

    private Value map(Object model, Primitive key) {
        int getterIndex = (getterFromModel.length - 1) & hashFunction.hashString(key.getString(), charset).asInt();
        Getter getter = getterFromModel[getterIndex];
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
