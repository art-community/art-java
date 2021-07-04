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
import io.art.meta.exception.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
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

    protected <P> MetaParameter<P> register(MetaParameter<P> parameter) {
        parameters.put(parameter.name(), parameter);
        return parameter;
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

    public T invoke() throws Throwable {
        throw new NotImplementedException(INVOKE_WITHOUT_ARGUMENTS);
    }

    public T invoke(Object argument) throws Throwable {
        throw new NotImplementedException(INVOKE_ARGUMENT);
    }

    public abstract T invoke(Object[] arguments) throws Throwable;

    public T invokeCatched() {
        try {
            return invoke();
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public T invokeCatched(Object argument) {
        try {
            return invoke(argument);
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public T invokeCatched(Object[] arguments) {
        try {
            return invoke(arguments);
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }
}
