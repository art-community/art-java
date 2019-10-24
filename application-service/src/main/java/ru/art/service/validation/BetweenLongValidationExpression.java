package ru.art.service.validation;


import lombok.*;
import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;

@Builder
class BetweenLongValidationExpression extends ValidationExpression<Long> {
    private long lowerValue;
    private long greaterValue;

    BetweenLongValidationExpression(long lowerValue, long greaterValue) {
        super(BETWEEN_LONG);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
    }

    @Override
    public boolean evaluate(String fieldName, Long value) {
        return super.evaluate(fieldName, value) && value > lowerValue && value < greaterValue;
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NOT_BETWEEN_VALIDATION_ERROR, fieldName, value, lowerValue, greaterValue);
    }
}
