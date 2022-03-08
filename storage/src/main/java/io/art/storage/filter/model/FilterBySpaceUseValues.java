package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.storage.filter.implementation.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import java.util.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface FilterBySpaceUseValues<Type, FieldType> {
    FilterRule<Type> equal(FieldType value);

    FilterRule<Type> notEqual(FieldType value);

    FilterRule<Type> in(List<FieldType> values);

    FilterRule<Type> notIn(List<FieldType> values);

    FilterRule<Type> in(FieldType... values);

    FilterRule<Type> notIn(FieldType... values);
}
