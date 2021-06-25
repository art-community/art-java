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
import io.art.meta.registry.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.type.TypeInspector.*;
import java.util.*;

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaClass<T> {
    private final MetaType<T> type;
    private final Set<MetaConstructor<T>> constructors;
    private final Map<String, MetaField<?>> fields;
    private final Set<MetaMethod<?>> methods;
    private final Map<Class<?>, MetaClass<?>> classes;
    private final Map<String, MetaType<?>> variables;
    //private MetaSchema<T> schema;

    protected MetaClass(MetaType<T> type) {
        this.type = type;
        constructors = set();
        fields = map();
        methods = set();
        variables = map();
        classes = map();
        MetaClassRegistry.register(this);
    }

    protected <F> MetaField<F> register(MetaField<F> field) {
        return cast(putIfAbsent(fields, field.name(), () -> field));
    }

    protected <M extends MetaMethod<?>> M register(M method) {
        return cast(putIfAbsent(methods, method));
    }

    protected <C extends MetaConstructor<T>> C register(C constructor) {
        return cast(putIfAbsent(constructors, constructor));
    }

    protected <C extends MetaClass<?>> C register(C metaClass) {
        classes.put(metaClass.type().type(), metaClass);
        return metaClass;
    }

    protected void compute() {
        type.compute();

        for (MetaField<?> field : fields.values()) {
            field.type().compute();
        }

        for (MetaConstructor<T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().compute());
        }

        for (MetaMethod<?> method : methods) {
            method.returnType().compute();
            method.parameters().values().forEach(parameter -> parameter.type().compute());
        }

        for (MetaClass<?> nested : classes.values()) {
            nested.compute();
        }

//        schema = computeSchema();
    }

    private boolean isGetter(MetaField<?> field, MetaMethod<?> method) {
        if (method.isStatic()) return false;
        if (!method.parameters().isEmpty()) return false;
        if (!method.returnType().equals(field.type())) return false;
        if (method.name().equals(GET_NAME + capitalize(field.name()))) return true;
        if (method.name().equals(field.name())) return true;
        return isBoolean(field.type().type()) && method.name().equals(IS_NAME + capitalize(field.name()));
    }

    public MetaType<T> type() {
        return type;
    }

    public MetaType<?> variable(String name) {
        return variables.get(name);
    }

    public ImmutableMap<String, MetaType<?>> variables() {
        return immutableMapOf(variables);
    }

    public <F> MetaField<F> field(String name) {
        return cast(fields.get(name));
    }

    public ImmutableMap<String, MetaField<?>> fields() {
        return immutableMapOf(fields);
    }

    public ImmutableSet<MetaMethod<?>> methods() {
        return immutableSetOf(methods);
    }

    public ImmutableSet<MetaConstructor<T>> constructors() {
        return immutableSetOf(constructors);
    }

    public ImmutableMap<Class<?>, MetaClass<?>> classes() {
        return immutableMapOf(classes);
    }
}
