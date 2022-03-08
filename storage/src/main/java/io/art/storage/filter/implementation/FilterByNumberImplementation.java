package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.storage.constants.StorageConstants.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;

@Getter
public class FilterByNumberImplementation<Type> extends FilterByFieldImplementation<Type, Number> implements FilterByNumber<Type> {
    public FilterByNumberImplementation(FilterRule<Type> rule, MetaField<? extends MetaClass<Type>, ? extends Number> field) {
        super(rule, cast(field));
    }

    @Override
    public FilterRule<Type> moreThan(Number value) {
        operator = FilterOperator.MORE;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> lessThan(Number value) {
        operator = FilterOperator.LESS;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> between(Number startValue, Number endValue) {
        operator = BETWEEN;
        values.add(startValue);
        values.add(endValue);
        return rule;
    }

    @Override
    public FilterRule<Type> notBetween(Number startValue, Number endValue) {
        operator = NOT_BETWEEN;
        values.add(startValue);
        values.add(endValue);
        return rule;
    }
}
