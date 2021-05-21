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

package io.art.meta;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import lombok.*;
import static io.art.core.caster.Caster.*;

@ForGenerator
@EqualsAndHashCode
public class MetaMethod {
    private final String name;
    private final MetaType<?> returnType;
    private final ImmutableMap<String, MetaParameter<?>> parameters;

    public MetaMethod(String name, Class<?> returnType, ImmutableMap<String, MetaParameter<?>> parameters) {
        this.name = name;
        this.returnType = new MetaType<>(returnType);
        this.parameters = parameters;
    }

    public String name() {
        return name;
    }

    public <T> MetaType<T> returnType() {
        return cast(returnType);
    }

    public <T> MetaParameter<T> parameter(String name) {
        return cast(parameters.get(name));
    }

    public <T> MetaType<T> returnType(Class<T> type) {
        return cast(returnType.reify(type));
    }

    public <T> MetaParameter<T> parameter(String name, Class<T> type) {
        return cast(parameters.get(name).reify(type));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return parameters;
    }
}
