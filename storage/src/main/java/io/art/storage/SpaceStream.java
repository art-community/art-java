package io.art.storage;

import io.art.core.collection.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.storage.SpaceStream.Filter.FilterOperator.*;
import static io.art.storage.SpaceStream.Sorter.SortComparator.LESS;
import static io.art.storage.SpaceStream.Sorter.SortComparator.MORE;
import static io.art.storage.SpaceStream.Sorter.SortOrder.*;
import static io.art.storage.SpaceStream.StreamOperation.*;
import java.util.*;
import java.util.function.*;

public abstract class SpaceStream<Type, Meta extends MetaClass<Type>> {
    protected final List<Map<StreamOperation, Object>> operators = linkedList();


    public SpaceStream<Type, Meta> limit(int value) {
        operators.add(mapOf(LIMIT, value));
        return this;
    }

    public SpaceStream<Type, Meta> offset(int value) {
        operators.add(mapOf(OFFSET, value));
        return this;
    }

    public SpaceStream<Type, Meta> range(int offset, int limit) {
        return offset(offset).limit(limit);
    }

    public SpaceStream<Type, Meta> distinct() {
        operators.add(mapOf(DISTINCT, null));
        return this;
    }

    public <FieldType> SpaceStream<Type, Meta> sort(MetaField<Meta, FieldType> current, UnaryOperator<Sorter<Type, Meta, FieldType>> sorter) {
        operators.add(mapOf(SORT, sorter.apply(new Sorter<>(current))));
        return this;
    }

    public <FieldType> SpaceStream<Type, Meta> filter(MetaField<Meta, FieldType> current, UnaryOperator<Filter<Type, Meta, FieldType>> filter) {
        operators.add(mapOf(FILTER, filter.apply(new Filter<>(current))));
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
    public static class Sorter<Type, Meta extends MetaClass<Type>, FieldType> {
        private final MetaField<Meta, FieldType> field;
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

        public Sorter<Type, Meta, FieldType> ascendant() {
            order = ASCENDANT;
            return this;
        }

        public Sorter<Type, Meta, FieldType> descendant() {
            order = DESCENDANT;
            return this;
        }

        public Sorter<Type, Meta, FieldType> more() {
            comparator = MORE;
            return this;
        }

        public Sorter<Type, Meta, FieldType> less() {
            comparator = LESS;
            return this;
        }
    }


    @Getter
    @RequiredArgsConstructor
    public static class Filter<Type, Meta extends MetaClass<Type>, FieldType> {
        private final MetaField<Meta, FieldType> current;
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

        public Filter<Type, Meta, FieldType> equal(FieldType value) {
            operator = EQUALS;
            values.add(value);
            return this;
        }

        public Filter<Type, Meta, FieldType> notEqual(FieldType value) {
            operator = NOT_EQUALS;
            values.add(value);
            return this;
        }

        public Filter<Type, Meta, FieldType> moreThan(FieldType value) {
            operator = FilterOperator.MORE;
            values.add(value);
            return this;
        }

        public Filter<Type, Meta, FieldType> lessThan(FieldType value) {
            operator = FilterOperator.LESS;
            values.add(value);
            return this;
        }

        public Filter<Type, Meta, FieldType> in(FieldType startValue, FieldType endValue) {
            operator = IN;
            values.add(startValue);
            values.add(endValue);
            return this;
        }

        public Filter<Type, Meta, FieldType> notIn(FieldType startValue, FieldType endValue) {
            operator = NOT_IN;
            values.add(startValue);
            values.add(endValue);
            return this;
        }

        public Filter<Type, Meta, FieldType> like(String pattern) {
            operator = LIKE;
            values.add(pattern);
            return this;
        }

        public Filter<Type, Meta, FieldType> startsWith(String pattern) {
            operator = STARTS_WITH;
            values.add(pattern);
            return this;
        }

        public Filter<Type, Meta, FieldType> endsWith(String pattern) {
            operator = ENDS_WITH;
            values.add(pattern);
            return this;
        }

        public Filter<Type, Meta, FieldType> contains(String pattern) {
            operator = CONTAINS;
            values.add(pattern);
            return this;
        }
    }
}
