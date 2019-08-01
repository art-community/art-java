package ru.adk.service.validation;

import ru.adk.service.exception.ValidationException;

public class Validator {
    @SafeVarargs
    public final <T> Validator validate(String fieldName, T value, ValidationExpression<T>... validationExpressions) {
        for (ValidationExpression<T> validationExpression : validationExpressions) {
            if (!validationExpression.evaluate(fieldName, value)) {
                throw new ValidationException(validationExpression.getValidationErrorMessage());
            }
        }
        return this;
    }
}
