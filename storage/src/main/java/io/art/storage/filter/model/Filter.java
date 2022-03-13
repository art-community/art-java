package io.art.storage.filter.model;

import io.art.core.annotation.*;
import io.art.meta.model.*;
import io.art.storage.*;
import io.art.storage.filter.implementation.*;
import io.art.storage.index.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;

@Public
@SuppressWarnings({VARARGS})
public interface Filter<Type> {
    <FieldType> FilterByField<Type, FieldType> byField(MetaField<? extends MetaClass<Type>, FieldType> field);

    FilterByString<Type> byString(MetaField<? extends MetaClass<Type>, String> field);

    FilterByNumber<Type> byNumber(MetaField<? extends MetaClass<Type>, ? extends Number> field);

    <Other> FilterBySpace<Type, Other> bySpace(MetaClass<Other> otherSpace, MetaField<? extends MetaClass<Type>, ?> mappingField);

    <Other, F1> FilterBySpace<Type, Other> byIndex(Index1<Other, F1> index, MetaField<? extends MetaClass<Type>, F1> field1);

    <Other, F1, F2> FilterBySpace<Type, Other> byIndex(Index2<Other, F1, F2> index,
                                                       MetaField<? extends MetaClass<Type>, F1> field1,
                                                       MetaField<? extends MetaClass<Type>, F2> field2);

    <Other, F1, F2, F3> FilterBySpace<Type, Other> byIndex(Index3<Other, F1, F2, F3> index,
                                                           MetaField<? extends MetaClass<Type>, F1> field1,
                                                           MetaField<? extends MetaClass<Type>, F2> field2,
                                                           MetaField<? extends MetaClass<Type>, F3> field3);

    <Other, F1, F2, F3, F4> FilterBySpace<Type, Other> byIndex(Index4<Other, F1, F2, F3, F4> index,
                                                               MetaField<? extends MetaClass<Type>, F1> field1,
                                                               MetaField<? extends MetaClass<Type>, F2> field2,
                                                               MetaField<? extends MetaClass<Type>, F3> field3,
                                                               MetaField<? extends MetaClass<Type>, F4> field4);

    <Other, F1, F2, F3, F4, F5> FilterBySpace<Type, Other> byIndex(Index5<Other, F1, F2, F3, F4, F5> index,
                                                                   MetaField<? extends MetaClass<Type>, F1> field1,
                                                                   MetaField<? extends MetaClass<Type>, F2> field2,
                                                                   MetaField<? extends MetaClass<Type>, F3> field3,
                                                                   MetaField<? extends MetaClass<Type>, F4> field4,
                                                                   MetaField<? extends MetaClass<Type>, F5> field5);

    FilterRule<Type> byFunction(MetaMethod<? extends MetaClass<? extends Storage>, Boolean> function);
}
