package ru.adk.service.validation;

import static ru.adk.core.caster.Caster.cast;
import java.util.List;

public interface ValidationExpressions {
    static <T> ValidationExpression<T> notNull() {
        return cast(new NotNullValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyString() {
        return cast(new NotEmptyStringValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyCollection() {
        return cast(new NotEmptyCollectionValidationExpression());
    }

    static <T> ValidationExpression<T> notEmptyMap() {
        return cast(new NotEmptyMapValidationExpression());
    }

    static <T> ValidationExpression<T> notEqualsOther(Object other) {
        return cast(new NotEqualsValidationExpression(other));
    }

    static <T> ValidationExpression<T> equalsOther(Object other) {
        return cast(new EqualsValidationExpression(other));
    }

    static <T> ValidationExpression<T> containsOther(List<Object> list) {
        return cast(new ContainsValidationExpression(list));
    }

    static <T> ValidationExpression<T> betweenInt(Integer lower, Integer greater) {
        return cast(BetweenIntValidationExpression.builder()
                .greaterValue(greater)
                .lowerValue(lower)
                .build());
    }

    static <T> ValidationExpression<T> betweenLong(Long lower, Long greater) {
        return cast(BetweenLongValidationExpression.builder()
                .greaterValue(greater)
                .lowerValue(lower)
                .build());
    }

    static <T> ValidationExpression<T> moreThanLong(Long lower) {
        return cast(BetweenLongValidationExpression.builder()
                .greaterValue(lower)
                .lowerValue(Long.MAX_VALUE)
                .build());
    }

    static <T> ValidationExpression<T> lessThanLong(Long greater) {
        return cast(BetweenLongValidationExpression.builder()
                .greaterValue(Long.MIN_VALUE)
                .lowerValue(greater)
                .build());
    }

    static <T> ValidationExpression<T> moreThanInt(Integer lower) {
        return cast(BetweenIntValidationExpression.builder()
                .greaterValue(lower)
                .lowerValue(Integer.MAX_VALUE)
                .build());
    }

    static <T> ValidationExpression<T> lessThanInt(Integer greater) {
        return cast(BetweenIntValidationExpression.builder()
                .greaterValue(Integer.MIN_VALUE)
                .lowerValue(greater)
                .build());
    }

    static <T> ValidationExpression<T> betweenDouble(Double lower, Double greater) {
        return cast(BetweenDoubleValidationExpression.builder()
                .greaterValue(greater)
                .lowerValue(lower)
                .build());
    }
}
