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
import io.art.meta.model.MetaProperty.*;
import io.art.meta.registry.*;
import io.art.value.builder.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapping.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.value.immutable.Entity.*;
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
    private final Set<MetaMethod<?>> methods;
    protected final Map<String, MetaType<?>> variables;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private MetaConstructor<T> allArgumentsConstructor;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final List<MetaProperty<?>> gettableProperties;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final List<MetaProperty<?>> objectProperties;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private final List<MetaProperty<?>> primitiveProperties;

    protected MetaClass(MetaType<T> type) {
        this.type = type;
        constructors = set();
        fields = map();
        methods = set();
        variables = map();
        gettableProperties = linkedList();
        objectProperties = linkedList();
        primitiveProperties = linkedList();
        MetaClassRegistry.register(this);
    }

    protected MetaClass(MetaClass<T> base) {
        type = base.type;
        constructors = base.constructors;
        fields = base.fields;
        methods = base.methods;
        variables = base.variables;
        allArgumentsConstructor = base.allArgumentsConstructor;
        gettableProperties = base.gettableProperties;
        objectProperties = base.objectProperties;
        primitiveProperties = base.primitiveProperties;
    }

    @SafeVarargs
    protected MetaClass(MetaType<T> metaType, MetaType<T>... variables) {
        this(metaType);
        for (MetaType<T> variable : variables) {
            this.variables.put(variable.variable(), variable);
        }
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

        for (MetaConstructor<T> constructor : constructors) {
            MetaField<?>[] fields = this.fields.values().toArray(new MetaField[0]);
            MetaParameter<?>[] parameters = constructor.parameters().values().toArray(new MetaParameter[0]);
            if (fields.length != parameters.length) continue;
            for (int index = 0; index < fields.length; index++) {
                if (parameters[index].name().equals(fields[index].name()) && parameters[index].type().equals(fields[index].type())) {
                    allArgumentsConstructor = constructor;
                    break;
                }
            }
        }

        for (MetaField<?> field : fields.values()) {
            MetaPropertyBuilder<?> builder = MetaProperty.builder()
                    .name(field.name())
                    .type(cast(field.type()));

            Optional<MetaMethod<?>> getter = methods
                    .stream()
                    .filter(method -> !method.isStatic())
                    .filter(method -> method.name().equals(GET_NAME + capitalize(field.name())))
                    .filter(method -> method.parameters().isEmpty())
                    .filter(method -> method.returnType().equals(field.type()))
                    .findFirst();

            getter.ifPresent(metaMethod -> builder.getter(cast(metaMethod)));

            if (nonNull(allArgumentsConstructor)) {
                int constructorParameterIndex = allArgumentsConstructor.parameters().get(field.name()).index();
                builder.index(constructorParameterIndex);

                MetaProperty<?> property = builder.build();

                if (getter.isPresent()) {
                    gettableProperties.add(property);
                }

                if (property.type().primitive()) {
                    primitiveProperties.add(property);
                    continue;
                }

                objectProperties.add(property);
                continue;
            }

            if (getter.isPresent()) {
                gettableProperties.add(builder.build());
            }
        }
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
                    break;
                }
                variableIndex++;
            }
            index++;
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

        parametrized.variables.clear();

        return parametrized;
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


    public T toModel(Value value) {
        Entity entity = Value.asEntity(value);
        EntityMapping mapping = entity.mapping();

        Object[] arguments = new Object[objectProperties.size() + primitiveProperties.size()];

        for (MetaProperty<?> property : primitiveProperties) {
            String name = property.name();
            MetaType<?> type = property.type();
            arguments[property.index()] = mapping.mapOrDefault(name, type.primitiveType(), type::toModel);
        }

        for (MetaProperty<?> property : objectProperties) {
            String name = property.name();
            MetaType<?> type = property.type();
            arguments[property.index()] = mapping.map(name, type::toModel);
        }

        return allArgumentsConstructor.invoke(arguments);
    }

    public Value fromModel(Object model) {
        EntityBuilder entityBuilder = entityBuilder();
        for (MetaProperty<?> property : gettableProperties) {
            InstanceMetaMethod<Object, ?> getter = property.getter();
            entityBuilder.put(property.name(), getter.invoke(model), getter.returnType()::fromModel);
        }
        return entityBuilder.build();
    }
}
