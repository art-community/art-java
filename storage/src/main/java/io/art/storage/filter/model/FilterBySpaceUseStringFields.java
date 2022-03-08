package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.filter.implementation.*;

@Public
public interface FilterBySpaceUseStringFields<Current, Other> extends FilterBySpaceUseFields<Current, Other, String> {
    FilterRule<Current> startsWith(MetaField<? extends MetaClass<Other>, String> otherField);

    FilterRule<Current> endsWith(MetaField<? extends MetaClass<Other>, String> otherField);

    FilterRule<Current> contains(MetaField<? extends MetaClass<Other>, String> otherField);
}
