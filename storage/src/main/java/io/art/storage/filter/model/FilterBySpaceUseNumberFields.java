package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.filter.implementation.*;

@Public
public interface FilterBySpaceUseNumberFields<Current, Other> {
    FilterRule<Current> moreThan(MetaField<? extends MetaClass<Other>, ? extends Number> otherField);

    FilterRule<Current> lessThan(MetaField<? extends MetaClass<Other>, ? extends Number> otherField);

    FilterRule<Current> between(MetaField<? extends MetaClass<Other>, ? extends Number> otherStartField, MetaField<? extends MetaClass<Other>, ? extends Number> otherEndField);

    FilterRule<Current> notBetween(MetaField<? extends MetaClass<Other>, ? extends Number> otherStartField, MetaField<? extends MetaClass<Other>, ? extends Number> otherEndField);
}
