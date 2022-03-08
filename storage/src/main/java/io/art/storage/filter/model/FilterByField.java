package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.storage.filter.implementation.*;
import java.util.*;

@Public
public interface FilterByField<Type, FieldType> {
    FilterRule<Type> equal(FieldType value);

    FilterRule<Type> notEqual(FieldType value);

    FilterRule<Type> in(List<FieldType> values);

    FilterRule<Type> notIn(List<FieldType> values);

    FilterRule<Type> in(FieldType... values);

    FilterRule<Type> notIn(FieldType... values);
}
