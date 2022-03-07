package io.art.storage;

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
        LESS,
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
        VALUE
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
}
