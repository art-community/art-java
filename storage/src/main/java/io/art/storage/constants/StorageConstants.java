package io.art.storage.constants;

public interface StorageConstants {
    enum SortOrder {
        DESCENDANT,
        ASCENDANT
    }

    enum SortComparator {
        MORE,
        LESS
    }

    enum FilterOperator {
        EQUALS,
        NOT_EQUALS,
        MORE,
        MORE_EQUALS,
        LESS,
        LESS_EQUALS,
        BETWEEN,
        NOT_BETWEEN,
        IN,
        NOT_IN,
        STARTS_WITH,
        ENDS_WITH,
        CONTAINS
    }

    enum FilterMode {
        FIELD,
        FUNCTION,
        SPACE,
        INDEX,
        NESTED
    }

    enum FilterExpressionType {
        FIELD,
        STRING_FIELD,
        NUMBER_FIELD,
        VALUE,
        STRING_VALUE,
        NUMBER_VALUE,
    }

    enum FilterCondition {
        AND,
        OR
    }

    enum MappingMode {
        FIELD,
        FUNCTION,
        SPACE,
        INDEX
    }

    enum ProcessingOperation {
        LIMIT,
        OFFSET,
        DISTINCT,
        SORT,
        FILTER,
        MAP
    }

    enum UpdateOpration {
        ADD,
        SUBTRACT,
        BITWISE_AND,
        BITWISE_OR,
        BITWISE_XOR,
        SET,
        SPLICE
    }
}
