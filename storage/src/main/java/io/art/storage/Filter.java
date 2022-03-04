package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.*;
import static io.art.storage.StorageConstants.FilterOperator.*;
import static java.util.Arrays.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class Filter<Type> {
    private MetaField<? extends MetaClass<Type>, ?> field;
    private FilterOperator operator;
    private final List<Object> values = linkedList();

    public <FieldType> void equal(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType value) {
        this.field = field;
        operator = EQUALS;
        values.add(value);
    }

    public <FieldType> void notEqual(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType value) {
        this.field = field;
        operator = NOT_EQUALS;
        values.add(value);
    }

    public <FieldType> void in(MetaField<? extends MetaClass<Type>, FieldType> field, List<FieldType> values) {
        this.field = field;
        operator = IN;
        this.values.addAll(values);
    }

    public <FieldType> void notIn(MetaField<? extends MetaClass<Type>, FieldType> field, List<FieldType> values) {
        this.field = field;
        operator = NOT_IN;
        this.values.addAll(values);
    }

    @SafeVarargs
    public final <FieldType> void in(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType... values) {
        in(field, asList(values));
    }

    @SafeVarargs
    public final <FieldType> void notIn(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType... values) {
        notIn(field, asList(values));
    }

    public void moreThan(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number value) {
        this.field = field;
        operator = FilterOperator.MORE;
        values.add(value);
    }

    public void lessThan(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number value) {
        this.field = field;
        operator = FilterOperator.LESS;
        values.add(value);
    }

    public void between(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
        this.field = field;
        operator = BETWEEN;
        values.add(startValue);
        values.add(endValue);
    }

    public void notBetween(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
        this.field = field;
        operator = NOT_BETWEEN;
        values.add(startValue);
        values.add(endValue);
    }

    public void startsWith(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
        this.field = field;
        operator = STARTS_WITH;
        values.add(pattern);
    }

    public void endsWith(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
        this.field = field;
        operator = ENDS_WITH;
        values.add(pattern);
    }

    public void contains(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
        this.field = field;
        operator = CONTAINS;
        values.add(pattern);
    }
}

