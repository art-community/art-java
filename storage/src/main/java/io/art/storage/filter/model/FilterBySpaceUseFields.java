package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.filter.implementation.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface FilterBySpaceUseFields<Current, Other, FieldType> {
    FilterRule<Current> equal(MetaField<? extends MetaClass<Other>, FieldType> otherField);

    FilterRule<Current> notEqual(MetaField<? extends MetaClass<Other>, FieldType> otherField);

    FilterRule<Current> in(List<MetaField<? extends MetaClass<Other>, FieldType>> otherFields);

    FilterRule<Current> notIn(List<MetaField<? extends MetaClass<Other>, FieldType>> otherFields);

    FilterRule<Current> in(MetaField<? extends MetaClass<Other>, FieldType>... otherFields);

    FilterRule<Current> notIn(MetaField<? extends MetaClass<Other>, FieldType>... otherFields);
}
