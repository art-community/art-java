package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.FilterCondition.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class Filter<Type> {
    private final FilterCondition currentCondition;
    @Getter
    private final List<FilterPart> parts;
    private final FilterRule<Type> rule = new FilterRule<>(this);

    public <FieldType> FilterByField<Type, FieldType> byField(MetaField<? extends MetaClass<Type>, FieldType> field) {
        FilterByField<Type, FieldType> filter = new FilterByField<>(rule, field);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FIELD);
        part.byField = filter;
        parts.add(part);
        return filter;
    }

    public FilterByString<Type> byString(MetaField<? extends MetaClass<Type>, String> field) {
        FilterByString<Type> filter = new FilterByString<>(rule, field);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FIELD);
        part.byField = filter;
        parts.add(part);
        return filter;
    }

    public FilterByNumber<Type> byNumber(MetaField<? extends MetaClass<Type>, ? extends Number> field) {
        FilterByNumber<Type> filter = new FilterByNumber<>(rule, field);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FIELD);
        part.byField = filter;
        parts.add(part);
        return filter;
    }

    public <Other> FilterBySpace<Type, Other> bySpace(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?> mappingField) {
        FilterBySpace<Type, Other> filter = new FilterBySpace<>(rule, otherSpace).bySpace(mappingField);
        FilterPart part = new FilterPart(currentCondition, FilterMode.SPACE);
        part.bySpace = filter;
        parts.add(part);
        return filter;
    }

    @SafeVarargs
    public final <Other> FilterBySpace<Type, Other> byIndex(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        FilterBySpace<Type, Other> filter = new FilterBySpace<>(rule, otherSpace).byIndex(indexedFields);
        FilterPart part = new FilterPart(currentCondition, FilterMode.INDEX);
        part.byIndex = filter;
        parts.add(part);
        return filter;
    }


    public FilterByFunction<Type> byFunction(MetaMethod<MetaClass<? extends Storage>, Boolean> function) {
        FilterByFunction<Type> filter = new FilterByFunction<>(this, function, rule);
        FilterPart part = new FilterPart(currentCondition, FilterMode.FUNCTION);
        part.byFunction = filter;
        parts.add(part);
        return filter;
    }

    Filter<Type> and(Consumer<Filter<Type>> nested) {
        Filter<Type> resolved = new Filter<>(AND, linkedList());
        nested.accept(resolved);
        NestedFilter<Type> filter = new NestedFilter<>(resolved.parts);
        FilterPart part = new FilterPart(currentCondition, FilterMode.NESTED);
        part.nested = filter;
        parts.add(part);
        return this;
    }

    Filter<Type> or(Consumer<Filter<Type>> nested) {
        Filter<Type> resolved = new Filter<>(OR, linkedList());
        nested.accept(resolved);
        NestedFilter<Type> filter = new NestedFilter<>(resolved.parts);
        FilterPart part = new FilterPart(currentCondition, FilterMode.NESTED);
        part.nested = filter;
        parts.add(part);
        return this;
    }

    Filter<Type> and() {
        return new Filter<>(AND, parts);
    }

    Filter<Type> or() {
        return new Filter<>(OR, parts);
    }

    @Getter
    @RequiredArgsConstructor(access = PRIVATE)
    public static class FilterPart {
        private final FilterCondition condition;
        private final FilterMode mode;
        private FilterByField<?, ?> byField;
        private FilterBySpace<?, ?> bySpace;
        private FilterBySpace<?, ?> byIndex;
        private FilterByFunction<?> byFunction;
        private NestedFilter<?> nested;
    }
}

