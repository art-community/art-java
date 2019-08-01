package ru.art.service.validation;

import static java.text.MessageFormat.format;
import static ru.art.service.constants.ServiceExceptionsMessages.NULL_VALIDATION_ERROR;

class NotNullValidationExpression extends ValidationExpression<Object> {
    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NULL_VALIDATION_ERROR, fieldName);
    }
}
