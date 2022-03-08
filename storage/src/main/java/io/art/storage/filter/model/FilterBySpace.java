package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;

@Public
public interface FilterBySpace<Current, Other> {
    <FieldType> FilterBySpaceUseFields<Current, Other, FieldType> currentField(MetaField<? extends MetaClass<Current>, FieldType> currentField);

    FilterBySpaceUseStringFields<Current, Other> currentString(MetaField<? extends MetaClass<Current>, String> currentField);

    FilterBySpaceUseNumberFields<Current, Other> currentNumber(MetaField<? extends MetaClass<Current>, ? extends Number> currentField);

    FilterBySpaceUseNumbers<Current> otherNumber(MetaField<? extends MetaClass<Other>, ? extends Number> otherField);

    FilterBySpaceUseStrings<Current> otherString(MetaField<? extends MetaClass<Other>, String> otherField);

    <FieldType> FilterBySpaceUseValues<Current, FieldType> otherField(MetaField<? extends MetaClass<Other>, FieldType> otherField);
}
