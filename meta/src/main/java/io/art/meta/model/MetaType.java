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

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.Builder;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.TypeConstants.*;
import static io.art.meta.model.KnownMappersComputer.*;
import static io.art.meta.registry.MetaClassRegistry.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@ForGenerator
@Builder(toBuilder = true)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetaType<T> {
    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @Accessors(fluent = true)
    private final Class<T> type;

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @Accessors(fluent = true)
    private final ImmutableSet<MetaType<?>> parameters;

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @Accessors(fluent = true)
    private final boolean primitive;

    @Getter
    @Accessors(fluent = true)
    private final PrimitiveType primitiveType;

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @Accessors(fluent = true)
    private final boolean array;

    @Getter
    @ToString.Include
    @EqualsAndHashCode.Include
    @Accessors(fluent = true)
    private final String variable;

    @Getter
    @Accessors(fluent = true)
    private final Function<Integer, T> asArray;

    private ValueToModelMapper<Object, Value> toModel;
    private ValueFromModelMapper<Object, Value> fromModel;

    @Getter(lazy = true, value = PRIVATE)
    private final static ImmutableMap<Class<?>, MetaClass<?>> classes = classes();

    protected MetaType<T> compute() {
        if (nonNull(toModel) && nonNull(fromModel)) return this;
        MetaClass<?> metaClass = getClasses().get(type);
        if (isNull(metaClass)) {
            ValueMapper<T, Value> mapper = computeKnownMappers(this);
            toModel = cast(mapper.getToModel());
            fromModel = cast(mapper.getFromModel());
            return this;
        }
        MetaClass<T> typedMetaClass = cast(metaClass.parameterize(parameters));
        toModel = typedMetaClass::toModel;
        fromModel = typedMetaClass::fromModel;
        return this;
    }

    protected MetaType<?> parameterize(Map<String, MetaType<?>> parameters) {
        ImmutableSet<MetaType<?>> parametrizedTypeParameters = this.parameters
                .stream()
                .map(parameter -> parameter.parameterize(parameters))
                .collect(immutableSetCollector());
        MetaTypeBuilder<?> builder = toBuilder()
                .variable(null)
                .parameters(parametrizedTypeParameters);
        if (isNull(variable)) {
            return builder.build();
        }
        MetaType<?> parameter = parameters.get(variable);
        if (nonNull(parameter)) {
            builder.type(cast(parameter.type)).asArray(cast(parameter.asArray));
        }
        return builder.build();
    }

    public Object toModel(Value value) {
        return toModel.map(value);
    }

    public Value fromModel(Object model) {
        return fromModel.map(model);
    }

    public static <T> MetaType<T> metaType(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?>... parameters) {
        return MetaType.<T>builder()
                .type(cast(type))
                .primitive(type.isPrimitive())
                .primitiveType(PRIMITIVE_TYPE_MAPPINGS.get(type))
                .asArray(cast(arrayFactory))
                .parameters(immutableSetOf(parameters))
                .build();
    }

    public static <T> MetaType<T> metaVariable(String variable) {
        return MetaType.<T>builder()
                .type(cast(Object.class))
                .variable(variable)
                .build();
    }

    public static <T> MetaType<T> metaArray(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?>... parameters) {
        return MetaType.<T>builder()
                .type(cast(type))
                .primitive(type.isPrimitive())
                .primitiveType(PRIMITIVE_TYPE_MAPPINGS.get(type))
                .asArray(cast(arrayFactory))
                .array(true)
                .parameters(immutableSetOf(parameters))
                .build();
    }
}
