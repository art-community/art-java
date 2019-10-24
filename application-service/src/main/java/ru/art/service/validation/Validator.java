package ru.art.service.validation;

import ru.art.service.exception.*;

public class Validator {
    @SafeVarargs
    public final <T> Validator validate(String fieldName, T value, ValidationExpression<T>... validationExpressions) {
        for (ValidationExpression<T> validationExpression : validationExpressions) {
            if (!validationExpression.evaluate(fieldName, value)) {
                throw new ValidationException(validationExpression);
            }
        }
        return this;
    }
}
