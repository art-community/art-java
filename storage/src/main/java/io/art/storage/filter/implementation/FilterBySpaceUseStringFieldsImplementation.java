package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseStringFieldsImplementation<Current, Other> extends FilterBySpaceUseFieldsImplementation<Current, Other, String> implements FilterBySpaceUseStringFields<Current, Other> {
    FilterBySpaceUseStringFieldsImplementation(FilterRule<Current> rule) {
        super(rule);
    }

    @Override
    public FilterRule<Current> startsWith(MetaField<? extends MetaClass<Other>, String> otherField) {
        this.operator = STARTS_WITH;
        fields.add(otherField);
        return rule;
    }

    @Override
    public FilterRule<Current> endsWith(MetaField<? extends MetaClass<Other>, String> otherField) {
        this.operator = ENDS_WITH;
        fields.add(otherField);
        return rule;
    }

    @Override
    public FilterRule<Current> contains(MetaField<? extends MetaClass<Other>, String> otherField) {
        this.operator = CONTAINS;
        fields.add(otherField);
        return rule;
    }
}
