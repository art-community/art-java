package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.FilterOperator.*;
import static java.util.Arrays.*;
import static lombok.AccessLevel.*;
import java.util.*;

@Getter
@RequiredArgsConstructor(access = PACKAGE)
public class FilterBySpaceUseValues<Current, Other, FieldType> {
    protected final FilterRule<Current> rule;
    protected final MetaField<? extends MetaClass<Other>, FieldType> field;
    protected FilterOperator operator;
    protected final List<FieldType> values = linkedList();

    public FilterRule<Current> equal(FieldType value) {
        operator = EQUALS;
        this.values.add(value);
        return rule;
    }

    public FilterRule<Current> notEqual(FieldType value) {
        operator = NOT_EQUALS;
        this.values.add(value);
        return rule;
    }

    public FilterRule<Current> in(List<FieldType> values) {
        this.operator = IN;
        this.values.addAll(values);
        return rule;
    }

    public FilterRule<Current> notIn(List<FieldType> values) {
        this.operator = NOT_IN;
        this.values.addAll(values);
        return rule;
    }

    @SafeVarargs
    public final FilterRule<Current> in(FieldType... values) {
        return in(asList(values));
    }

    @SafeVarargs
    public final FilterRule<Current> notIn(FieldType... values) {
        return notIn(asList(values));
    }
}
