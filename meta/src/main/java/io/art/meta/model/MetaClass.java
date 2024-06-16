/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import io.art.core.property.*;
import io.art.meta.exception.*;
import io.art.meta.model.MetaProperty.*;
import io.art.meta.schema.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.collector.ArrayCollector.*;
import static io.art.core.collector.MapCollector.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.ArrayFactory.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static io.art.meta.constants.MetaConstants.Errors.*;
import static io.art.meta.constants.MetaConstants.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.function.Function.*;
import java.util.*;
import java.util.function.*;

@Generation
@EqualsAndHashCode
public abstract class MetaClass<T> {
    private final MetaType<T> definition;
    private final Set<MetaConstructor<MetaClass<?>, T>> constructors;
    private final Map<String, MetaField<MetaClass<?>, ?>> fieldsMap;
    private final Set<MetaMethod<MetaClass<?>, ?>> methods;
    private final Map<Class<?>, MetaClass<?>> classes;
    private final static Map<Class<?>, MetaClass<?>> mutableRegistry = map();
    private MetaProviderTemplate provider;
    private MetaCreatorTemplate creator;
    private Boolean known;
    private List<MetaField<MetaClass<?>, ?>> fieldsArray;

    protected MetaClass(MetaType<T> definition) {
        this.definition = definition;
        constructors = set();
        fieldsMap = map();
        methods = set();
        classes = map();
        mutableRegistry.put(definition().type(), this);
    }

    protected <Meta extends MetaClass<?>, F extends MetaField<Meta, ?>> F register(F field) {
        return cast(computeIfAbsent(fieldsMap, field.name(), () -> cast(field)));
    }

    protected <Meta extends MetaClass<?>, M extends MetaMethod<Meta, ?>> M register(M method) {
        return cast(computeIfAbsent(methods, cast(method)));
    }

    protected <Meta extends MetaClass<?>, C extends MetaConstructor<Meta, T>> C register(C constructor) {
        return cast(computeIfAbsent(constructors, cast(constructor)));
    }

    protected <Meta extends MetaClass<?>> Meta register(Meta metaClass) {
        classes.put(metaClass.definition().type(), metaClass);
        return metaClass;
    }

    protected void beginComputation() {
        definition.beginComputation();

        fieldsArray = fixedArrayOf(fieldsMap.values());

        for (MetaField<MetaClass<?>, ?> field : fieldsMap.values()) {
            field.type().beginComputation();
        }

        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().beginComputation());
        }

        for (MetaMethod<MetaClass<?>, ?> method : methods) {
            method.returnType().beginComputation();
            method.parameters().values().forEach(parameter -> parameter.type().beginComputation());
        }

        for (MetaClass<?> nested : classes.values()) {
            nested.beginComputation();
        }

        MetaConstructor<MetaClass<?>, T> noPropertiesConstructor = selectNoPropertiesConstructor();
        MetaConstructor<MetaClass<?>, T> localPropertiesConstructor = selectLocalPropertiesConstructor();
        MetaConstructor<MetaClass<?>, T> allPropertiesConstructor = selectAllPropertiesConstructor();

        List<MetaProperty<?>> gettableProperties = linkedList();
        List<MetaProperty<?>> constructableProperties = linkedList();

        for (MetaField<MetaClass<?>, ?> field : fieldsMap.values()) {
            MetaPropertyBuilder<?> builder = MetaProperty.builder()
                    .name(field.name())
                    .type(cast(field.type()));

            Optional<MetaMethod<MetaClass<?>, ?>> getter = methods
                    .stream()
                    .filter(method -> isGetter(field, method))
                    .findFirst();

            getter.ifPresent(metaMethod -> builder.getter(cast(metaMethod)));

            if (nonNull(localPropertiesConstructor) || nonNull(allPropertiesConstructor)) {
                MetaConstructor<MetaClass<?>, ?> constructor = nonNull(allPropertiesConstructor) ? allPropertiesConstructor : localPropertiesConstructor;
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

        provider = new MetaProviderTemplate(immutableMapOf(gettablePropertyMap), immutableArrayOf(gettableProperties));

        Map<String, MetaProperty<?>> constructablePropertyMap = constructableProperties
                .stream()
                .collect(mapCollector(MetaProperty::name, identity()));

        creator = MetaCreatorTemplate.builder()
                .owner(this)
                .propertyMap(immutableMapOf(constructablePropertyMap))
                .propertyArray(immutableArrayOf(constructableProperties))
                .noPropertiesConstructor(noPropertiesConstructor)
                .localPropertiesConstructor(localPropertiesConstructor)
                .allPropertiesConstructor(allPropertiesConstructor)
                .build();
    }

    private MetaConstructor<MetaClass<?>, T> selectNoPropertiesConstructor() {
        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            if (constructor.parameters().isEmpty()) return constructor;
        }
        return null;
    }

    private MetaConstructor<MetaClass<?>, T> selectLocalPropertiesConstructor() {
        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            List<MetaField<MetaClass<?>, ?>> localFields = this.fieldsMap.values()
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

    private MetaConstructor<MetaClass<?>, T> selectAllPropertiesConstructor() {
        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            Collection<MetaField<MetaClass<?>, ?>> fields = this.fieldsMap.values();
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

        for (MetaField<MetaClass<?>, ?> field : fieldsMap.values()) {
            field.compute();
            field.type().completeComputation();
        }

        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().completeComputation());
        }

        for (MetaMethod<MetaClass<?>, ?> method : methods) {
            method.returnType().completeComputation();
            method.parameters().values().forEach(parameter -> parameter.type().completeComputation());
        }

        for (MetaClass<?> nested : classes.values()) {
            nested.completeComputation();
        }

        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            constructor.parameters().values().forEach(parameter -> parameter.type().completeComputation());
        }
    }

    private boolean isGetter(MetaField<MetaClass<?>, ?> field, MetaMethod<MetaClass<?>, ?> method) {
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

    public <F> MetaField<MetaClass<?>, F> field(String name) {
        return cast(fieldsMap.get(name));
    }

    public int index(MetaField<MetaClass<?>, ?> field) {
        return fieldsArray.indexOf(field);
    }

    public ImmutableMap<String, MetaField<MetaClass<?>, ?>> fields() {
        return immutableMapOf(fieldsMap);
    }

    public ImmutableSet<MetaMethod<MetaClass<?>, ?>> methods() {
        return immutableSetOf(methods);
    }

    public ImmutableSet<MetaConstructor<MetaClass<?>, T>> constructors() {
        return immutableSetOf(constructors);
    }

    public ImmutableMap<Class<?>, MetaClass<?>> classes() {
        return immutableMapOf(classes);
    }

    public MetaProxy proxy(Map<MetaMethod<MetaClass<?>, ?>, Function<Object, Object>> invocations) {
        throw new MetaException(format(CLASS_WITHOUT_PROXY, definition().type()));
    }

    public String toString() {
        return definition.toString();
    }

    public boolean isKnown() {
        if (nonNull(known)) return known;

        known = true;

        for (MetaField<MetaClass<?>, ?> field : fieldsMap.values()) {
            if (!field.isKnown()) return known = false;
        }

        for (MetaMethod<MetaClass<?>, ?> method : methods) {
            if (!method.isKnown()) return known = false;
        }

        for (MetaConstructor<MetaClass<?>, T> constructor : constructors) {
            if (!constructor.isKnown()) return known = false;
        }

        for (MetaClass<?> inner : classes.values()) {
            if (!inner.isKnown()) return known = false;
        }

        return known = true;
    }

    protected static <Meta extends MetaClass<?>> LazyProperty<Meta> self(Class<?> type) {
        return lazy(() -> cast(declaration(type)));
    }

    static void clearClassMutableRegistry() {
        mutableRegistry.clear();
    }

    static ImmutableMap<Class<?>, MetaClass<?>> getClassMutableRegistry() {
        return immutableMapOf(mutableRegistry);
    }

    static boolean hasClassInMutableRegistry(Class<?> type) {
        return mutableRegistry.containsKey(type);
    }
}
