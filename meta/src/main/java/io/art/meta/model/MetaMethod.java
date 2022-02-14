/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import java.util.*;

@Generation
@EqualsAndHashCode
public abstract class MetaMethod<T> {
    private final String name;
    private final Map<String, MetaParameter<?>> parameters;
    private final MetaType<T> returnType;
    private final MetaClass<?> owner;
    private Boolean known;

    protected MetaMethod(String name, MetaType<?> returnType, MetaClass<?> owner) {
        this.name = name;
        this.owner = owner;
        this.returnType = cast(returnType);
        this.parameters = map();
    }

    protected <P> MetaParameter<P> register(MetaParameter<P> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
    }

    public String name() {
        return name;
    }

    public MetaType<T> returnType() {
        return returnType;
    }

    public MetaClass<?> owner() {
        return owner;
    }

    public <P> MetaParameter<P> parameter(String name) {
        return cast(parameters.get(name));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return immutableMapOf(parameters);
    }

    public abstract boolean isStatic();

    @Override
    public String toString() {
        String parameters = this.parameters
                .values()
                .stream()
                .map(parameter -> parameter.type() + SPACE + parameter.name())
                .collect(joining(COMMA));
        return format(METHOD_FORMAT, returnType, name, parameters);
    }

    public boolean isKnown() {
        if (nonNull(known)) return known;

        known = true;

        if (!returnType.isKnown()) return known = false;

        if (parameters.isEmpty()) return known = true;

        return known = parameters.values().stream().allMatch(parameter -> parameter.type().isKnown());
    }
}
