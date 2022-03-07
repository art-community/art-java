package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.storage.StorageConstants.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterByNumber<Type> extends FilterByField<Type, Number> {
    public FilterByNumber(FilterRule<Type> rule, MetaField<? extends MetaClass<Type>, ? extends Number> field) {
        super(rule, cast(field));
    }

    public FilterRule<Type> moreThan(Number value) {
        operator = FilterOperator.MORE;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> lessThan(Number value) {
        operator = FilterOperator.LESS;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> between(Number startValue, Number endValue) {
        operator = BETWEEN;
        values.add(startValue);
        values.add(endValue);
        return rule;
    }

    public FilterRule<Type> notBetween(Number startValue, Number endValue) {
        operator = NOT_BETWEEN;
        values.add(startValue);
        values.add(endValue);
        return rule;
    }
}
