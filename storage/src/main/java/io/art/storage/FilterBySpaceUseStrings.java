package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseStrings<Current, Other> extends FilterBySpaceUseValues<Current, Other, String> {
    FilterBySpaceUseStrings(FilterRule<Current> rule, MetaField<? extends MetaClass<Other>, String> otherField) {
        super(rule, otherField);
    }

    public FilterRule<Current> startsWith(String pattern) {
        this.operator = STARTS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Current> endsWith(String pattern) {
        this.operator = ENDS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Current> contains(String pattern) {
        this.operator = CONTAINS;
        values.add(pattern);
        return rule;
    }
}
