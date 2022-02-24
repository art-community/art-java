package io.art.storage;

import io.art.meta.model.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.storage.SpaceStream.Filter.FilterOperator.*;
import static io.art.storage.SpaceStream.Sorter.SortComparator.LESS;
import static io.art.storage.SpaceStream.Sorter.SortComparator.MORE;
import static io.art.storage.SpaceStream.Sorter.SortOrder.*;
import static io.art.storage.SpaceStream.StreamOperation.*;
import java.util.*;
import java.util.function.*;

public class SpaceStream<Stream extends SpaceStream<Stream, ?, ?>, Type extends Class<?>, Meta extends MetaClass<Type>> {
    private final List<Map<StreamOperation, Object>> operators = linkedList();

    public Stream limit(int value) {
        operators.add(mapOf(LIMIT, value));
        return cast(this);
    }

    public Stream offset(int value) {
        operators.add(mapOf(OFFSET, value));
        return cast(this);
    }

    public Stream range(int offset, int limit) {
        return offset(offset).limit(limit);
    }

    public Stream distinct() {
        operators.add(mapOf(DISTINCT, null));
        return cast(this);
    }

    public <FieldType> Stream sort(MetaField<Meta, FieldType> current, UnaryOperator<Sorter<Type, Meta, FieldType>> sorter) {
        operators.add(mapOf(SORT, sorter.apply(new Sorter<>(current))));
        return cast(this);
    }

    public <FieldType> Stream filter(MetaField<Meta, FieldType> current, UnaryOperator<Filter<Type, Meta, FieldType>> filter) {
        operators.add(mapOf(FILTER, filter));
        return cast(this);
    }

    public List<Map<StreamOperation, Object>> collect() {
        return operators;
    }

    public enum StreamOperation {
        LIMIT,
        OFFSET,
        DISTINCT,
        SORT,
        FILTER
    }

    @Getter
    @RequiredArgsConstructor
    public static class Sorter<Type extends Class<?>, Meta extends MetaClass<Type>, FieldType> {
        private final MetaField<Meta, FieldType> current;
        private SortOrder order = ASCENDANT;
        private SortComparator comparator = MORE;
        private MetaField<Meta, FieldType> other;

        enum SortOrder {
            DESCENDANT,
            ASCENDANT
        }

        enum SortComparator {
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

        public Sorter<Type, Meta, FieldType> moreThan(MetaField<Meta, FieldType> field) {
            comparator = MORE;
            other = field;
            return this;
        }

        public Sorter<Type, Meta, FieldType> lessThan(MetaField<Meta, FieldType> field) {
            comparator = LESS;
            other = field;
            return this;
        }
    }


    @Getter
    @RequiredArgsConstructor
    public static class Filter<Type extends Class<?>, Meta extends MetaClass<Type>, FieldType> {
        private final MetaField<Meta, FieldType> current;
        private FilterOperator operator;
        private final List<Object> values = linkedList();

        enum FilterOperator {
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
