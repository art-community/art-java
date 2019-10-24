package ru.art.service.validation;

import ru.art.service.constants.*;
import static ru.art.service.constants.ValidationExpressionType.*;
import java.text.*;
import java.util.*;

class NotEmptyCollectionValidationExpression extends ValidationExpression<Collection> {
    NotEmptyCollectionValidationExpression() {
        super(NOT_EMPTY_COLLECTION);
    }

    @Override
    public boolean evaluate(String fieldName, Collection value) {
        return super.evaluate(fieldName, value) && !value.isEmpty();
    }

    public String getValidationErrorMessage() {
        return MessageFormat.format(ServiceExceptionsMessages.EMPTY_VALIDATION_ERROR, fieldName);
    }
}
