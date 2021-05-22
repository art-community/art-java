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
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@ForGenerator
@EqualsAndHashCode
public abstract class MetaMethod<R> {
    private MetaType<?> returnType;
    private final String name;
    private final Map<String, MetaParameter<?>> parameters;

    protected MetaMethod(MetaMethod<R> base) {
        this.name = base.name;
        this.returnType = base.returnType;
        this.parameters = base.parameters;
    }

    protected MetaMethod(String name, MetaType<R> returnType) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = map();
    }

    protected <T> MetaParameter<T> register(MetaParameter<T> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
    }

    protected abstract MetaMethod<R> duplicate();

    protected MetaMethod<R> parameterize(Map<String, MetaType<?>> parameters) {
        MetaMethod<R> newMethod = duplicate();
        newMethod.returnType = returnType.parameterize(parameters);
        for (Map.Entry<String, MetaParameter<?>> parameter : this.parameters().entrySet()) {
            MetaType<?> newParameterType = parameter.getValue().type().parameterize(parameters);
            newMethod.register(new MetaParameter<>(parameter.getKey(), newParameterType));
        }
        return newMethod;
    }

    public String name() {
        return name;
    }

    public MetaType<R> returnType() {
        return cast(returnType);
    }

    public <T> MetaParameter<T> parameter(String name) {
        return cast(parameters.get(name));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return immutableMapOf(parameters);
    }
}
