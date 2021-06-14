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
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.type.TypeInspector.*;
import static java.util.Objects.*;
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
    private MetaSchema<T> schema;

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

        schema = computeSchema();
    }

    private MetaSchema<T> computeSchema() {
        MetaConstructor<T> propertiesConstructor = null;
        for (MetaConstructor<T> constructor : constructors) {
            if (!constructor.isPublic()) continue;
            constructor.parameters().values().forEach(parameter -> parameter.type().compute());

            Collection<MetaField<?>> fields = this.fields.values();
            MetaParameter<?>[] parameters = constructor.parameters().values().toArray(new MetaParameter[0]);
            if (fields.size() != parameters.length) continue;
            for (int index = 0; index < fields.size(); index++) {
                MetaParameter<?> parameter = parameters[index];
                boolean hasField = fields
                        .stream()
                        .anyMatch(field -> parameter.name().equals(field.name()) && parameter.type().equals(field.type()));
                if (hasField) {
                    propertiesConstructor = constructor;
                    break;
                }
            }
        }

        List<MetaProperty<?>> gettableProperties = linkedList();
        List<MetaProperty<?>> primitiveProperties = linkedList();
        List<MetaProperty<?>> objectProperties = linkedList();

        for (MetaField<?> field : fields.values()) {
            MetaPropertyBuilder<?> builder = MetaProperty.builder()
                    .name(field.name())
                    .type(cast(field.type()));

            Optional<MetaMethod<?>> getter = methods
                    .stream()
                    .filter(method -> isGetter(field, method))
                    .findFirst();

            getter.ifPresent(metaMethod -> builder.getter(cast(metaMethod)));

            if (nonNull(propertiesConstructor)) {
                int constructorParameterIndex = propertiesConstructor.parameters().get(field.name()).index();
                builder.index(constructorParameterIndex);

                MetaProperty<?> property = builder.build();

                if (getter.isPresent()) {
                    gettableProperties.add(property);
                }

                if (property.type().isPrimitive()) {
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

        return MetaSchema.<T>builder()
                .propertiesConstructor(propertiesConstructor)
                .primitiveProperties(primitiveProperties)
                .gettableProperties(gettableProperties)
                .objectProperties(objectProperties)
                .build();
    }

    private boolean isGetter(MetaField<?> field, MetaMethod<?> method) {
        if (!method.isPublic()) return false;
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

    public MetaSchema<T> schema() {
        return schema;
    }
}
