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
import io.art.meta.registry.*;
import io.art.value.constants.ValueModuleConstants.ValueType.*;
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.Builder;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.TypeConstants.*;
import static io.art.meta.model.KnownMappersComputer.*;
import static io.art.meta.type.TypeInspector.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@Getter
@ToString
@ForGenerator
@EqualsAndHashCode
@Accessors(fluent = true)
@Builder(toBuilder = true)
public class MetaType<T> {
    private final Class<T> type;
    private final ImmutableSet<MetaType<?>> parameters;

    private final boolean primitive;
    private final PrimitiveType primitiveType;

    private final boolean flux;
    private final boolean mono;

    private final boolean array;
    private final MetaType<?> arrayComponentType;

    private final MetaTypeVariable variable;

    private final static Map<CacheKey, MetaType<?>> CACHE = weakMap();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<Integer, T> asArray;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ValueToModelMapper<Object, Value> toModel;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ValueFromModelMapper<Object, Value> fromModel;

    @Getter(lazy = true, value = PRIVATE)
    private final static ImmutableMap<Class<?>, MetaClass<?>> classes = MetaClassRegistry.classes();

    protected MetaType<T> compute() {
        if (nonNull(toModel) && nonNull(fromModel)) return this;

        if (nonNull(variable)) {
            toModel = cast(ValueToModelMapper.identity());
            fromModel = cast(ValueFromModelMapper.identity());
            return this;
        }

        MetaClass<?> metaClass = classes().get(type);
        if (isNull(metaClass)) {
            ValueMapper<T, Value> mapper = computeKnownMappers(this);
            toModel = cast(mapper.getToModel());
            fromModel = cast(mapper.getFromModel());
            return this;
        }

        MetaClass<T> typedMetaClass = cast(metaClass.parameterize(parameters));
        toModel = value -> typedMetaClass.schema().toModel(value);
        fromModel = value -> typedMetaClass.schema().fromModel(value);
        return this;
    }

    protected MetaType<?> parameterize(Map<String, MetaType<?>> parameters) {
        ImmutableSet<MetaType<?>> parametrizedTypeParameters = this.parameters
                .stream()
                .map(parameter -> parameter.parameterize(parameters))
                .collect(immutableSetCollector());
        MetaTypeBuilder<?> builder = toBuilder().parameters(parametrizedTypeParameters);
        if (Objects.nonNull(arrayComponentType)) {
            builder.arrayComponentType(arrayComponentType.parameterize(parameters));
        }
        if (isNull(variable)) {
            return builder.build();
        }
        MetaType<?> parameter = parameters.get(variable.name());
        if (nonNull(parameter)) {
            builder.variable(null)
                    .type(cast(parameter.type))
                    .asArray(cast(parameter.asArray));
        }
        return builder.build();
    }

    public static <T> MetaType<T> metaType(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?>... parameters) {
        return cast(putIfAbsent(CACHE, new CacheKey(type, parameters), () -> MetaType.<T>builder()
                .type(cast(type))
                .primitive(type.isPrimitive())
                .primitiveType(PRIMITIVE_TYPE_MAPPINGS.get(type))
                .flux(isFlux(type))
                .mono(isMono(type))
                .asArray(cast(arrayFactory))
                .parameters(immutableSetOf(parameters))
                .build()));
    }

    public static <T> MetaType<T> metaVariable(String name) {
        return MetaType.<T>builder().variable(new MetaTypeVariable(name)).build();
    }

    public static <T> MetaType<T> metaArray(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?> arrayComponentType) {
        return cast(putIfAbsent(CACHE, new CacheKey(type, null, arrayComponentType), () -> MetaType.<T>builder()
                .type(cast(type))
                .primitive(type.isPrimitive())
                .primitiveType(PRIMITIVE_TYPE_MAPPINGS.get(type))
                .asArray(cast(arrayFactory))
                .array(true)
                .arrayComponentType(arrayComponentType)
                .build()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @AllArgsConstructor
    private static class CacheKey {
        private final Class<?> typeClass;
        private final MetaType<?>[] parameters;
        private MetaType<?> arrayComponentType;
    }
}
