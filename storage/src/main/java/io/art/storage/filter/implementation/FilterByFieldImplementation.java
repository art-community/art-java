package io.art.storage.filter.implementation;

import io.art.meta.model.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;
import static java.util.Arrays.*;
import java.util.*;

@RequiredArgsConstructor
public class FilterByFieldImplementation<Type, FieldType> implements FilterByField<Type, FieldType> {
    protected final FilterRule<Type> rule;
    @Getter
    protected final MetaField<? extends MetaClass<Type>, FieldType> field;
    @Getter
    protected FilterOperator operator;
    @Getter
    protected final List<Object> values = linkedList();

    @Override
    public FilterRule<Type> equal(FieldType value) {
        operator = EQUALS;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> notEqual(FieldType value) {
        operator = NOT_EQUALS;
        values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> in(List<FieldType> values) {
        operator = IN;
        this.values.addAll(values);
        return rule;
    }

    @Override
    public FilterRule<Type> notIn(List<FieldType> values) {
        operator = NOT_IN;
        this.values.addAll(values);
        return rule;
    }

    @Override
    @SafeVarargs
    public final FilterRule<Type> in(FieldType... values) {
        return in(asList(values));
    }

    @Override
    @SafeVarargs
    public final FilterRule<Type> notIn(FieldType... values) {
        return notIn(asList(values));
    }
}
