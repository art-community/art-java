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
public class FilterBySpaceUseFields<Current, Other, FieldType> {
    protected final FilterRule<Current> rule;
    protected final MetaField<? extends MetaClass<Current>, FieldType> field;
    protected FilterOperator operator;
    protected final List<MetaField<? extends MetaClass<Other>, ?>> fields = linkedList();

    public FilterRule<Current> equal(MetaField<? extends MetaClass<Other>, FieldType> value) {
        operator = EQUALS;
        this.fields.add(value);
        return rule;
    }

    public FilterRule<Current> notEqual(MetaField<? extends MetaClass<Other>, FieldType> value) {
        operator = NOT_EQUALS;
        this.fields.add(value);
        return rule;
    }

    public FilterRule<Current> in(List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.operator = IN;
        this.fields.addAll(values);
        return rule;
    }

    public FilterRule<Current> notIn(List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.operator = NOT_IN;
        this.fields.addAll(values);
        return rule;
    }

    @SafeVarargs
    public final FilterRule<Current> in(MetaField<? extends MetaClass<Other>, FieldType>... values) {
        return in(asList(values));
    }

    @SafeVarargs
    public final FilterRule<Current> notIn(MetaField<? extends MetaClass<Other>, FieldType>... values) {
        return notIn(asList(values));
    }
}
