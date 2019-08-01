package ru.art.service.validation;

import static java.text.MessageFormat.format;
import static ru.art.service.constants.ServiceExceptionsMessages.EMPTY_VALIDATION_ERROR;
import java.util.Collection;

class NotEmptyCollectionValidationExpression extends ValidationExpression<Collection> {
    @Override
    public boolean evaluate(String fieldName, Collection value) {
        return super.evaluate(fieldName, value) && !value.isEmpty();
    }

    public String getValidationErrorMessage() {
        return format(EMPTY_VALIDATION_ERROR, fieldName);
    }
}
