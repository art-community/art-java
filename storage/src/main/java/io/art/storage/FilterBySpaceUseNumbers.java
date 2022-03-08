package io.art.storage;

import lombok.*;
import static io.art.storage.StorageConstants.FilterOperator.*;

@Getter
public class FilterBySpaceUseNumbers<Type> extends FilterBySpaceUseValues<Type, Number> {
    FilterBySpaceUseNumbers(FilterRule<Type> rule) {
        super(rule);
    }

    public FilterRule<Type> moreThan(Number value) {
        this.operator = MORE;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> lessThan(Number value) {
        this.operator = LESS;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> between(Number start, Number end) {
        this.operator = BETWEEN;
        values.add(start);
        values.add(end);
        return rule;
    }

    public FilterRule<Type> notBetween(Number start, Number end) {
        this.operator = NOT_BETWEEN;
        values.add(start);
        values.add(end);
        return rule;
    }
}
