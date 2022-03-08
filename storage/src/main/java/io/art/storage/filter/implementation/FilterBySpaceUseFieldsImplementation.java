package io.art.storage.filter.implementation;

import io.art.meta.model.*;
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
public class FilterBySpaceUseFieldsImplementation<Current, Other, FieldType> implements FilterBySpaceUseFields<Current, Other, FieldType> {
    protected final FilterRule<Current> rule;
    protected FilterOperator operator;
    protected final List<MetaField<? extends MetaClass<Other>, FieldType>> fields = linkedList();

    @Override
    public FilterRule<Current> equal(MetaField<? extends MetaClass<Other>, FieldType> otherField) {
        operator = EQUALS;
        this.fields.add(otherField);
        return rule;
    }

    @Override
    public FilterRule<Current> notEqual(MetaField<? extends MetaClass<Other>, FieldType> otherField) {
        operator = NOT_EQUALS;
        this.fields.add(otherField);
        return rule;
    }

    @Override
    public FilterRule<Current> in(List<MetaField<? extends MetaClass<Other>, FieldType>> otherFields) {
        this.operator = IN;
        this.fields.addAll(otherFields);
        return rule;
    }

    @Override
    public FilterRule<Current> notIn(List<MetaField<? extends MetaClass<Other>, FieldType>> otherFields) {
        this.operator = NOT_IN;
        this.fields.addAll(otherFields);
        return rule;
    }

    @Override
    @SafeVarargs
    public final FilterRule<Current> in(MetaField<? extends MetaClass<Other>, FieldType>... otherFields) {
        return in(asList(otherFields));
    }

    @Override
    @SafeVarargs
    public final FilterRule<Current> notIn(MetaField<? extends MetaClass<Other>, FieldType>... otherFields) {
        return notIn(asList(otherFields));
    }
}
