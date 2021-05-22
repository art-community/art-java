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
import io.art.core.singleton.*;
import lombok.*;
import java.util.function.*;

@ForGenerator
@EqualsAndHashCode
public class MetaInstance<T> {
    private final MetaClass<T> metaClass;
    private final Function<Object[], T> instance;

    public MetaInstance(MetaClass<T> metaClass, Function<Object[], T> constructor) {
        this.metaClass = metaClass;
        this.instance = constructor;
    }

    public T instance(Object... arguments) {
        return instance.apply(arguments);
    }

    public T singleton(Object... arguments) {
        return SingletonsRegistry.singleton(metaClass.type().type(), () -> instance.apply(arguments));
    }
}
