package ru.art.service.validation;


import lombok.*;
import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;

@Builder
class BetweenDoubleValidationExpression extends ValidationExpression<Double> {
    private double lowerValue;
    private double greaterValue;

    BetweenDoubleValidationExpression(double lowerValue, double greaterValue) {
        super(BETWEEN_DOUBLE);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
    }

    @Override
    public boolean evaluate(String fieldName, Double value) {
        return super.evaluate(fieldName, value) && value < lowerValue && value > greaterValue;
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NOT_BETWEEN_VALIDATION_ERROR, fieldName, value, lowerValue, greaterValue);
    }
}

