package io.art.storage.filter.model;

import io.art.meta.model.*;
import io.art.storage.filter.implementation.*;

public interface FilterBySpace<Current, Other> {
    <FieldType> FilterBySpaceUseFieldsImplementation<Current, Other, FieldType> currentField(MetaField<? extends MetaClass<Current>, FieldType> currentField);

    FilterBySpaceUseStringFieldsImplementation<Current, Other> currentString(MetaField<? extends MetaClass<Current>, String> currentField);

    FilterBySpaceUseNumberFieldsImplementation<Current, Other> currentNumber(MetaField<? extends MetaClass<Current>, ? extends Number> currentField);

    FilterBySpaceUseNumbersImplementation<Current> otherNumber(MetaField<? extends MetaClass<Other>, ? extends Number> otherField);

    FilterBySpaceUseStringsImplementation<Current> otherString(MetaField<? extends MetaClass<Other>, String> otherField);

    <FieldType> FilterBySpaceUseValuesImplementation<Current, FieldType> otherField(MetaField<? extends MetaClass<Other>, FieldType> otherField);
}
