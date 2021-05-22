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
import io.art.value.immutable.Value;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.model.MetaType.*;
import java.util.*;
import java.util.Map.*;
import java.util.function.*;

@ForGenerator
@EqualsAndHashCode
public abstract class MetaClass<T> {
    private final MetaType<T> type;
    private final Set<MetaConstructor<T>> constructors;
    private final Map<String, MetaField<?>> fields;
    private final Map<String, MetaProperty<?>> properties;
    private final Set<MetaMethod<?>> methods;
    private final Map<String, MetaType<?>> variables;
    private MetaType<?> parent;

    protected MetaClass(Class<?> type, Function<Integer, ?> arrayFactory) {
        this.type = metaType(type);
        this.arrayFactory = cast(arrayFactory);
        constructors = set();
        fields = map();
        properties = map();
        methods = set();
        variables = map();
        MetaClassRegistry.register(this);
    }

    protected MetaClass(Class<T> type, Function<Integer, T> arrayFactory, MetaType<?> parent) {
        this.type = metaType(type);
        this.parent = parent;
        this.arrayFactory = arrayFactory;
        constructors = set();
        fields = map();
        properties = map();
        methods = set();
        variables = map();
        MetaClassRegistry.register(this);
    }

    protected MetaClass(MetaClass<T> base) {
        type = base.type;
        arrayFactory = base.arrayFactory;
        parent = base.parent;
        constructors = base.constructors;
        fields = base.fields;
        properties = base.properties;
        methods = base.methods;
        variables = base.variables;
        MetaClassRegistry.register(this);
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

    protected MetaType<?> register(String name, MetaType<?> variable) {
        variables.put(name, variable);
        return variable;
    }

    protected MetaClass<T> parameterize(MetaType<?>... parameters) {
        if (isEmpty(variables)) return this;
        MetaClass<T> newMetaClass = duplicate();
        Map<String, MetaType<?>> variableToParameter = map();
        for (int index = 0; index < parameters.length; index++) {
            int variableIndex = 0;
            for (Entry<String, MetaType<?>> variable : variables.entrySet()) {
                if (variableIndex == index) {
                    variableToParameter.put(variable.getKey(), parameters[index]);
                }
                variableIndex++;
            }
        }

        if (isEmpty(variableToParameter)) return this;
        for (MetaField<?> field : fields.values()) {
            newMetaClass.fields.put(field.name(), field.parameterize(variableToParameter));
        }
        for (MetaConstructor<T> constructor : constructors) {
            newMetaClass.constructors.add(constructor.parameterize(variableToParameter));
        }
        for (MetaMethod<?> method : methods) {
            newMetaClass.methods.add(method.parameterize(variableToParameter));
        }
        return newMetaClass;
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
    }

    protected abstract MetaClass<T> duplicate();

    public MetaType<T> type() {
        return type;
    }

    public MetaType<?> variable(String name) {
        return variables.get(name);
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

    public ImmutableMap<String, MetaType<?>> variables() {
        return immutableMapOf(variables);
    }

    public T toModel(Value value) {
        return null;
    }

    public Value fromModel(T model) {
        return null;
    }

}
