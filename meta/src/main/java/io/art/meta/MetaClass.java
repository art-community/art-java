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
import io.art.value.immutable.Value;
import lombok.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;

@ForGenerator
@EqualsAndHashCode
public abstract class MetaClass<T> {
    private final MetaType<T> type;
    private final Set<MetaConstructor<T>> constructors = set();
    private final Map<String, MetaField<?>> fields = map();
    private final Map<String, MetaProperty<?>> properties = map();
    private final Set<MetaMethod<?>> methods = set();
    private MetaType<?> parent;

    protected MetaClass(Class<T> type) {
        this.type = new MetaType<>(type, this);
    }

    protected MetaClass(Class<T> type, MetaType<?> parent) {
        this.type = new MetaType<>(type, this);
        this.parent = parent;
    }

    protected <F> MetaField<F> register(MetaField<F> field) {
        fields.put(field.name(), field);
        return field;
    }

    protected <M extends MetaMethod<?>> M register(M method) {
        methods.add(method);
        return method;
    }

    protected <C extends MetaConstructor<T>> C register(C constructor) {
        constructors.add(constructor);
        return constructor;
    }

    public MetaType<T> type() {
        return type;
    }

    public ImmutableMap<String, MetaField<?>> fields() {
        return immutableMapOf(fields);
    }


    public ImmutableMap<String, MetaProperty<?>> properties() {
        return immutableMapOf(properties);
    }

    public ImmutableSet<MetaMethod<?>> methods() {
        return immutableSetOf(methods);
    }

    public ImmutableSet<MetaConstructor<T>> constructors() {
        return immutableSetOf(constructors);
    }

    public T toModel(Value value) {
        return null;
    }

    public Value fromModel(T model) {
        return null;
    }
}
