package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.storage.filter.implementation.*;

@Public
public interface FilterBySpaceUseNumbers<Type> extends FilterBySpaceUseValues<Type, Number> {
    FilterRule<Type> moreThan(Number value);

    FilterRule<Type> lessThan(Number value);

    FilterRule<Type> between(Number start, Number end);

    FilterRule<Type> notBetween(Number start, Number end);
}
