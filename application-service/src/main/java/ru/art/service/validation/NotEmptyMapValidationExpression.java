package ru.art.service.validation;

import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;
import java.util.*;

class NotEmptyMapValidationExpression extends ValidationExpression<Map> {
    NotEmptyMapValidationExpression() {
        super(NOT_EMPTY_MAP);
    }

    @Override
    public boolean evaluate(String fieldName, Map value) {
        return super.evaluate(fieldName, value) && !value.isEmpty();
    }

    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.EMPTY_VALIDATION_ERROR, fieldName);
    }
}
