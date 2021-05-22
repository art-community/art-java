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
import io.art.value.immutable.Value;
import io.art.value.mapper.*;
import lombok.*;
import static io.art.core.caster.Caster.*;

@ForGenerator
@EqualsAndHashCode
public class MetaType<T> {
    private final Class<T> type;
    private final ValueToModelMapper<T, Value> toModel;
    private final ValueFromModelMapper<T, Value> fromModel;

    public MetaType(Class<?> type, ValueToModelMapper<?, ? extends Value> toModel, ValueFromModelMapper<?, ? extends Value> fromModel) {
        this.type = cast(type);
        this.toModel = cast(toModel);
        this.fromModel = cast(fromModel);
    }

    public MetaType(Class<?> type, MetaClass<T> metaClass) {
        this.type = cast(type);
        toModel = metaClass::toModel;
        fromModel = metaClass::fromModel;
    }

    public Class<T> type() {
        return type;
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
}
