package io.art.storage;

import io.art.meta.model.*;
import io.art.storage.StorageConstants.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.FilterOperator.*;
import static io.art.storage.StorageConstants.FilterWithMode.*;
import static java.util.Arrays.*;
import java.util.*;

@RequiredArgsConstructor
public class FilterWith<Current, Other> {
    private FilterWithMode mode;

    private final MetaClass<Other> mappingSpace;
    private MetaField<? extends MetaClass<Current>, ?> mappingKey;
    private List<MetaField<? extends MetaClass<Current>, ?>> mappingIndexedFields = linkedList();

    private MetaField<? extends MetaClass<Current>, ?> field;
    private FilterOperator operator;
    private final List<Object> values = linkedList();

    private final Filter<Current> delegate = new Filter<>();

    public FilterWith<Current, Other> byKey(MetaField<? extends MetaClass<Current>, ?> field) {
        this.mappingKey = field;
        mode = KEY;
        return this;
    }

    public FilterWith<Current, Other> byIndex(MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
        this.mappingIndexedFields = asList(indexedFields);
        mode = INDEX;
        return this;
    }

    public <FieldType> void equal(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType> value) {
        this.field = field;
        operator = EQUALS;
        this.values.add(value);
    }

    public <FieldType> void notEqual(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType> value) {
        this.field = field;
        operator = NOT_EQUALS;
        this.values.add(value);
    }

    public <FieldType> void in(MetaField<? extends MetaClass<Current>, FieldType> field, List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.field = field;
        this.operator = IN;
        this.values.addAll(values);
    }

    public <FieldType> void notIn(MetaField<? extends MetaClass<Current>, FieldType> field, List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
        this.field = field;
        this.operator = NOT_IN;
        this.values.addAll(values);
    }

    @SafeVarargs
    public final <FieldType> void in(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType>... values) {
        in(field, asList(values));
    }

    @SafeVarargs
    public final <FieldType> void notIn(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType>... values) {
        notIn(field, asList(values));
    }

    public void moreThan(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, Number> other) {
        this.field = current;
        this.operator = MORE;
        values.add(other.index());
    }

    public void lessThan(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, Number> other) {
        this.field = current;
        this.operator = LESS;
        values.add(other.index());
    }

    public void between(MetaField<? extends MetaClass<Current>, String> current,
                        MetaField<? extends MetaClass<Other>, Number> otherStart,
                        MetaField<? extends MetaClass<Other>, Number> otherEnd) {
        this.field = current;
        this.operator = BETWEEN;
        values.add(otherStart.index());
        values.add(otherEnd.index());
    }

    public void notBetween(MetaField<? extends MetaClass<Current>, String> current,
                           MetaField<? extends MetaClass<Other>, Number> otherStart,
                           MetaField<? extends MetaClass<Other>, Number> otherEnd) {
        this.field = current;
        this.operator = NOT_BETWEEN;
        values.add(otherStart.index());
        values.add(otherEnd.index());
    }

    public void startsWith(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
        this.field = current;
        this.operator = STARTS_WITH;
        values.add(other.index());
    }

    public void endsWith(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
        this.field = current;
        this.operator = ENDS_WITH;
        values.add(other.index());
    }

    public void contains(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
        this.field = current;
        this.operator = CONTAINS;
        values.add(other.index());
    }
}
