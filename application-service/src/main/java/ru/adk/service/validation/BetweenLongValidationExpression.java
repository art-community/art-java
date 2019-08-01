package ru.adk.service.validation;


import lombok.Builder;
import static java.text.MessageFormat.format;
import static ru.adk.service.constants.ServiceExceptionsMessages.NOT_BETWEEN_VALIDATION_ERROR;

@Builder
class BetweenLongValidationExpression extends ValidationExpression<Long> {
    private long lowerValue;
    private long greaterValue;

    @Override
    public boolean evaluate(String fieldName, Long value) {
        return super.evaluate(fieldName, value) && value > lowerValue && value < greaterValue;
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NOT_BETWEEN_VALIDATION_ERROR, fieldName, value, lowerValue, greaterValue);
    }
}
