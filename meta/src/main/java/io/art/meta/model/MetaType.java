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
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;

@Builder
@ForGenerator
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MetaType<T> {
    @EqualsAndHashCode.Include
    private final Class<T> type;

    @EqualsAndHashCode.Include
    private final ImmutableSet<MetaType<?>> parameters;

    @EqualsAndHashCode.Include
    private final MetaType<?> arrayType;

    private final ValueToModelMapper<T, Value> toModel;
    private final ValueFromModelMapper<T, Value> fromModel;

    public Class<T> type() {
        return type;
    }

    public ImmutableSet<MetaType<?>> parameters() {
        return parameters;
    }

    public MetaType<?> arrayType() {
        return arrayType;
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

    public static <T> MetaType<T> metaType(Class<T> type, ValueToModelMapper<?, ? extends Value> toModel, ValueFromModelMapper<?, ? extends Value> fromModel) {
        return metaType(type)
                .toModel(cast(toModel))
                .fromModel(cast(fromModel))
                .build();
    }

    public static <T> MetaTypeBuilder<T> metaType(Class<T> type) {
        return MetaType.<T>builder().type(type);
    }
}
