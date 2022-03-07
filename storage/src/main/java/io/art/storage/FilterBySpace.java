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
public class FilterBySpace<Current, Other> {
    private final FilterRule<Current> rule;
    private final MetaClass<Other> mappingSpace;
    private final MetaField<? extends MetaClass<Current>, ?> currentField;

    private MetaField<? extends MetaClass<Current>, ?> mappingKeyField;
    private List<MetaField<? extends MetaClass<Current>, ?>> mappingIndexedFields = linkedList();

    private FilterOperator operator;
    private final List<MetaField<? extends MetaClass<Other>, ?>> filterableFields = linkedList();

    FilterBySpace<Current, Other> bySpace(MetaField<? extends MetaClass<Current>, ?> mappingField) {
        mappingKeyField = mappingField;
        return this;
    }

    FilterBySpace<Current, Other> byIndex(MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        mappingIndexedFields = asList(indexedFields);
        return this;
    }

    public <FieldType> FilterRule<Current> equal(MetaField<? extends MetaClass<Other>, FieldType> value) {
        operator = EQUALS;
        this.filterableFields.add(value);
        return rule;
    }

    public <FieldType> FilterRule<Current> notEqual(MetaField<? extends MetaClass<Other>, FieldType> value) {
        operator = NOT_EQUALS;
        this.filterableFields.add(value);
        return rule;
    }

    public <FieldType> FilterRule<Current> in(List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.operator = IN;
        this.filterableFields.addAll(values);
        return rule;
    }

    public <FieldType> FilterRule<Current> notIn(List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.operator = NOT_IN;
        this.filterableFields.addAll(values);
        return rule;
    }

    @SafeVarargs
    public final <FieldType> FilterRule<Current> in(MetaField<? extends MetaClass<Other>, FieldType>... values) {
        return in(asList(values));
    }

    @SafeVarargs
    public final <FieldType> FilterRule<Current> notIn(MetaField<? extends MetaClass<Other>, FieldType>... values) {
        return notIn(asList(values));
    }

    public FilterRule<Current> moreThan(MetaField<? extends MetaClass<Other>, ? extends Number> other) {
        this.operator = MORE;
        filterableFields.add(other);
        return rule;
    }

    public FilterRule<Current> lessThan(MetaField<? extends MetaClass<Other>, ? extends Number> other) {
        this.operator = LESS;
        filterableFields.add(other);
        return rule;
    }

    public FilterRule<Current> between(MetaField<? extends MetaClass<Other>, ? extends Number> otherStart, MetaField<? extends MetaClass<Other>, ? extends Number> otherEnd) {
        this.operator = BETWEEN;
        filterableFields.add(otherStart);
        filterableFields.add(otherEnd);
        return rule;
    }

    public FilterRule<Current> notBetween(MetaField<? extends MetaClass<Other>, ? extends Number> otherStart, MetaField<? extends MetaClass<Other>, ? extends Number> otherEnd) {
        this.operator = NOT_BETWEEN;
        filterableFields.add(otherStart);
        filterableFields.add(otherEnd);
        return rule;
    }

    public FilterRule<Current> startsWith(MetaField<? extends MetaClass<Other>, String> other) {
        this.operator = STARTS_WITH;
        filterableFields.add(other);
        return rule;
    }

    public FilterRule<Current> endsWith(MetaField<? extends MetaClass<Other>, String> other) {
        this.operator = ENDS_WITH;
        filterableFields.add(other);
        return rule;
    }

    public FilterRule<Current> contains(MetaField<? extends MetaClass<Other>, String> other) {
        this.operator = CONTAINS;
        filterableFields.add(other);
        return rule;
    }
}
