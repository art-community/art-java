package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.FilterCondition.*;
import static lombok.AccessLevel.*;
import java.util.*;

@Getter
public class Filter<Type> {
    private FilterCondition condition = AND;
    private final List<FilterPart> filters = linkedList();

    public <FieldType> FilterByField<Type> byField(MetaField<? extends MetaClass<Type>, FieldType> field) {
        FilterByField<Type> filter = new FilterByField<>(new FilterRule<>(this), field);
        FilterPart part = new FilterPart(condition, FilterMode.FIELD);
        part.byField = filter;
        filters.add(part);
        return filter;
    }

    public <Other> FilterBySpace<Type, Other> bySpace(MetaClass<Other> otherSpace,
                                                      MetaField<? extends MetaClass<Type>, ?> currentField,
                                                      MetaField<? extends MetaClass<Type>, ?> mappingField) {
        FilterBySpace<Type, Other> filter = new FilterBySpace<>(new FilterRule<>(this), otherSpace, currentField).bySpace(mappingField);
        FilterPart part = new FilterPart(condition, FilterMode.SPACE);
        part.bySpace = filter;
        filters.add(part);
        return filter;
    }

    @SafeVarargs
    public final <Other> FilterBySpace<Type, Other> byIndex(MetaClass<Other> otherSpace,
                                                            MetaField<? extends MetaClass<Type>, ?> currentField,
                                                            MetaField<? extends MetaClass<Type>, ?>... indexedFields) {
        FilterBySpace<Type, Other> filter = new FilterBySpace<>(new FilterRule<>(this), otherSpace, currentField).byIndex(indexedFields);
        FilterPart part = new FilterPart(condition, FilterMode.INDEX);
        part.byIndex = filter;
        filters.add(part);
        return filter;
    }


    public FilterByFunction<Type> byFunction(MetaMethod<MetaClass<? extends Storage>, Boolean> function) {
        FilterByFunction<Type> filter = new FilterByFunction<>(new FilterRule<>(this), function);
        FilterPart part = new FilterPart(condition, FilterMode.FUNCTION);
        part.byFunction = filter;
        filters.add(part);
        return filter;
    }

    Filter<Type> and() {
        condition = AND;
        return this;
    }

    Filter<Type> or() {
        condition = OR;
        return this;
    }

    @Getter
    @RequiredArgsConstructor(access = PRIVATE)
    public static class FilterPart {
        private final FilterCondition condition;
        private final FilterMode mode;
        private FilterByField<?> byField;
        private FilterBySpace<?, ?> bySpace;
        private FilterBySpace<?, ?> byIndex;
        private FilterByFunction<?> byFunction;
    }
}

