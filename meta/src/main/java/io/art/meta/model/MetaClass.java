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
import io.art.meta.exception.*;
import io.art.meta.registry.*;
import io.art.value.builder.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapping.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.TypeConstants.*;
import static io.art.value.immutable.Entity.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.util.*;
import java.util.Map.*;

@ToString
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

    private MetaConstructor<T> allArgumentsConstructors = null;
    private List<MetaProperty<?>> getters;

    protected MetaClass(MetaType<T> type) {
        this.type = type;
        constructors = set();
        fields = map();
        properties = map();
        methods = set();
        variables = map();
        MetaClassRegistry.register(this);
    }

    protected MetaClass(MetaType<T> type, MetaType<?> parent) {
        this.type = type;
        this.parent = parent;
        constructors = set();
        fields = map();
        properties = map();
        methods = set();
        variables = map();
        MetaClassRegistry.register(this);
    }

    protected MetaClass(MetaClass<T> base) {
        type = base.type;
        parent = base.parent;
        constructors = base.constructors;
        fields = base.fields;
        properties = base.properties;
        methods = base.methods;
        variables = base.variables;
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

    protected MetaType<?> register(MetaType<?> variable) {
        return cast(putIfAbsent(variables, variable.variable(), () -> variable));
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
        getters = properties()
                .values()
                .stream()
                .filter(property -> nonNull(property.getter()))
                .collect(listCollector());
    }

    protected MetaClass<T> parameterize(ImmutableSet<MetaType<?>> parameters) {
        if (isEmpty(variables) || isEmpty(parameters)) return this;
        MetaClass<T> parametrized = new ParametrizedMetaClass<>(this);
        Map<String, MetaType<?>> variableToParameter = map();
        int index = 0;
        for (MetaType<?> parameter : parameters) {
            int variableIndex = 0;
            for (Entry<String, MetaType<?>> variable : variables.entrySet()) {
                if (variableIndex == index) {
                    variableToParameter.put(variable.getKey(), parameter);
                }
                variableIndex++;
            }
        }
        if (isEmpty(variableToParameter)) return this;
        for (MetaField<?> field : fields.values()) {
            parametrized.fields.put(field.name(), field.parameterize(variableToParameter));
        }
        for (MetaConstructor<T> constructor : constructors) {
            parametrized.constructors.add(constructor.parameterize(variableToParameter));
        }
        for (MetaMethod<?> method : methods) {
            parametrized.methods.add(method.parameterize(variableToParameter));
        }
        return parametrized;
    }

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
        if (!isEntity(value)) {
            throw new MetaException(format(UNSUPPORTED_TYPE, type));
        }
        Entity entity = asEntity(value);
        EntityMapping mapping = entity.mapping();
        Object[] constructorArguments = new Object[properties.size()];
        int index = 0;
        for (Entry<String, MetaProperty<?>> property : properties.entrySet()) {
            String propertyKey = property.getKey();
            MetaProperty<?> propertyValue = property.getValue();
            MetaType<?> type = propertyValue.type();
            if (type.primitive()) {
                constructorArguments[index] = mapping.mapOrDefault(propertyKey, PRIMITIVE_TYPE_MAPPINGS.get(type), type::toModel);
                index++;
                continue;
            }
            constructorArguments[index] = mapping.map(propertyKey, type::toModel);
            index++;
        }
        return allArgumentsConstructors.invoke(constructorArguments);
    }

    public Value fromModel(Object model) {
        EntityBuilder entityBuilder = entityBuilder();
        for (MetaProperty<?> property : getters) {
            InstanceMetaMethod<Object, ?> getter = property.getter();
            entityBuilder.put(property.name(), getter.invoke(model), getter.returnType()::fromModel);
        }
        return entityBuilder.build();
    }

}
