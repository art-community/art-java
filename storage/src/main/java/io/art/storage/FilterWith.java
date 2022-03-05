package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.FilterOperator.*;
import static io.art.storage.StorageConstants.FilterWithMode.*;
import static java.util.Arrays.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class FilterWith<Current, Other> {
    private FilterWithMode mode;

    private final MetaClass<Other> mappingSpace;
    private MetaField<? extends MetaClass<Current>, ?> mappingKeyField;
    private List<MetaField<? extends MetaClass<Current>, ?>> mappingIndexedFields = linkedList();

    private MetaField<? extends MetaClass<Current>, ?> currentField;
    private FilterOperator operator;
    private final List<MetaField<? extends MetaClass<Other>, ?>> filterableFields = linkedList();

    public FilterWith<Current, Other> byKey(MetaField<? extends MetaClass<Current>, ?> field) {
        this.mappingKeyField = field;
        mode = KEY;
        return this;
    }

    @SafeVarargs
    public final FilterWith<Current, Other> byIndex(MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        this.mappingIndexedFields = asList(indexedFields);
        mode = INDEX;
        return this;
    }

    public <FieldType> void equal(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType> value) {
        this.currentField = field;
        operator = EQUALS;
        this.filterableFields.add(value);
    }

    public <FieldType> void notEqual(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType> value) {
        this.currentField = field;
        operator = NOT_EQUALS;
        this.filterableFields.add(value);
    }

    public <FieldType> void in(MetaField<? extends MetaClass<Current>, FieldType> field, List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.currentField = field;
        this.operator = IN;
        this.filterableFields.addAll(values);
    }

    public <FieldType> void notIn(MetaField<? extends MetaClass<Current>, FieldType> field, List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.currentField = field;
        this.operator = NOT_IN;
        this.filterableFields.addAll(values);
    }

    @SafeVarargs
    public final <FieldType> void in(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType>... values) {
        in(field, asList(values));
    }

    @SafeVarargs
    public final <FieldType> void notIn(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType>... values) {
        notIn(field, asList(values));
    }

    public void moreThan(MetaField<? extends MetaClass<Current>, ? extends Number> current, MetaField<? extends MetaClass<Other>, ? extends Number> other) {
        this.currentField = current;
        this.operator = MORE;
        filterableFields.add(other);
    }

    public void lessThan(MetaField<? extends MetaClass<Current>, ? extends Number> current, MetaField<? extends MetaClass<Other>, ? extends Number> other) {
        this.currentField = current;
        this.operator = LESS;
        filterableFields.add(other);
    }

    public void between(MetaField<? extends MetaClass<Current>, ? extends Number> current,
                        MetaField<? extends MetaClass<Other>, ? extends Number> otherStart,
                        MetaField<? extends MetaClass<Other>, ? extends Number> otherEnd) {
        this.currentField = current;
        this.operator = BETWEEN;
        filterableFields.add(otherStart);
        filterableFields.add(otherEnd);
    }

    public void notBetween(MetaField<? extends MetaClass<Current>, ? extends Number> current,
                           MetaField<? extends MetaClass<Other>, ? extends Number> otherStart,
                           MetaField<? extends MetaClass<Other>, ? extends Number> otherEnd) {
        this.currentField = current;
        this.operator = NOT_BETWEEN;
        filterableFields.add(otherStart);
        filterableFields.add(otherEnd);
    }

    public void startsWith(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
        this.currentField = current;
        this.operator = STARTS_WITH;
        filterableFields.add(other);
    }

    public void endsWith(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
        this.currentField = current;
        this.operator = ENDS_WITH;
        filterableFields.add(other);
    }

    public void contains(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
        this.currentField = current;
        this.operator = CONTAINS;
        filterableFields.add(other);
    }
}
