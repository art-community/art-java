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
import io.art.value.builder.*;
import io.art.value.immutable.Value;
import io.art.value.immutable.*;
import io.art.value.mapping.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.TypeConstants.*;
import static io.art.meta.type.TypeInspector.*;
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
    private final Map<String, MetaProperty<?>> properties;
    private final Set<MetaMethod<?>> methods;
    private final Map<String, MetaType<?>> variables;

    private MetaConstructor<T> allArgumentsConstructors;
    private List<MetaProperty<?>> gettableProperties;
    private List<MetaProperty<?>> settableProperties;
    private List<MetaProperty<?>> constructableProperties;

    protected MetaClass(MetaType<T> type) {
        this.type = type;
        constructors = set();
        fields = map();
        properties = map();
        methods = set();
        variables = map();
        MetaClassRegistry.register(this);
    }

    protected MetaClass(MetaClass<T> base) {
        type = base.type;
        constructors = base.constructors;
        fields = base.fields;
        properties = base.properties;
        methods = base.methods;
        variables = base.variables;
        allArgumentsConstructors = base.allArgumentsConstructors;
        gettableProperties = base.gettableProperties;
        settableProperties = base.settableProperties;
        constructableProperties = base.constructableProperties;
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
            int parameterIndex = 0;
            for (MetaParameter<?> parameter : constructor.parameters().values()) {
                for (MetaField<?> field : fields.values()) {
                    if (!field.type().equals(parameter.type()) && !field.name().equals(parameter.name())) {
                        break;
                    }
                }
                parameterIndex++;
            }
            if (parameterIndex == constructor.parameters().size()) {
                allArgumentsConstructors = constructor;
                break;
            }
        }

        Map<String, MetaMethod<?>> getters = map();
        Map<String, MetaMethod<?>> setters = map();
        Map<String, List<MetaConstructor<?>>> constructors = map();

        for (Entry<String, MetaField<?>> entry : fields.entrySet()) {
            String fieldName = entry.getKey();
            String fieldCapitalizedName = capitalize(entry.getKey());
            MetaField<?> fieldValue = entry.getValue();
            Class<?> fieldType = fieldValue.type().type();

            for (MetaMethod<?> method : methods) {
                if (method.isStatic()) continue;

                String methodName = method.name();
                MetaParameter<?>[] methodParameters = method.parameters().values().toArray(MetaParameter[]::new);
                MetaType<?> methodReturnType = method.returnType();

                boolean hasSetter = methodName.equals(SET_NAME + fieldCapitalizedName)
                        && methodParameters.length == 1
                        && methodParameters[0].type().equals(fieldValue.type());

                if (hasSetter) {
                    setters.put(fieldName, method);
                }

                if (methodParameters.length != 0) continue;

                if (!fieldType.equals(methodReturnType.type())) {
                    continue;
                }

                if (isBoolean(fieldType)) {
                    if (methodName.equals(IS_NAME + fieldCapitalizedName) || methodName.equals(GET_NAME + fieldCapitalizedName)) {
                        getters.put(fieldName, method);
                        continue;
                    }
                }

                if (methodName.equals(GET_NAME + fieldCapitalizedName)) {
                    getters.put(fieldName, method);
                }
            }

            for (MetaConstructor<?> constructor : this.constructors) {
                for (Entry<String, MetaParameter<?>> parameter : constructor.parameters().entrySet()) {
                    if (parameter.getKey().equals(fieldName) && parameter.getValue().type().equals(fieldValue.type())) {
                        List<MetaConstructor<?>> fieldConstructors = constructors.get(parameter.getKey());
                        if (isNull(fieldConstructors)) {
                            constructors.put(parameter.getKey(), linkedListOf(constructor));
                            continue;
                        }
                        fieldConstructors.add(constructor);
                    }
                }
            }
        }

        for (Entry<String, MetaField<?>> entry : fields.entrySet()) {
            MetaProperty.MetaPropertyBuilder<Object> builder = MetaProperty.builder()
                    .name(entry.getKey())
                    .type(cast(entry.getValue().type()))
                    .owner(cast(this));

            MetaMethod<?> fieldGetter = getters.get(entry.getKey());
            MetaMethod<?> fieldSetter = setters.get(entry.getKey());
            List<MetaConstructor<?>> fieldConstructors = constructors.get(entry.getKey());

            if (isNull(fieldGetter) && isNull(fieldSetter) && isEmpty(fieldConstructors)) continue;
            boolean constructable = fieldConstructors
                    .stream()
                    .anyMatch(constructor -> constructor == allArgumentsConstructors);

            apply(fieldGetter, getter -> builder.getter(cast(getter)));
            apply(fieldConstructors, constructorList -> builder.constructors(immutableArrayOf(constructorList)));

            if (nonNull(fieldSetter)) {
                builder.setter(cast(fieldSetter));
                if (!constructable) {
                    MetaProperty<Object> property = builder.build();
                    settableProperties.add(property);
                    properties.put(entry.getKey(), property);
                    continue;
                }
            }

            MetaProperty<Object> property = builder.build();
            if (constructable) {
                constructableProperties.add(property);
            }

            properties.put(entry.getKey(), property);
        }

        this.gettableProperties = properties()
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
        Entity entity = Value.asEntity(value);
        EntityMapping mapping = entity.mapping();

        Object[] constructorArguments = new Object[constructableProperties.size()];
        int index = 0;
        for (MetaProperty<?> property : constructableProperties) {
            String name = property.name();
            MetaType<?> type = property.type();

            if (type.primitive()) {
                constructorArguments[index] = mapping.mapOrDefault(name, PRIMITIVE_TYPE_MAPPINGS.get(type), type::toModel);
                index++;
                continue;
            }

            constructorArguments[index] = mapping.map(name, type::toModel);
            index++;
        }

        T model = allArgumentsConstructors.invoke(constructorArguments);

        for (MetaProperty<?> property : settableProperties) {
            String propertyKey = property.name();
            InstanceMetaMethod<Object, Object> setter = property.setter();
            MetaType<Object> type = property.setter().returnType();

            if (type.primitive()) {
                setter.invoke(model, mapping.mapOrDefault(propertyKey, PRIMITIVE_TYPE_MAPPINGS.get(type), type::toModel));
                continue;
            }

            setter.invoke(model, mapping.map(propertyKey, type::toModel));
        }

        return model;
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
