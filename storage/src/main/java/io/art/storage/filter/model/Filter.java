package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.implementation.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;

@Public
@SuppressWarnings({UNCHECKED, VARARGS})
public interface Filter<Type> {
    <FieldType> FilterByField<Type, FieldType> byField(MetaField<? extends MetaClass<Type>, FieldType> field);

    FilterByString<Type> byString(MetaField<? extends MetaClass<Type>, String> field);

    FilterByNumber<Type> byNumber(MetaField<? extends MetaClass<Type>, ? extends Number> field);

    <Other> FilterBySpace<Type, Other> bySpace(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?> mappingField);

    <Other> FilterBySpace<Type, Other> byIndex(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?>... indexedFields);

    FilterRule<Type> byFunction(MetaMethod<? extends MetaClass<? extends Storage>, Boolean> function);
}
