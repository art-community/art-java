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
import io.art.core.exception.*;
import io.art.meta.exception.*;
import lombok.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static java.text.MessageFormat.*;
import static lombok.EqualsAndHashCode.CacheStrategy.LAZY;

@Generation
@EqualsAndHashCode(callSuper = true)
public abstract class StaticMetaMethod<T> extends MetaMethod<T> {

    protected StaticMetaMethod(String name, MetaType<?> returnType, MetaClass<?> owner) {
        super(name, returnType, owner);
    }

    public Object invoke() throws Throwable {
        throw new NotImplementedException(INVOKE_WITHOUT_ARGUMENTS);
    }

    public Object invoke(Object argument) throws Throwable {
        throw new NotImplementedException(INVOKE_ARGUMENT);
    }

    public abstract Object invoke(Object[] arguments) throws Throwable;


    public Object invokeCatched() {
        try {
            return invoke();
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public Object invokeCatched(Object argument) {
        try {
            return invoke(argument);
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    public Object invokeCatched(Object[] arguments) {
        try {
            return invoke(arguments);
        } catch (Throwable throwable) {
            throw new MetaException(format(INVOCATION_ERROR, toString(), throwable.getMessage()), throwable);
        }
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
