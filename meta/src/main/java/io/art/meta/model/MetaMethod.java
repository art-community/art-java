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

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaMethod<T> {
    private final String name;
    private final Map<String, MetaParameter<?>> parameters;
    private MetaType<T> returnType;

    protected MetaMethod(MetaMethod<T> base) {
        this.name = base.name;
        this.returnType = base.returnType;
        this.parameters = base.parameters;
    }

    protected MetaMethod(String name, MetaType<T> returnType) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = map();
    }

    protected <P> MetaParameter<P> register(MetaParameter<P> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
    }

    protected MetaMethod<T> parameterize(Map<String, MetaType<?>> parameters) {
        MetaMethod<T> newMethod = new ParametrizedMetaMethod<>(this);
        newMethod.returnType = cast(returnType.parameterize(parameters));
        for (Map.Entry<String, MetaParameter<?>> parameter : this.parameters().entrySet()) {
            newMethod.register(parameter.getValue().parameterize(parameters));
        }
        return newMethod;
    }

    public String name() {
        return name;
    }

    public MetaType<T> returnType() {
        return returnType;
    }

    public <P> MetaParameter<P> parameter(String name) {
        return cast(parameters.get(name));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return immutableMapOf(parameters);
    }

    public abstract boolean isStatic();
}
