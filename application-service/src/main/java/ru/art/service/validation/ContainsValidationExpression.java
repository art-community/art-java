package ru.art.service.validation;

import lombok.AllArgsConstructor;
import static java.text.MessageFormat.format;
import static ru.art.service.constants.ServiceExceptionsMessages.NOT_CONTAINS_VALIDATION_ERROR;
import java.util.List;

@AllArgsConstructor
public class ContainsValidationExpression extends ValidationExpression<Object> {
    private List<Object> objectList;

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value) && objectList.contains(value);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(NOT_CONTAINS_VALIDATION_ERROR, fieldName, value, objectList.toString());
    }
}
