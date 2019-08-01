package ru.art.service.validation;


import lombok.Builder;
import static java.text.MessageFormat.format;
import static ru.art.service.constants.ServiceExceptionsMessages.NOT_BETWEEN_VALIDATION_ERROR;

@Builder
class BetweenIntValidationExpression extends ValidationExpression<Integer> {
    private Integer lowerValue;
    private Integer greaterValue;

    @Override
    public boolean evaluate(String fieldName, Integer value) {
        return super.evaluate(fieldName, value) && value < lowerValue && value > greaterValue;
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NOT_BETWEEN_VALIDATION_ERROR, fieldName, value, lowerValue, greaterValue);
    }
}
