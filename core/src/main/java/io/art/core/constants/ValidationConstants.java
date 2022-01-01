package io.art.core.constants;

public interface ValidationConstants {
    interface ValidationExpressionType {
        String name();
    }

    enum ValidationExpressionTypes implements ValidationExpressionType {
        BETWEEN_DOUBLE,
        BETWEEN_INT,
        BETWEEN_LONG,
        CONTAINS,
        EQUALS,
        NOT_EMPTY_COLLECTION,
        NOT_EMPTY_MAP,
        NOT_EMPTY_STRING,
        NOT_EQUALS,
        NOT_NULL
    }


    interface ValidationErrorPatterns {
        String NOT_BETWEEN_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' not between ''{2,number,#}'' and ''{3,number,#}''";
        String NOT_EQUALS_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' is not equals to ''{2}''";
        String NOT_CONTAINS_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' is not contains to ''{2}''";
        String EQUALS_VALIDATION_ERROR = "Validation error. ''{0}'' = ''{1}'' is equals to ''{2}''";
        String EMPTY_VALIDATION_ERROR = "Validation error. ''{0}'' is empty";
        String NULL_VALIDATION_ERROR = "Validation error. ''{0}'' is null";
        String INPUT_IS_NULL = "Validation error. Input is null";
    }
}
