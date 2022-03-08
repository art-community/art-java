package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseNumberFields<Current, Other> extends FilterBySpaceUseFields<Current, Other, Number> {
    FilterBySpaceUseNumberFields(FilterRule<Current> rule) {
        super(rule);
    }

    public FilterRule<Current> moreThan(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        this.operator = MORE;
        fields.add(cast(otherField));
        return rule;
    }

    public FilterRule<Current> lessThan(MetaField<? extends MetaClass<Other>, ? extends Number> otherField) {
        this.operator = LESS;
        fields.add(cast(otherField));
        return rule;
    }

    public FilterRule<Current> between(MetaField<? extends MetaClass<Other>, ? extends Number> otherStartField, MetaField<? extends MetaClass<Other>, ? extends Number> otherEndField) {
        this.operator = BETWEEN;
        fields.add(cast(otherStartField));
        fields.add(cast(otherEndField));
        return rule;
    }

    public FilterRule<Current> notBetween(MetaField<? extends MetaClass<Other>, ? extends Number> otherStartField, MetaField<? extends MetaClass<Other>, ? extends Number> otherEndField) {
        this.operator = NOT_BETWEEN;
        fields.add(cast(otherStartField));
        fields.add(cast(otherEndField));
        return rule;
    }
}
