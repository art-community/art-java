package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseNumberFieldsImplementation<Current, Other>
        extends FilterBySpaceUseFieldsImplementation<Current, Other, Number>
        implements FilterBySpaceUseNumberFields<Current, Other> {

    FilterBySpaceUseNumberFieldsImplementation(FilterRule<Current> rule) {
        super(rule);
    }

    @Override
    public FilterRule<Current> moreThan(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        this.operator = MORE;
        fields.add(cast(otherField));
        return rule;
    }

    @Override
    public FilterRule<Current> lessThan(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        this.operator = LESS;
        fields.add(cast(otherField));
        return rule;
    }

    @Override
    public FilterRule<Current> between(MetaField<? extends MetaClass<Other>, ? extends Number> otherStartField, MetaField<? extends MetaClass<Other>, ? extends Number> otherEndField) {
        this.operator = BETWEEN;
        fields.add(cast(otherStartField));
        fields.add(cast(otherEndField));
        return rule;
    }

    @Override
    public FilterRule<Current> notBetween(MetaField<? extends MetaClass<Other>, ? extends Number> otherStartField, MetaField<? extends MetaClass<Other>, ? extends Number> otherEndField) {
        this.operator = NOT_BETWEEN;
        fields.add(cast(otherStartField));
        fields.add(cast(otherEndField));
        return rule;
    }
}
