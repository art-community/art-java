package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.storage.filter.implementation.*;

@Public
public interface FilterByNumber<Type> extends FilterByField<Type, Number> {
    FilterRule<Type> moreThan(Number value);

    FilterRule<Type> lessThan(Number value);

    FilterRule<Type> between(Number startValue, Number endValue);

    FilterRule<Type> notBetween(Number startValue, Number endValue);
}
