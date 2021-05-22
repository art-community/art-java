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
import io.art.core.exception.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;

@ForGenerator
@EqualsAndHashCode
public abstract class MetaConstructor<C> {
    private final MetaType<C> type;
    private final Map<String, MetaParameter<?>> parameters;

    protected MetaConstructor(MetaType<C> type) {
        this.type = type;
        parameters = map();
    }

    protected MetaConstructor(MetaConstructor<C> base) {
        this.type = base.type;
        this.parameters = base.parameters;
    }

    protected <T> MetaParameter<T> register(MetaParameter<T> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
    }

    protected MetaConstructor<C> parameterize(Map<String, MetaType<?>> parameters) {
        MetaConstructor<C> newConstructor = duplicate();
        for (Map.Entry<String, MetaParameter<?>> parameter : this.parameters().entrySet()) {
            MetaType<?> newParameterType = parameter.getValue().type().parameterize(parameters);
            newConstructor.register(new MetaParameter<>(parameter.getKey(), newParameterType));
        }
        return newConstructor;
    }

    protected abstract MetaConstructor<C> duplicate();

    public MetaType<C> type() {
        return type;
    }

    public <T> MetaParameter<T> parameter(String name) {
        return cast(parameters.get(name));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return immutableMapOf(parameters);
    }

    public C invoke() {
        throw new NotImplementedException("invoke()");
    }

    public C invoke(Object argument) {
        throw new NotImplementedException("invoke(argument)");
    }

    public abstract C invoke(Object... arguments);
}
