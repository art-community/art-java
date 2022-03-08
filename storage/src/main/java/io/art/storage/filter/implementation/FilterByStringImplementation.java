package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;

@Getter
public class FilterByStringImplementation<Type> extends FilterByFieldImplementation<Type, String> implements FilterByString<Type> {
    public FilterByStringImplementation(FilterRule<Type> rule, MetaField<? extends MetaClass<Type>, String> field) {
        super(rule, field);
    }

    @Override
    public FilterRule<Type> startsWith(String pattern) {
        operator = STARTS_WITH;
        values.add(pattern);
        return rule;
    }

    @Override
    public FilterRule<Type> endsWith(String pattern) {
        operator = ENDS_WITH;
        values.add(pattern);
        return rule;
    }

    @Override
    public FilterRule<Type> contains(String pattern) {
        operator = CONTAINS;
        values.add(pattern);
        return rule;
    }
}
