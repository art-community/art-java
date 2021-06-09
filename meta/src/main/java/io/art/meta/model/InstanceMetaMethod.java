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
import io.art.core.exception.*;
import lombok.*;
import java.util.*;

@ForGenerator
@EqualsAndHashCode(callSuper = true)
public abstract class InstanceMetaMethod<I, R> extends MetaMethod<R> {
    protected InstanceMetaMethod(String name, MetaType<R> returnType, boolean isPublic) {
        super(name, returnType, isPublic);
    }

    public Object invoke(I instance) throws Throwable {
        throw new NotImplementedException("invoke(instance)");
    }

    public Object invoke(I instance, Object argument) throws Throwable {
        throw new NotImplementedException("invoke(instance, argument)");
    }

    public abstract Object invoke(I instance, Object[] arguments) throws Throwable;

    @Override
    public boolean isStatic() {
        return false;
    }
}
