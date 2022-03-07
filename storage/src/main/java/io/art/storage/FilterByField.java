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
public class FilterByField<Type, FieldType> {
    protected final FilterRule<Type> rule;
    protected final MetaField<? extends MetaClass<Type>, FieldType> field;
    protected FilterOperator operator;
    protected final List<Object> values = linkedList();

    public FilterRule<Type> equal(FieldType value) {
        operator = EQUALS;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> notEqual(FieldType value) {
        operator = NOT_EQUALS;
        values.add(value);
        return rule;
    }

    public FilterRule<Type> in(List<FieldType> values) {
        operator = IN;
        this.values.addAll(values);
        return rule;
    }

    public FilterRule<Type> notIn(List<FieldType> values) {
        operator = NOT_IN;
        this.values.addAll(values);
        return rule;
    }

    @SafeVarargs
    public final FilterRule<Type> in(FieldType... values) {
        return in(asList(values));
    }

    @SafeVarargs
    public final FilterRule<Type> notIn(FieldType... values) {
        return notIn(asList(values));
    }
}
