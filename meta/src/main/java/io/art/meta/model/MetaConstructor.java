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
import io.art.core.exception.*;
import io.art.meta.exception.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.EqualsAndHashCode.CacheStrategy.*;
import java.util.*;

@ToString
@Generation
@EqualsAndHashCode(exclude = "owner", cacheStrategy = LAZY)
public abstract class MetaConstructor<OwnerType extends MetaClass<?>, ReturnType> {
    private final MetaType<ReturnType> type;
    private final Map<String, MetaParameter<?>> parameters;
    private final OwnerType owner;
    private Boolean known;

    protected MetaConstructor(MetaType<ReturnType> type, OwnerType owner) {
        this.type = type;
        this.owner = owner;
        parameters = map();
    }

    protected <P> MetaParameter<P> register(MetaParameter<P> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
    }

    public MetaType<ReturnType> type() {
        return type;
    }

    public <P> MetaParameter<P> parameter(String name) {
        return cast(parameters.get(name));
    }

    public ImmutableMap<String, MetaParameter<?>> parameters() {
        return immutableMapOf(parameters);
    }

    public ReturnType invoke() throws Throwable {
        throw new NotImplementedException(INVOKE_WITHOUT_ARGUMENTS);
    }

    public ReturnType invoke(Object argument) throws Throwable {
        throw new NotImplementedException(INVOKE_ARGUMENT);
    }

    public abstract ReturnType invoke(Object[] arguments) throws Throwable;

    public ReturnType invokeCatched() {
        try {
            return invoke();
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public ReturnType invokeCatched(Object argument) {
        try {
            return invoke(argument);
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public ReturnType invokeCatched(Object[] arguments) {
        try {
            return invoke(arguments);
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public boolean isKnown() {
        if (nonNull(known)) return known;

        known = true;

        if (parameters.isEmpty()) return known = true;

        return known = parameters.values().stream().allMatch(parameter -> parameter.type().isKnown());
    }

    public OwnerType owner() {
        return owner;
    }
}
