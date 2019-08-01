package ru.art.service.validation;

import static java.text.MessageFormat.format;
import static ru.art.service.constants.ServiceExceptionsMessages.EMPTY_VALIDATION_ERROR;

class NotEmptyStringValidationExpression extends ValidationExpression<String> {
    @Override
    public boolean evaluate(String fieldName, String value) {
        return super.evaluate(fieldName, value) && !value.isEmpty();
    }

    public String getValidationErrorMessage() {
        return format(EMPTY_VALIDATION_ERROR, fieldName);
    }
}
