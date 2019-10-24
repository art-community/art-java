package ru.art.service.validation;

import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;

class NotNullValidationExpression extends ValidationExpression<Object> {
    NotNullValidationExpression() {
        super(NOT_NULL);
    }

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NULL_VALIDATION_ERROR, fieldName);
    }
}
