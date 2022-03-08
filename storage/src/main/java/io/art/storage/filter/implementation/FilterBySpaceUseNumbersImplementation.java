package io.art.storage.filter.implementation;

import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseNumbersImplementation<Type> extends FilterBySpaceUseValuesImplementation<Type, Number> implements FilterBySpaceUseNumbers<Type> {
    FilterBySpaceUseNumbersImplementation(FilterRule<Type> rule) {
        super(rule);
    }

    @Override
    public FilterRule<Type> moreThan(Number value) {
        this.operator = MORE;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> lessThan(Number value) {
        this.operator = LESS;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> moreThanEquals(Number value) {
        this.operator = MORE_EQUALS;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> lessThanEquals(Number value) {
        this.operator = LESS_EQUALS;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> between(Number start, Number end) {
        this.operator = BETWEEN;
        values.add(start);
        values.add(end);
        return rule;
    }

    @Override
    public FilterRule<Type> notBetween(Number start, Number end) {
        this.operator = NOT_BETWEEN;
        values.add(start);
        values.add(end);
        return rule;
    }
}
