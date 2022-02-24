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


    public SpaceStream<Type, Meta> limit(long value) {
        operators.add(mapOf(LIMIT, value));
        return this;
    }

    public SpaceStream<Type, Meta> offset(long value) {
        operators.add(mapOf(OFFSET, value));
        return this;
    }

    public SpaceStream<Type, Meta> range(long offset, long limit) {
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

    public SpaceStream<Type, Meta> filter(UnaryOperator<Filter<Type, Meta>> filter) {
        operators.add(mapOf(FILTER, filter.apply(new Filter<>())));
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
    public static class Filter<Type, Meta extends MetaClass<Type>> {
        private MetaField<Meta, ?> field;
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

        public <FieldType> Filter<Type, Meta> equal(MetaField<Meta, FieldType> field, FieldType value) {
            this.field = field;
            operator = EQUALS;
            values.add(value);
            return this;
        }

        public <FieldType> Filter<Type, Meta> notEqual(MetaField<Meta, FieldType> field, FieldType value) {
            this.field = field;
            operator = NOT_EQUALS;
            values.add(value);
            return this;
        }

        public Filter<Type, Meta> moreThan(MetaField<Meta, ? extends Number> field, Number value) {
            this.field = field;
            operator = FilterOperator.MORE;
            values.add(value.longValue());
            return this;
        }

        public Filter<Type, Meta> lessThan(MetaField<Meta, ? extends Number> field, Number value) {
            this.field = field;
            operator = FilterOperator.LESS;
            values.add(value.longValue());
            return this;
        }

        public Filter<Type, Meta> in(MetaField<Meta, ? extends Number> field, Number startValue, Number endValue) {
            this.field = field;
            operator = IN;
            values.add(startValue.longValue());
            values.add(endValue.longValue());
            return this;
        }

        public Filter<Type, Meta> notIn(MetaField<Meta, ? extends Number> field, Number startValue, Number endValue) {
            this.field = field;
            operator = NOT_IN;
            values.add(startValue.longValue());
            values.add(endValue.longValue());
            return this;
        }

        public Filter<Type, Meta> like(MetaField<Meta, String> field, String pattern) {
            this.field = field;
            operator = LIKE;
            values.add(pattern);
            return this;
        }

        public Filter<Type, Meta> startsWith(MetaField<Meta, String> field, String pattern) {
            this.field = field;
            operator = STARTS_WITH;
            values.add(pattern);
            return this;
        }

        public Filter<Type, Meta> endsWith(MetaField<Meta, String> field, String pattern) {
            this.field = field;
            operator = ENDS_WITH;
            values.add(pattern);
            return this;
        }

        public Filter<Type, Meta> contains(MetaField<Meta, String> field, String pattern) {
            this.field = field;
            operator = CONTAINS;
            values.add(pattern);
            return this;
        }
    }
}
