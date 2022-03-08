package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseStringFields<Current, Other> extends FilterBySpaceUseFields<Current, Other, String> {
    FilterBySpaceUseStringFields(FilterRule<Current> rule) {
        super(rule);
    }

    public FilterRule<Current> startsWith(MetaField<? extends MetaClass<Other>, String> other) {
        this.operator = STARTS_WITH;
        fields.add(other);
        return rule;
    }

    public FilterRule<Current> endsWith(MetaField<? extends MetaClass<Other>, String> other) {
        this.operator = ENDS_WITH;
        fields.add(other);
        return rule;
    }

    public FilterRule<Current> contains(MetaField<? extends MetaClass<Other>, String> other) {
        this.operator = CONTAINS;
        fields.add(other);
        return rule;
    }
}
