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
import io.art.core.exception.*;
import lombok.*;

@ForGenerator
@EqualsAndHashCode(callSuper = true)
public abstract class StaticMetaMethod<R> extends MetaMethod<R> {
    protected StaticMetaMethod(String name, Class<R> returnType) {
        super(name, returnType);
    }

    public R invoke() {
        throw new NotImplementedException("");
    }

    public R invoke(Object argument) {
        throw new NotImplementedException("");
    }

    public abstract R invoke(Object... arguments);
}
