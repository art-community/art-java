package io.art.storage;

import lombok.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseStrings<Type> extends FilterBySpaceUseValues<Type, String> {
    FilterBySpaceUseStrings(FilterRule<Type> rule) {
        super(rule);
    }

    public FilterRule<Type> startsWith(String pattern) {
        this.operator = STARTS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Type> endsWith(String pattern) {
        this.operator = ENDS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Type> contains(String pattern) {
        this.operator = CONTAINS;
        values.add(pattern);
        return rule;
    }
}
