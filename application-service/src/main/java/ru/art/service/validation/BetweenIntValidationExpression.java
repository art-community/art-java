package ru.art.service.validation;


import lombok.*;
import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;

@Builder
class BetweenIntValidationExpression extends ValidationExpression<Integer> {
    private Integer lowerValue;
    private Integer greaterValue;

    BetweenIntValidationExpression(Integer lowerValue, Integer greaterValue) {
        super(BETWEEN_INT);
        this.lowerValue = lowerValue;
        this.greaterValue = greaterValue;
    }

    @Override
    public boolean evaluate(String fieldName, Integer value) {
        return super.evaluate(fieldName, value) && value < lowerValue && value > greaterValue;
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NOT_BETWEEN_VALIDATION_ERROR, fieldName, value, lowerValue, greaterValue);
    }
}
