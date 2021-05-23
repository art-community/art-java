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
import io.art.meta.computer.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.factory.SetFactory.*;
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
    @ToString.Include
    @EqualsAndHashCode.Include
    private Class<T> type;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final ImmutableSet<MetaType<?>> parameters;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final boolean array;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final String variable;

    private final Function<Integer, T> arrayFactory;
    private ValueToModelMapper<T, Value> toModel;
    private ValueFromModelMapper<T, Value> fromModel;

    @Getter(lazy = true, value = PRIVATE)
    private final ImmutableMap<Class<?>, MetaClass<?>> classes = classes();

    public Class<T> type() {
        return type;
    }

    public ImmutableSet<MetaType<?>> parameters() {
        return parameters;
    }

    public boolean array() {
        return array;
    }

    public Function<Integer, T> arrayFactory() {
        return arrayFactory;
    }

    public ValueToModelMapper<T, ? extends Value> toModel() {
        return toModel;
    }

    public ValueFromModelMapper<T, ? extends Value> fromModel() {
        return fromModel;
    }

    public T toModel(io.art.value.immutable.Value value) {
        return toModel.map(value);
    }

    public Value fromModel(T model) {
        return fromModel.map(model);
    }

    public static <T> MetaType<T> metaType(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?>... parameters) {
        return MetaType.<T>builder()
                .type(cast(type))
                .arrayFactory(cast(arrayFactory))
                .parameters(immutableSetOf(parameters))
                .build();
    }

    public static <T> MetaType<T> metaType(String variable) {
        return MetaType.<T>builder()
                .variable(variable)
                .build();
    }

    public static <T> MetaType<T> metaArray(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?>... parameters) {
        return MetaType.<T>builder()
                .type(cast(type))
                .arrayFactory(cast(arrayFactory))
                .array(true)
                .parameters(immutableSetOf(parameters))
                .build();
    }

    public MetaType<T> compute() {
        if (nonNull(toModel) && nonNull(fromModel)) return this;
        MetaClass<?> metaClass = getClasses().get(type);
        if (isNull(metaClass)) {
            ValueMapper<T, Value> mapper = MetaTypeMappersComputer.compute(this);
            toModel = mapper.getToModel();
            fromModel = mapper.getFromModel();
            return this;
        }
        MetaClass<T> typedMetaClass = cast(metaClass.parameterize(parameters.toArray(new MetaType[0])));
        type = typedMetaClass.type().type;
        toModel = typedMetaClass::toModel;
        fromModel = typedMetaClass::fromModel;
        return this;
    }

    public MetaType<?> parameterize(Map<String, MetaType<?>> parameters) {
        if (isNull(variable)) {
            return this;
        }
        MetaTypeBuilder<?> builder = toBuilder().variable(null).parameters(this.parameters
                .stream()
                .map(parameter -> parameter.parameterize(parameters))
                .collect(immutableSetCollector()));
        MetaType<?> parameter = parameters.get(variable);
        if (nonNull(parameter)) {
            builder.type(cast(parameter.type))
                    .arrayFactory(cast(parameter.arrayFactory));
        }
        return builder.build();
    }
}
