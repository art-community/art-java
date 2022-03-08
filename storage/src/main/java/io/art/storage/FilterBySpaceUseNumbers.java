package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseNumbers<Current, Other> extends FilterBySpaceUseValues<Current, Other, Number> {
    FilterBySpaceUseNumbers(FilterRule<Current> rule, MetaField<? extends MetaClass<Other>, ? extends Number> field) {
        super(rule, cast(field));
    }

    public FilterRule<Current> moreThan(Number value) {
        this.operator = MORE;
        values.add(value);
        return rule;
    }

    public FilterRule<Current> lessThan(Number value) {
        this.operator = LESS;
        values.add(value);
        return rule;
    }

    public FilterRule<Current> between(Number otherStart, Number otherEnd) {
        this.operator = BETWEEN;
        values.add(otherStart);
        values.add(otherEnd);
        return rule;
    }

    public FilterRule<Current> notBetween(Number otherStart, Number otherEnd) {
        this.operator = NOT_BETWEEN;
        values.add(otherStart);
        values.add(otherEnd);
        return rule;
    }
}
