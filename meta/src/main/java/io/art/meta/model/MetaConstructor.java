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

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaConstructor<T> {
    private final MetaType<T> type;
    private final Map<String, MetaParameter<?>> parameters;

    protected MetaConstructor(MetaType<T> type) {
        this.type = type;
        parameters = map();
    }

    protected MetaConstructor(MetaConstructor<T> base) {
        this.type = base.type;
        this.parameters = base.parameters;
    }

    protected <P> MetaParameter<P> register(MetaParameter<P> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
    }

    protected MetaConstructor<T> parameterize(Map<String, MetaType<?>> parameters) {
        MetaConstructor<T> newConstructor = new ParametrizedMetaConstructor<>(this);
        for (Map.Entry<String, MetaParameter<?>> parameter : this.parameters().entrySet()) {
            MetaType<?> newParameterType = parameter.getValue().type().parameterize(parameters);
            newConstructor.register(new MetaParameter<>(parameter.getKey(), newParameterType));
        }
        return newConstructor;
    }

    public MetaType<T> type() {
        return type;
    }

    public <P> MetaParameter<P> parameter(String name) {
        return cast(parameters.get(name));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return immutableMapOf(parameters);
    }

    public T invoke() {
        throw new NotImplementedException("invoke()");
    }

    public T invoke(Object argument) {
        throw new NotImplementedException("invoke(argument)");
    }

    public abstract T invoke(Object... arguments);
}
