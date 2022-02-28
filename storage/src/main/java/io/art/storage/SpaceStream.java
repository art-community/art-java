package io.art.storage;

import io.art.core.collection.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.storage.SpaceStream.FilterOperator.*;
import static io.art.storage.SpaceStream.ProcessingOperation.*;
import static io.art.storage.SpaceStream.SortOrder.*;
import java.util.*;
import java.util.function.*;

public abstract class SpaceStream<Type> {
    protected List<ProcessingOperator> operators = linkedList();

    public SpaceStream<Type> limit(long value) {
        operators.add(new ProcessingOperator(LIMIT, value));
        return this;
    }

    public SpaceStream<Type> offset(long value) {
        operators.add(new ProcessingOperator(OFFSET, value));
        return this;
    }

    public SpaceStream<Type> range(long offset, long limit) {
        return offset(offset).limit(limit);
    }

    public SpaceStream<Type> distinct() {
        operators.add(new ProcessingOperator(DISTINCT, null));
        return this;
    }

    public <FieldType> SpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        operators.add(new ProcessingOperator(SORT, sorter.apply(new Sorter<>(current))));
        return this;
    }

    public SpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        Filter<Type> newFilter = new Filter<>();
        filter.accept(newFilter);
        operators.add(new ProcessingOperator(FILTER, newFilter));
        return this;
    }

    public SpaceStream<Type> refresh() {
        operators = linkedList();
        return this;
    }

    public abstract ImmutableArray<Type> collect();

    public abstract long count();

    public abstract boolean all(Consumer<Filter<Type>> filter);

    public abstract boolean any(Consumer<Filter<Type>> filter);

    @Getter
    @RequiredArgsConstructor
    public static class Sorter<Type, FieldType> {
        private final MetaField<? extends MetaClass<Type>, FieldType> field;
        private SortOrder order = SortOrder.ASCENDANT;
        private SortComparator comparator = SortComparator.MORE;

        public Sorter<Type, FieldType> ascendant() {
            order = SortOrder.ASCENDANT;
            return this;
        }

        public Sorter<Type, FieldType> descendant() {
            order = DESCENDANT;
            return this;
        }

        public Sorter<Type, FieldType> more() {
            comparator = SortComparator.MORE;
            return this;
        }

        public Sorter<Type, FieldType> less() {
            comparator = SortComparator.LESS;
            return this;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Filter<Type> {
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
            in(field, Arrays.asList(values));
        }

        @SafeVarargs
        public final <FieldType> void notIn(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType... values) {
            notIn(field, Arrays.asList(values));
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
    }

    public enum FilterOperator {
        EQUALS,
        NOT_EQUALS,
        MORE,
        LESS,
        BETWEEN,
        NOT_BETWEEN,
        IN,
        NOT_IN,
        STARTS_WITH,
        ENDS_WITH,
        CONTAINS
    }

    public enum ProcessingOperation {
        LIMIT,
        OFFSET,
        DISTINCT,
        SORT,
        FILTER
    }

    public enum SortOrder {
        DESCENDANT,
        ASCENDANT
    }

    public enum SortComparator {
        MORE,
        LESS
    }

    @Getter
    @AllArgsConstructor
    public static class ProcessingOperator {
        private final ProcessingOperation operation;
        private final Object value;
    }
}
