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
import io.art.meta.type.*;
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

    private final boolean isPrimitive;
    private final boolean isFlux;
    private final boolean isMono;
    private final boolean isEnum;
    private final boolean isArray;

    private final PrimitiveType primitiveType;
    private final MetaType<?> arrayComponentType;

    private final static Map<CacheKey, MetaType<?>> CACHE = weakMap();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<Integer, T> arrayFactory;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<String, T> enumFactory;

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

        MetaClass<?> metaClass = classes().get(type);
        if (isNull(metaClass)) {
            ValueMapper<T, Value> mapper = computeKnownMappers(this);
            toModel = cast(mapper.getToModel());
            fromModel = cast(mapper.getFromModel());
            return this;
        }

        MetaClass<T> typedMetaClass = cast(metaClass);
        toModel = value -> typedMetaClass.schema().toModel(value);
        fromModel = value -> typedMetaClass.schema().fromModel(value);
        return this;
    }

    public static <T> MetaType<T> metaType(Class<?> type, MetaType<?>... parameters) {
        return cast(putIfAbsent(CACHE, CacheKey.of(type, parameters), () -> MetaType.<T>builder()
                .type(cast(type))
                .isPrimitive(type.isPrimitive())
                .primitiveType(PRIMITIVE_TYPE_MAPPINGS.get(type))
                .isFlux(TypeInspector.isFlux(type))
                .isMono(TypeInspector.isMono(type))
                .parameters(immutableSetOf(parameters))
                .build()));
    }

    public static <T> MetaType<T> metaEnum(Class<?> type, Function<String, T> enumFactory) {
        return cast(putIfAbsent(CACHE, CacheKey.of(type), () -> MetaType.<T>builder()
                .type(cast(type))
                .isEnum(true)
                .enumFactory(enumFactory)
                .build()));
    }

    public static <T> MetaType<T> metaArray(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?> arrayComponentType) {
        return cast(putIfAbsent(CACHE, CacheKey.of(type, arrayComponentType), () -> MetaType.<T>builder()
                .type(cast(type))
                .isArray(true)
                .arrayFactory(cast(arrayFactory))
                .arrayComponentType(arrayComponentType)
                .build()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    private static class CacheKey {
        private final Class<?> typeClass;
        private MetaType<?>[] parameters;
        private MetaType<?> arrayComponentType;

        public static CacheKey of(Class<?> typeClass) {
            return new CacheKey(typeClass);
        }

        public static CacheKey of(Class<?> typeClass, MetaType<?>[] parameters) {
            CacheKey cacheKey = new CacheKey(typeClass);
            cacheKey.parameters = parameters;
            return cacheKey;
        }

        public static CacheKey of(Class<?> typeClass, MetaType<?>[] parameters, MetaType<?> arrayComponentType) {
            CacheKey cacheKey = new CacheKey(typeClass);
            cacheKey.parameters = parameters;
            cacheKey.arrayComponentType = arrayComponentType;
            return cacheKey;
        }

        public static CacheKey of(Class<?> typeClass, MetaType<?> arrayComponentType) {
            CacheKey cacheKey = new CacheKey(typeClass);
            cacheKey.arrayComponentType = arrayComponentType;
            return cacheKey;
        }
    }
}
