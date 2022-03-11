package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterCondition.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class FilterImplementation<Type> implements Filter<Type> {
    private final FilterCondition currentCondition;
    @Getter
    private final List<FilterPart> parts;

    private final FilterRule<Type> rule = new FilterRule<>(this);

    @Override
    public <FieldType> FilterByField<Type, FieldType> byField(MetaField<? extends MetaClass<Type>, FieldType> field) {
        FilterByFieldImplementation<Type, FieldType> filter = new FilterByFieldImplementation<>(rule, field);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FIELD);
        part.byField = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public FilterByString<Type> byString(MetaField<? extends MetaClass<Type>, String> field) {
        FilterByStringImplementation<Type> filter = new FilterByStringImplementation<>(rule, field);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FIELD);
        part.byField = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public FilterByNumber<Type> byNumber(MetaField<? extends MetaClass<Type>, ? extends Number> field) {
        FilterByNumberImplementation<Type> filter = new FilterByNumberImplementation<>(rule, field);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FIELD);
        part.byField = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public <Other> FilterBySpace<Type, Other> bySpace(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?> mappingField) {
        FilterBySpaceImplementation<Type, Other> filter = new FilterBySpaceImplementation<>(rule, otherSpace).bySpace(mappingField);
        FilterPart part = new FilterPart(currentCondition, FilterMode.SPACE);
        part.bySpace = filter;
        parts.add(part);
        return filter;
    }

    @Override
    @SafeVarargs
    public final <Other> FilterBySpace<Type, Other> byIndex(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        FilterBySpaceImplementation<Type, Other> filter = new FilterBySpaceImplementation<>(rule, otherSpace).byIndex(indexedFields);
        FilterPart part = new FilterPart(currentCondition, FilterMode.INDEX);
        part.byIndex = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public FilterRule<Type> byFunction(MetaMethod<? extends MetaClass<? extends Storage>, Boolean> function) {
        FilterByFunctionImplementation<Type> filter = new FilterByFunctionImplementation<>(rule, function);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FUNCTION);
        part.byFunction = filter;
        parts.add(part);
        return rule;
    }

    Filter<Type> and(Consumer<Filter<Type>> nested) {
        FilterImplementation<Type> resolved = new FilterImplementation<>(AND, linkedList());
        nested.accept(resolved);
        NestedFilter filter = new NestedFilter(resolved.parts);
        FilterPart part = new FilterPart(currentCondition, FilterMode.NESTED);
        part.nested = filter;
        parts.add(part);
        return this;
    }

    Filter<Type> or(Consumer<Filter<Type>> nested) {
        FilterImplementation<Type> resolved = new FilterImplementation<>(OR, linkedList());
        nested.accept(resolved);
        NestedFilter filter = new NestedFilter(resolved.parts);
        FilterPart part = new FilterPart(currentCondition, FilterMode.NESTED);
        part.nested = filter;
        parts.add(part);
        return this;
    }

    Filter<Type> and() {
        return new FilterImplementation<>(AND, parts);
    }

    Filter<Type> or() {
        return new FilterImplementation<>(OR, parts);
    }

    @Getter
    @RequiredArgsConstructor(access = PRIVATE)
    public static class FilterPart {
        private final FilterCondition condition;
        private final FilterMode mode;
        private FilterByFieldImplementation<?, ?> byField;
        private FilterBySpaceImplementation<?, ?> bySpace;
        private FilterBySpaceImplementation<?, ?> byIndex;
        private FilterByFunctionImplementation<?> byFunction;
        private NestedFilter nested;
    }
}

