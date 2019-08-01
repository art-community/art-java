package ru.adk.service.validation;

import static java.text.MessageFormat.format;
import static ru.adk.service.constants.ServiceExceptionsMessages.EMPTY_VALIDATION_ERROR;
import java.util.Map;

class NotEmptyMapValidationExpression extends ValidationExpression<Map> {
    @Override
    public boolean evaluate(String fieldName, Map value) {
        return super.evaluate(fieldName, value) && !value.isEmpty();
    }

    public String getValidationErrorMessage() {
        return format(EMPTY_VALIDATION_ERROR, fieldName);
    }
}
