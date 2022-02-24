package io.art.storage;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.PairFactory.*;
import static io.art.storage.SpaceStream.FilterOperator.*;
import static io.art.storage.SpaceStream.SortOrder.*;
import static io.art.storage.SpaceStream.StreamOperation.*;
import java.util.*;
import java.util.function.*;

public abstract class SpaceStream<Type> {
    protected final List<Pair<StreamOperation, Object>> operators = linkedList();

    public SpaceStream<Type> limit(long value) {
        operators.add(pairOf(LIMIT, value));
        return this;
    }

    public SpaceStream<Type> offset(long value) {
        operators.add(pairOf(OFFSET, value));
        return this;
    }

    public SpaceStream<Type> range(long offset, long limit) {
        return offset(offset).limit(limit);
    }

    public SpaceStream<Type> distinct() {
        operators.add(pairOf(DISTINCT, null));
        return this;
    }

    public <FieldType> SpaceStream<Type> sort(MetaField<? extends MetaClass<Type>, FieldType> current, UnaryOperator<Sorter<Type, FieldType>> sorter) {
        operators.add(pairOf(SORT, sorter.apply(new Sorter<>(current))));
        return this;
    }

    public SpaceStream<Type> filter(Consumer<Filter<Type>> filter) {
        Filter<Type> newFilter = new Filter<>();
        filter.accept(newFilter);
        operators.add(pairOf(FILTER, newFilter));
        return this;
    }

    public SpaceStream<Type> refresh() {
        operators.clear();
        return this;
    }

    public abstract ImmutableArray<Type> collect();

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

        public void in(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
            this.field = field;
            operator = IN;
            values.add(startValue);
            values.add(endValue);
        }

        public void notIn(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
            this.field = field;
            operator = NOT_IN;
            values.add(startValue);
            values.add(endValue);
        }

        public void like(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
            this.field = field;
            operator = LIKE;
            values.add(pattern);
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
        IN,
        NOT_IN,
        LIKE,
        STARTS_WITH,
        ENDS_WITH,
        CONTAINS
    }

    public enum StreamOperation {
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
}
