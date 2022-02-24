package io.art.storage;

import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.PairFactory.*;
import static io.art.storage.SpaceStream.Filter.FilterOperator.*;
import static io.art.storage.SpaceStream.Sorter.SortComparator.LESS;
import static io.art.storage.SpaceStream.Sorter.SortComparator.MORE;
import static io.art.storage.SpaceStream.Sorter.SortOrder.*;
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

    public SpaceStream<Type> filter(UnaryOperator<Filter<Type>> filter) {
        operators.add(pairOf(FILTER, filter.apply(new Filter<>())));
        return this;
    }

    public SpaceStream<Type> refresh() {
        operators.clear();
        return this;
    }

    public abstract ImmutableArray<Type> collect();

    public enum StreamOperation {
        LIMIT,
        OFFSET,
        DISTINCT,
        SORT,
        FILTER
    }

    @Getter
    @RequiredArgsConstructor
    public static class Sorter<Type, FieldType> {
        private final MetaField<? extends MetaClass<Type>, FieldType> field;
        private SortOrder order = ASCENDANT;
        private SortComparator comparator = MORE;

        public enum SortOrder {
            DESCENDANT,
            ASCENDANT
        }

        public enum SortComparator {
            MORE,
            LESS
        }

        public Sorter<Type, FieldType> ascendant() {
            order = ASCENDANT;
            return this;
        }

        public Sorter<Type, FieldType> descendant() {
            order = DESCENDANT;
            return this;
        }

        public Sorter<Type, FieldType> more() {
            comparator = MORE;
            return this;
        }

        public Sorter<Type, FieldType> less() {
            comparator = LESS;
            return this;
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class Filter<Type> {
        private MetaField<? extends MetaClass<Type>, ?> field;
        private FilterOperator operator;
        private final List<Object> values = linkedList();

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

        public <FieldType> Filter<Type> equal(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType value) {
            this.field = field;
            operator = EQUALS;
            values.add(value);
            return this;
        }

        public <FieldType> Filter<Type> notEqual(MetaField<? extends MetaClass<Type>, FieldType> field, FieldType value) {
            this.field = field;
            operator = NOT_EQUALS;
            values.add(value);
            return this;
        }

        public Filter<Type> moreThan(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number value) {
            this.field = field;
            operator = FilterOperator.MORE;
            values.add(value);
            return this;
        }

        public Filter<Type> lessThan(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number value) {
            this.field = field;
            operator = FilterOperator.LESS;
            values.add(value);
            return this;
        }

        public Filter<Type> in(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
            this.field = field;
            operator = IN;
            values.add(startValue);
            values.add(endValue);
            return this;
        }

        public Filter<Type> notIn(MetaField<? extends MetaClass<Type>, ? extends Number> field, Number startValue, Number endValue) {
            this.field = field;
            operator = NOT_IN;
            values.add(startValue);
            values.add(endValue);
            return this;
        }

        public Filter<Type> like(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
            this.field = field;
            operator = LIKE;
            values.add(pattern);
            return this;
        }

        public Filter<Type> startsWith(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
            this.field = field;
            operator = STARTS_WITH;
            values.add(pattern);
            return this;
        }

        public Filter<Type> endsWith(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
            this.field = field;
            operator = ENDS_WITH;
            values.add(pattern);
            return this;
        }

        public Filter<Type> contains(MetaField<? extends MetaClass<Type>, String> field, String pattern) {
            this.field = field;
            operator = CONTAINS;
            values.add(pattern);
            return this;
        }
    }
}
