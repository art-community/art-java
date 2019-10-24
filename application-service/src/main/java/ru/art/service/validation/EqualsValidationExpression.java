package ru.art.service.validation;

import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;

class EqualsValidationExpression extends ValidationExpression<Object> {
    private Object other;

    EqualsValidationExpression(Object other) {
        super(EQUALS);
        this.other = other;
    }

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value) && value.equals(other);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NOT_EQUALS_VALIDATION_ERROR, fieldName, value, other);
    }

}
