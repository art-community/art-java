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
import io.art.core.property.*;
import io.art.core.validation.*;
import io.art.meta.constants.MetaConstants.*;
import io.art.meta.transformer.*;
import lombok.Builder;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.constants.MetaConstants.ClassNames.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.ARRAY;
import static io.art.meta.constants.MetaConstants.MetaTypeModifiers.*;
import static io.art.meta.model.MetaTypeKindComputer.*;
import static io.art.meta.model.TransformersComputer.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.meta.searcher.ClassSearcher.*;
import static io.art.meta.state.MetaComputationState.*;
import static io.art.meta.validator.MetaTypeValidator.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;
import java.util.function.*;

@Generation
@EqualsAndHashCode
@Accessors(fluent = true)
public class MetaType<T> {
    @Getter
    private final Class<T> type;

    @Getter
    private final ImmutableArray<MetaType<?>> parameters;

    @Getter
    private final MetaType<?> arrayComponentType;

    @Getter
    @EqualsAndHashCode.Exclude
    private MetaTypeInternalKind internalKind;

    @Getter
    @EqualsAndHashCode.Exclude
    private MetaTypeExternalKind externalKind;

    @Getter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<Integer, T> arrayFactory;

    @Getter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final Function<String, T> enumFactory;

    @Getter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MetaTransformer<T> inputTransformer;

    @Getter
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MetaTransformer<T> outputTransformer;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final LazyProperty<MetaClass<T>> declaration;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Boolean known;

    @Builder(toBuilder = true)
    public MetaType(Class<T> type,
                    ImmutableArray<MetaType<?>> parameters,
                    MetaType<?> arrayComponentType,
                    MetaTypeInternalKind internalKind,
                    MetaTypeExternalKind externalKind,
                    Function<Integer, T> arrayFactory,
                    Function<String, T> enumFactory,
                    MetaTransformer<T> inputTransformer,
                    MetaTransformer<T> outputTransformer,
                    Boolean known) {
        this.type = type;
        this.parameters = parameters;
        this.arrayComponentType = arrayComponentType;
        this.internalKind = internalKind;
        this.externalKind = externalKind;
        this.arrayFactory = arrayFactory;
        this.enumFactory = enumFactory;
        this.inputTransformer = inputTransformer;
        this.outputTransformer = outputTransformer;
        this.declaration = lazy(() -> cast(metaModule().configuration().library().classes().get(type)));
        this.known = known;
    }

    private final static Map<CacheKey, MetaType<?>> cache = weakMap();

    private final Set<MetaTypeModifiers> modifiers = set();

    public ImmutableSet<MetaTypeModifiers> modifiers() {
        return immutableSetOf(modifiers);
    }

    public MetaClass<T> declaration() {
        return declaration.get();
    }

    public boolean isKnown() {
        if (nonNull(known)) return known;

        known = true;

        if (isNull(internalKind)) return known = false;

        if (internalKind == UNKNOWN) return known = false;

        if (internalKind == ENTITY && (isNull(declaration()) || !declaration().isKnown())) {
            return known = false;
        }

        if (nonNull(arrayComponentType) && !arrayComponentType.isKnown()) {
            return known = false;
        }

        for (MetaType<?> parameter : parameters) {
            if (!parameter.isKnown()) {
                return known = false;
            }
        }

        return known = true;
    }

    protected MetaType<T> beginComputation() {
        cache.clear();

        if (Validatable.class.isAssignableFrom(type)) {
            modifiers.add(VALIDATABLE);
        }

        if (type.isArray()) {
            modifiers.add(ARRAY);
        }

        if (!parameters.isEmpty()) {
            modifiers.add(PARAMETRIZED);
        }

        if (isAssignableFrom(type, COMMUNICATOR_NAME)) {
            modifiers.add(COMMUNICATOR);
        }

        if (isAssignableFrom(type, CONNECTOR_NAME)) {
            modifiers.add(CONNECTOR);
        }

        if (isAssignableFrom(type, TESTS_NAME)) {
            modifiers.add(TESTS);
        }

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

        if (isNull(inputTransformer) && internalKind != UNKNOWN) {
            inputTransformer = cast(computeInputTransformer(this));
        }

        if (isNull(outputTransformer) && internalKind != UNKNOWN) {
            outputTransformer = cast(computeOutputTransformer(this));
        }
    }

    public static <T> MetaType<T> metaType(Class<?> type, MetaType<?>... parameters) {
        return cast(putIfAbsent(cache, CacheKey.of(type, parameters), () -> MetaType.<T>createTypeBuilder(type)
                .type(cast(type))
                .parameters(immutableArrayOf(parameters))
                .build()));
    }


    public static <T> MetaType<T> metaEnum(Class<?> type, Function<String, T> enumFactory) {
        return cast(putIfAbsent(cache, CacheKey.of(type), () -> MetaType.<T>createTypeBuilder(type)
                .type(cast(type))
                .parameters(emptyImmutableArray())
                .enumFactory(enumFactory)
                .build()));
    }

    public static <T> MetaType<T> metaArray(Class<?> type, Function<Integer, ?> arrayFactory, MetaType<?> arrayComponentType) {
        return cast(putIfAbsent(cache, CacheKey.of(type, arrayComponentType), () -> MetaType.<T>createTypeBuilder(type)
                .type(cast(type))
                .parameters(emptyImmutableArray())
                .arrayFactory(cast(arrayFactory))
                .arrayComponentType(arrayComponentType)
                .build()));
    }

    private static <T> MetaTypeBuilder<T> createTypeBuilder(Class<?> type) {
        MetaType<T> custom = cast(metaModule().configuration().getCustomTypes().get(type));
        return let(cast(custom), MetaType<T>::toBuilder, MetaType.<T>builder());
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

    @Override
    public String toString() {
        String ownerName = type.getSimpleName();
        if (!parameters.isEmpty()) {
            ownerName += parameters.stream().map(MetaType::toString).collect(joining(COMMA, LESS, MORE));
        }
        return ownerName;
    }
}
