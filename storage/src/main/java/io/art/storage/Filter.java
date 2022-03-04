package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.StorageConstants.*;
import static io.art.storage.StorageConstants.FilterOperator.*;
import static io.art.storage.StorageConstants.FilterWithMode.*;
import static java.util.Arrays.*;
import java.util.*;

@Getter
@RequiredArgsConstructor
public class Filter<Type> {
    private MetaField<? extends MetaClass<Type>, ?> field;
    private FilterOperator operator;
    private final List<Object> values = linkedList();

    public <FieldType> void equal(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType value) {
        this.field = field;
        operator = EQUALS;
        values.add(value);
    }

    public <FieldType> void notEqual(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType value) {
        this.field = field;
        operator = NOT_EQUALS;
        values.add(value);
    }

    public <FieldType> void in(MetaField<? extends MetaClass<Type>, FieldType> field, List<FieldType> values) {
        this.field = field;
        operator = IN;
        this.values.addAll(values);
    }

    public <FieldType> void notIn(MetaField<? extends MetaClass<Type>, FieldType> field, List<FieldType> values) {
        this.field = field;
        operator = NOT_IN;
        this.values.addAll(values);
    }

    @SafeVarargs
    public final <FieldType> void in(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType... values) {
        in(field, asList(values));
    }

    @SafeVarargs
    public final <FieldType> void notIn(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType... values) {
        notIn(field, asList(values));
    }

    public void moreThan(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number value) {
        this.field = field;
        operator = FilterOperator.MORE;
        values.add(value);
    }

    public void lessThan(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number value) {
        this.field = field;
        operator = FilterOperator.LESS;
        values.add(value);
    }

    public void between(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
        this.field = field;
        operator = BETWEEN;
        values.add(startValue);
        values.add(endValue);
    }

    public void notBetween(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
        this.field = field;
        operator = NOT_BETWEEN;
        values.add(startValue);
        values.add(endValue);
    }

    public void startsWith(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
        this.field = field;
        operator = STARTS_WITH;
        values.add(pattern);
    }

    public void endsWith(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
        this.field = field;
        operator = ENDS_WITH;
        values.add(pattern);
    }

    public void contains(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
        this.field = field;
        operator = CONTAINS;
        values.add(pattern);
    }

    public <Other> void with(MetaClass<Other> mappingSpace) {
        operator = WITH;
        values.add(new KeyFilter<Type, Other>(mappingSpace));
    }

    @RequiredArgsConstructor
    public class KeyFilter<Current, Other> {
        private FilterWithMode mode;

        private final MetaClass<Other> mappingSpace;
        private MetaField<? extends MetaClass<Current>, ?> mappingKey;
        private List<MetaField<? extends MetaClass<Current>, ?>> mappingIndexedFields = linkedList();

        private final Filter<Current> delegate = new Filter<>();

        public KeyFilter<Current, Other> byKey(MetaField<? extends MetaClass<Current>, ?> field) {
            this.mappingKey = field;
            mode = KEY;
            return this;
        }

        public KeyFilter<Current, Other> byIndex(MetaField<? extends MetaClass<Current>, ?>... indexedFields) {
            this.mappingIndexedFields = asList(indexedFields);
            mode = INDEX;
            return this;
        }

        public <FieldType> void equal(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType> value) {
            delegate.field = field;
            operator = EQUALS;
            delegate.values.add(value);
        }

        public <FieldType> void notEqual(MetaField<? extends MetaClass<Current>, FieldType> field, MetaField<? extends MetaClass<Other>, FieldType> value) {
            delegate.field = field;
            operator = NOT_EQUALS;
            delegate.values.add(value);
        }

        public <FieldType> void in(MetaField<? extends MetaClass<Current>, FieldType> field, List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
            delegate.field = field;
            delegate.operator = IN;
            delegate.values.addAll(values);
        }

        public <FieldType> void notIn(MetaField<? extends MetaClass<Current>, FieldType> field, List<MetaField<? extends MetaClass<Other>, FieldType>> values) {
            delegate.field = field;
            delegate.operator = NOT_IN;
            delegate.values.addAll(values);
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
            delegate.field = current;
            delegate.operator = MORE;
            values.add(other.index());
        }

        public void lessThan(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, Number> other) {
            delegate.field = current;
            delegate.operator = LESS;
            values.add(other.index());
        }

        public void between(MetaField<? extends MetaClass<Current>, String> current,
                            MetaField<? extends MetaClass<Other>, Number> otherStart,
                            MetaField<? extends MetaClass<Other>, Number> otherEnd) {
            delegate.field = current;
            delegate.operator = BETWEEN;
            values.add(otherStart.index());
            values.add(otherEnd.index());
        }

        public void notBetween(MetaField<? extends MetaClass<Current>, String> current,
                               MetaField<? extends MetaClass<Other>, Number> otherStart,
                               MetaField<? extends MetaClass<Other>, Number> otherEnd) {
            delegate.field = current;
            delegate.operator = NOT_BETWEEN;
            values.add(otherStart.index());
            values.add(otherEnd.index());
        }

        public void startsWith(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
            delegate.field = current;
            delegate.operator = STARTS_WITH;
            values.add(other.index());
        }

        public void endsWith(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
            delegate.field = current;
            delegate.operator = ENDS_WITH;
            values.add(other.index());
        }

        public void contains(MetaField<? extends MetaClass<Current>, String> current, MetaField<? extends MetaClass<Other>, String> other) {
            delegate.field = current;
            delegate.operator = CONTAINS;
            values.add(other.index());
        }
    }
}

