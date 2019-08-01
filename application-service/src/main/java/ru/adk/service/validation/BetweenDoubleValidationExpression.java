package ru.adk.service.validation;


import lombok.Builder;
import static java.text.MessageFormat.format;
import static ru.adk.service.constants.ServiceExceptionsMessages.NOT_BETWEEN_VALIDATION_ERROR;

@Builder
class BetweenDoubleValidationExpression extends ValidationExpression<Double> {
    private double lowerValue;
    private double greaterValue;

    @Override
    public boolean evaluate(String fieldName, Double value) {
        return super.evaluate(fieldName, value) && value < lowerValue && value > greaterValue;
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NOT_BETWEEN_VALIDATION_ERROR, fieldName, value, lowerValue, greaterValue);
    }
}

