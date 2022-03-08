package io.art.storage.filter.implementation;

import io.art.storage.constants.StorageConstants.*;
import io.art.storage.filter.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.constants.StorageConstants.FilterOperator.*;
import static java.util.Arrays.*;
import static lombok.AccessLevel.*;
import java.util.*;

@Getter
@RequiredArgsConstructor(access = PACKAGE)
public class FilterBySpaceUseValuesImplementation<Type, FieldType> implements FilterBySpaceUseValues<Type, FieldType> {
    protected final FilterRule<Type> rule;
    protected FilterOperator operator;
    protected final List<FieldType> values = linkedList();

    @Override
    public FilterRule<Type> equal(FieldType value) {
        operator = EQUALS;
        this.values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> notEqual(FieldType value) {
        operator = NOT_EQUALS;
        this.values.add(value);
        return rule;
    }

    @Override
    public FilterRule<Type> in(List<FieldType> values) {
        this.operator = IN;
        this.values.addAll(values);
        return rule;
    }

    @Override
    public FilterRule<Type> notIn(List<FieldType> values) {
        this.operator = NOT_IN;
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
