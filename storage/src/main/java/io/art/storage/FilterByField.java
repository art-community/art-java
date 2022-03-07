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
public class FilterByField<Type> {
    private final FilterRule<Type> rule;
    private final MetaField<? extends MetaClass<Type>, ?> field;
    private FilterOperator operator;
    private final List<Object> values = linkedList();

    public <FieldType> FilterRule<Type> equal(FieldType value) {
        operator = EQUALS;
        values.add(value);
        return rule;
    }

    public <FieldType> FilterRule<Type> notEqual(FieldType value) {
        operator = NOT_EQUALS;
        values.add(value);
        return rule;
    }

    public <FieldType> FilterRule<Type> in(List<FieldType> values) {
        operator = IN;
        this.values.addAll(values);
        return rule;
    }

    public <FieldType> FilterRule<Type> notIn(List<FieldType> values) {
        operator = NOT_IN;
        this.values.addAll(values);
        return rule;
    }

    @SafeVarargs
    public final <FieldType> FilterRule<Type> in(FieldType... values) {
        return in(asList(values));
    }

    @SafeVarargs
    public final <FieldType> FilterRule<Type> notIn(FieldType... values) {
        return notIn(asList(values));
    }

    public FilterRule<Type> moreThan(Number value) {
        operator = FilterOperator.MORE;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> lessThan(Number value) {
        operator = FilterOperator.LESS;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> between(Number startValue, Number endValue) {
        operator = BETWEEN;
        values.add(startValue);
        values.add(endValue);
        return rule;
    }

    public FilterRule<Type> notBetween(Number startValue, Number endValue) {
        operator = NOT_BETWEEN;
        values.add(startValue);
        values.add(endValue);
        return rule;
    }

    public FilterRule<Type> startsWith(String pattern) {
        operator = STARTS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Type> endsWith(String pattern) {
        operator = ENDS_WITH;
        values.add(pattern);
        return rule;
    }

    public FilterRule<Type> contains(String pattern) {
        operator = CONTAINS;
        values.add(pattern);
        return rule;
    }
}
