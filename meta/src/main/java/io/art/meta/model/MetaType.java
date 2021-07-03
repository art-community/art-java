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
import io.art.meta.constants.MetaConstants.*;
import io.art.meta.transformer.*;
import lombok.Builder;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.computer.MetaTypeKindComputer.*;
import static io.art.meta.computer.TransformersComputer.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.meta.state.MetaComputationState.*;
import static io.art.meta.validator.MetaTypeValidator.*;
import static java.util.Objects.*;
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
    private final ImmutableArray<MetaType<?>> parameters;
    private final MetaType<?> arrayComponentType;
    private MetaTypeInternalKind internalKind;
    private MetaTypeExternalKind externalKind;

    private final static Map<CacheKey, MetaType<?>> CACHE = weakMap();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<Integer, T> arrayFactory;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<String, T> enumFactory;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MetaTransformer<T> inputTransformer;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MetaTransformer<T> outputTransformer;

    @ToString.Exclude
    @Getter(lazy = true)
    @Accessors(fluent = true)
    @EqualsAndHashCode.Exclude
    private final MetaClass<?> declaration = classes().get(type);

    protected MetaType<T> beginComputation() {
        CACHE.clear();

        if (nonNull(arrayComponentType)) {
            rememberValidation(arrayComponentType, validate(arrayComponentType.beginComputation()));
        }
        for (MetaType<?> parameter : parameters) {
            rememberValidation(parameter, validate(parameter.beginComputation()));
        }
        if (isNull(internalKind)) {
            internalKind = computeInternalKind(this);
            rememberValidation(this, validate(this));
        }
        return this;
    }

    protected void completeComputation() {
        if (nonNull(arrayComponentType)) {
            arrayComponentType.completeComputation();
        }
        for (MetaType<?> parameter : parameters) {
            parameter.completeComputation();
        }
        if (isNull(externalKind)) {
            externalKind = computeExternalKind(this);
        }
        if (isNull(inputTransformer)) {
            inputTransformer = cast(computeInputTransformer(this));
        }
        if (isNull(outputTransformer)) {
            outputTransformer = cast(computeOutputTransformer(this));
        }
    }

    public static <T> MetaType<T> metaType(Class<?> type, MetaType<?>... parameters) {
        return cast(putIfAbsent(CACHE, CacheKey.of(type, parameters), () -> MetaType.<T>builder()
                .type(cast(type))
                .parameters(immutableArrayOf(parameters))
                .build()));
    }

    public static <T> MetaType<T> metaEnum(Class<?> type, Function<String, T> enumFactory) {
        return cast(putIfAbsent(CACHE, CacheKey.of(type), () -> MetaType.<T>builder()
                .type(cast(type))
                .parameters(emptyImmutableArray())
                .enumFactory(enumFactory)
                .build()));
    }

    public static <T> MetaType<T> metaArray(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?> arrayComponentType) {
        return cast(putIfAbsent(CACHE, CacheKey.of(type, arrayComponentType), () -> MetaType.<T>builder()
                .type(cast(type))
                .parameters(emptyImmutableArray())
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
