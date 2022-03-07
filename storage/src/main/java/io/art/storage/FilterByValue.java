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
public class FilterByValue<Type> {
    private final MetaField<? extends MetaClass<Type>, ?> field;
    private final FilterCondition condition;
    private FilterOperator operator;
    private final List<Object> values = linkedList();

    public <FieldType> void equal(FieldType value) {
        operator = EQUALS;
        values.add(value);
    }

    public <FieldType> void notEqual(FieldType value) {
        operator = NOT_EQUALS;
        values.add(value);
    }

    public <FieldType> void in(List<FieldType> values) {
        operator = IN;
        this.values.addAll(values);
    }

    public <FieldType> void notIn(List<FieldType> values) {
        operator = NOT_IN;
        this.values.addAll(values);
    }

    @SafeVarargs
    public final <FieldType> void in(FieldType... values) {
        in(asList(values));
    }

    @SafeVarargs
    public final <FieldType> void notIn(FieldType... values) {
        notIn(asList(values));
    }

    public void moreThan(Number value) {
        operator = FilterOperator.MORE;
        values.add(value);
    }

    public void lessThan(Number value) {
        operator = FilterOperator.LESS;
        values.add(value);
    }

    public void between(Number startValue, Number endValue) {
        operator = BETWEEN;
        values.add(startValue);
        values.add(endValue);
    }

    public void notBetween(Number startValue, Number endValue) {
        operator = NOT_BETWEEN;
        values.add(startValue);
        values.add(endValue);
    }

    public void startsWith(String pattern) {
        operator = STARTS_WITH;
        values.add(pattern);
    }

    public void endsWith(String pattern) {
        operator = ENDS_WITH;
        values.add(pattern);
    }

    public void contains(String pattern) {
        operator = CONTAINS;
        values.add(pattern);
    }
}
