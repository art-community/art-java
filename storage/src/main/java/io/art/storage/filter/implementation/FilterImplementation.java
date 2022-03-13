package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.model.*;
import io.art.storage.index.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.model.Tuple.*;
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
        FilterBySpaceImplementation<Type, Other> filter = cast(new FilterBySpaceImplementation<>(rule).bySpace(cast(otherSpace), mappingField));
        FilterPart part = new FilterPart(currentCondition, FilterMode.SPACE);
        part.bySpace = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public <Other, F1> FilterBySpace<Type, Other> byIndex(Index1<Other, F1> index, MetaField<? extends MetaClass<Type>, F1> field1) {
        FilterBySpaceImplementation<Type, Other> filter = cast(new FilterBySpaceImplementation<>(rule).byIndex(index, tuple(field1)));
        FilterPart part = new FilterPart(currentCondition, FilterMode.INDEX);
        part.byIndex = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public <Other, F1, F2> FilterBySpace<Type, Other> byIndex(Index2<Other, F1, F2> index,
                                                              MetaField<? extends MetaClass<Type>, F1> field1,
                                                              MetaField<? extends MetaClass<Type>, F2> field2) {
        FilterBySpaceImplementation<Type, Other> filter = cast(new FilterBySpaceImplementation<>(rule).byIndex(index, tuple(field1, field2)));
        FilterPart part = new FilterPart(currentCondition, FilterMode.INDEX);
        part.byIndex = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public <Other, F1, F2, F3> FilterBySpace<Type, Other> byIndex(Index3<Other, F1, F2, F3> index,
                                                                  MetaField<? extends MetaClass<Type>, F1> field1,
                                                                  MetaField<? extends MetaClass<Type>, F2> field2,
                                                                  MetaField<? extends MetaClass<Type>, F3> field3) {
        FilterBySpaceImplementation<Type, Other> filter = cast(new FilterBySpaceImplementation<>(rule).byIndex(index, tuple(field1, field2, field3)));
        FilterPart part = new FilterPart(currentCondition, FilterMode.INDEX);
        part.byIndex = filter;
        parts.add(part);
        return filter;
    }


    @Override
    public <Other, F1, F2, F3, F4> FilterBySpace<Type, Other> byIndex(Index4<Other, F1, F2, F3, F4> index,
                                                                      MetaField<? extends MetaClass<Type>, F1> field1,
                                                                      MetaField<? extends MetaClass<Type>, F2> field2,
                                                                      MetaField<? extends MetaClass<Type>, F3> field3,
                                                                      MetaField<? extends MetaClass<Type>, F4> field4) {
        FilterBySpaceImplementation<Type, Other> filter = cast(new FilterBySpaceImplementation<>(rule).byIndex(index, tuple(field1, field2, field3, field4)));
        FilterPart part = new FilterPart(currentCondition, FilterMode.INDEX);
        part.byIndex = filter;
        parts.add(part);
        return filter;
    }

    @Override
    public <Other, F1, F2, F3, F4, F5> FilterBySpace<Type, Other> byIndex(Index5<Other, F1, F2, F3, F4, F5> index,
                                                                          MetaField<? extends MetaClass<Type>, F1> field1,
                                                                          MetaField<? extends MetaClass<Type>, F2> field2,
                                                                          MetaField<? extends MetaClass<Type>, F3> field3,
                                                                          MetaField<? extends MetaClass<Type>, F4> field4,
                                                                          MetaField<? extends MetaClass<Type>, F5> field5) {
        FilterBySpaceImplementation<Type, Other> filter = cast(new FilterBySpaceImplementation<>(rule).byIndex(index, tuple(field1, field2, field3, field4, field5)));
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
        FilterPart part = new FilterPart(AND, FilterMode.NESTED);
        part.nested = filter;
        parts.add(part);
        return this;
    }

    Filter<Type> or(Consumer<Filter<Type>> nested) {
        FilterImplementation<Type> resolved = new FilterImplementation<>(AND, linkedList());
        nested.accept(resolved);
        NestedFilter filter = new NestedFilter(resolved.parts);
        FilterPart part = new FilterPart(OR, FilterMode.NESTED);
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

