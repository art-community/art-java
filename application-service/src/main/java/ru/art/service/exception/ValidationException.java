package ru.art.service.exception;

import lombok.*;
import ru.art.service.constants.*;
import ru.art.service.validation.*;

@Getter
public class ValidationException extends RuntimeException {
    private String fieldName;
    private ValidationExpressionType type;
    private ValidationExpression<?> expression;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String fieldName, String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public ValidationException(String fieldName, String message, ValidationExpressionType type) {
        super(message);
        this.fieldName = fieldName;
        this.type = type;
    }

    public ValidationException(ValidationExpression<?> expression) {
        super(expression.getValidationErrorMessage());
        this.fieldName = expression.getFieldName();
        this.type = expression.getType();
        this.expression = expression;
    }
}
