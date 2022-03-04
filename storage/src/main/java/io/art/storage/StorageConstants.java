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

    enum FilterWithMode {
        KEY,
        INDEX
    }


    public enum ProcessingOperation {
        LIMIT,
        OFFSET,
        DISTINCT,
        SORT,
        FILTER,
        FILTER_WITH
    }
}
