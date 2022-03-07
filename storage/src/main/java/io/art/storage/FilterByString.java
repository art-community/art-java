package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterByString<Type> extends FilterByField<Type, String> {
    public FilterByString(FilterRule<Type> rule, MetaField<? extends MetaClass<Type>, String> field) {
        super(rule, field);
    }

    public FilterRule<Type> startsWith(String pattern) {
        operator = STARTS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Type> endsWith(String pattern) {
        operator = ENDS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Type> contains(String pattern) {
        operator = CONTAINS;
        values.add(pattern);
        return rule;
    }
}
