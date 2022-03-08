package io.art.storage.filter.implementation;

import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseStringsImplementation<Type> extends FilterBySpaceUseValuesImplementation<Type, String> implements FilterBySpaceUseStrings<Type> {
    FilterBySpaceUseStringsImplementation(FilterRule<Type> rule) {
        super(rule);
    }

    @Override
    public FilterRule<Type> startsWith(String pattern) {
        this.operator = STARTS_WITH;
        values.add(pattern);
        return rule;
    }

    @Override
    public FilterRule<Type> endsWith(String pattern) {
        this.operator = ENDS_WITH;
        values.add(pattern);
        return rule;
    }

    @Override
    public FilterRule<Type> contains(String pattern) {
        this.operator = CONTAINS;
        values.add(pattern);
        return rule;
    }
}
