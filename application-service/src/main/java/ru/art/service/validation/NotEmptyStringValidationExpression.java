package ru.art.service.validation;

import ru.art.service.constants.*;
import static ru.art.service.constants.ValidationExpressionType.*;
import java.text.*;

class NotEmptyStringValidationExpression extends ValidationExpression<String> {
    NotEmptyStringValidationExpression() {
        super(NOT_EMPTY_STRING);
    }

    @Override
    public boolean evaluate(String fieldName, String value) {
        return super.evaluate(fieldName, value) && !value.isEmpty();
    }

    public String getValidationErrorMessage() {
        return MessageFormat.format(ServiceExceptionsMessages.EMPTY_VALIDATION_ERROR, fieldName);
    }
}
