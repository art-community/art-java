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
import io.art.meta.schema.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.util.*;

@ToString
@ForGenerator
@EqualsAndHashCode
public abstract class MetaClass<T> {
    private final MetaType<T> definition;
    private final Set<MetaConstructor<T>> constructors;
    private final Map<String, MetaField<?>> fields;
    private final Set<MetaMethod<?>> methods;
    private final Map<Class<?>, MetaClass<?>> classes;
    private MetaProviderTemplate provider;
    private MetaCreatorTemplate creator;

    protected MetaClass(MetaType<T> definition) {
        this.definition = definition;
        constructors = set();
        fields = map();
        methods = set();
        classes = map();
        MetaClassMutableRegistry.register(this);
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
        classes.put(metaClass.definition().type(), metaClass);
        return metaClass;
    }

    protected void beginComputation() {
        definition.beginComputation();

        for (MetaField<?> field : fields.values()) {
            field.type().beginComputation();
        }

        for (MetaConstructor<T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().beginComputation());
        }

        for (MetaMethod<?> method : methods) {
            method.returnType().beginComputation();
            method.parameters().values().forEach(parameter -> parameter.type().beginComputation());
        }

        for (MetaClass<?> nested : classes.values()) {
            nested.beginComputation();
        }

        MetaConstructor<T> noPropertiesConstructor = selectNoPropertiesConstructor();
        MetaConstructor<T> localPropertiesConstructor = selectLocalPropertiesConstructor();
        MetaConstructor<T> allPropertiesConstructor = selectAllPropertiesConstructor();

        List<MetaProperty<?>> gettableProperties = linkedList();
        List<MetaProperty<?>> constructableProperties = linkedList();

        for (MetaField<?> field : fields.values()) {
            MetaPropertyBuilder<?> builder = MetaProperty.builder()
                    .name(field.name())
                    .type(cast(field.type()));

            Optional<MetaMethod<?>> getter = methods
                    .stream()
                    .filter(method -> isGetter(field, method))
                    .findFirst();

            getter.ifPresent(metaMethod -> builder.getter(cast(metaMethod)));

            if (nonNull(localPropertiesConstructor) || nonNull(allPropertiesConstructor)) {
                MetaConstructor<?> constructor = nonNull(allPropertiesConstructor) ? allPropertiesConstructor : localPropertiesConstructor;
                builder.index(constructor.parameters().get(field.name()).index());

                MetaProperty<?> property = builder.build();

                if (getter.isPresent()) {
                    gettableProperties.add(property);
                }

                constructableProperties.add(property);
                continue;
            }

            if (getter.isPresent()) {
                gettableProperties.add(builder.build());
            }
        }

        Map<String, MetaProperty<?>> gettablePropertyMap = gettableProperties
                .stream()
                .collect(mapCollector(MetaProperty::name, identity()));
        provider = new MetaProviderTemplate(gettablePropertyMap, gettableProperties.toArray(new MetaProperty[0]));

        Map<String, MetaProperty<?>> constructablePropertyMap = constructableProperties
                .stream()
                .collect(mapCollector(MetaProperty::name, identity()));
        creator = MetaCreatorTemplate.builder()
                .propertyMap(constructablePropertyMap)
                .propertyArray(constructableProperties.toArray(new MetaProperty[0]))
                .noPropertiesConstructor(noPropertiesConstructor)
                .localPropertiesConstructor(localPropertiesConstructor)
                .allPropertiesConstructor(allPropertiesConstructor)
                .build();
    }

    private MetaConstructor<T> selectNoPropertiesConstructor() {
        for (MetaConstructor<T> constructor : constructors) {
            if (constructor.parameters().isEmpty()) return constructor;
        }
        return null;
    }

    private MetaConstructor<T> selectLocalPropertiesConstructor() {
        for (MetaConstructor<T> constructor : constructors) {
            List<MetaField<?>> localFields = this.fields.values()
                    .stream()
                    .filter(field -> !field.inherited())
                    .collect(listCollector());
            MetaParameter<?>[] parameters = constructor.parameters().values().toArray(new MetaParameter[0]);
            if (localFields.size() != parameters.length) continue;
            for (int index = 0; index < localFields.size(); index++) {
                MetaParameter<?> parameter = parameters[index];
                boolean hasField = localFields
                        .stream()
                        .anyMatch(field -> parameter.name().equals(field.name()) && parameter.type().equals(field.type()));
                if (hasField) {
                    return constructor;
                }
            }
        }
        return null;
    }

    private MetaConstructor<T> selectAllPropertiesConstructor() {
        for (MetaConstructor<T> constructor : constructors) {
            Collection<MetaField<?>> fields = this.fields.values();
            MetaParameter<?>[] parameters = constructor.parameters().values().toArray(new MetaParameter[0]);
            if (fields.size() != parameters.length) continue;
            for (int index = 0; index < fields.size(); index++) {
                MetaParameter<?> parameter = parameters[index];
                boolean hasField = fields
                        .stream()
                        .anyMatch(field -> parameter.name().equals(field.name()) && parameter.type().equals(field.type()));
                if (hasField) {
                    return constructor;
                }
            }
        }
        return null;
    }

    protected void completeComputation() {
        definition.completeComputation();

        for (MetaField<?> field : fields.values()) {
            field.type().completeComputation();
        }

        for (MetaConstructor<T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().completeComputation());
        }

        for (MetaMethod<?> method : methods) {
            method.returnType().completeComputation();
            method.parameters().values().forEach(parameter -> parameter.type().completeComputation());
        }

        for (MetaClass<?> nested : classes.values()) {
            nested.completeComputation();
        }

        for (MetaConstructor<T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().completeComputation());
        }
    }

    private boolean isGetter(MetaField<?> field, MetaMethod<?> method) {
        if (method.isStatic()) return false;
        if (!method.parameters().isEmpty()) return false;
        if (!method.returnType().equals(field.type())) return false;
        if (method.name().equals(GET_NAME + capitalize(field.name()))) return true;
        if (method.name().equals(field.name())) return true;
        return field.type().internalKind() == BOOLEAN && method.name().equals(IS_NAME + capitalize(field.name()));
    }

    public MetaProviderTemplate provider() {
        return provider;
    }

    public MetaCreatorTemplate creator() {
        return creator;
    }

    public MetaType<T> definition() {
        return definition;
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
