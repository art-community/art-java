package ru.art.service.validation;

import lombok.*;
import ru.art.service.constants.*;

@Getter
public abstract class ValidationExpression<T> {
    protected String fieldName;
    protected T value;
    protected final ValidationExpressionType type;

    protected ValidationExpression(ValidationExpressionType type) {
        this.type = type;
    }

    public boolean evaluate(String fieldName, T value) {
        this.fieldName = fieldName;
        this.value = value;
        return value != null;
    }

    public abstract String getValidationErrorMessage();
}
