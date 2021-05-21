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
import io.art.core.collection.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;

@ForGenerator
@EqualsAndHashCode
public abstract class MetaClass {
    private final MetaType<?> type;
    private final Map<String, MetaField<?>> fields = map();
    private final Set<MetaMethod> methods = set();

    protected MetaClass(Class<?> type) {
        this.type = cast(new MetaType<>(type));
    }

    protected <T> MetaField<T> register(MetaField<T> field) {
        fields.put(field.name(), field);
        return field;
    }

    protected MetaMethod register(MetaMethod method) {
        methods.add(method);
        return method;
    }

    public <T> MetaType<T> type() {
        return cast(type);
    }

    public ImmutableMap<String, MetaField<?>> fields() {
        return immutableMapOf(fields);
    }

    public ImmutableSet<MetaMethod> methods() {
        return immutableSetOf(methods);
    }
}
