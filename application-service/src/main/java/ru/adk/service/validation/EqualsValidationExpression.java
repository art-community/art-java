package ru.adk.service.validation;

import lombok.AllArgsConstructor;
import static java.text.MessageFormat.format;
import static ru.adk.service.constants.ServiceExceptionsMessages.NOT_EQUALS_VALIDATION_ERROR;

@AllArgsConstructor
class EqualsValidationExpression extends ValidationExpression<Object> {
    private Object other;

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value) && value.equals(other);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NOT_EQUALS_VALIDATION_ERROR, fieldName, value, other);
    }

}
