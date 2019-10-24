package ru.art.service.validation;

import ru.art.service.constants.*;
import static java.text.MessageFormat.*;
import static ru.art.service.constants.ValidationExpressionType.*;
import java.util.*;

public class ContainsValidationExpression extends ValidationExpression<Object> {
    private List<Object> objectList;

    ContainsValidationExpression(List<Object> objectList) {
        super(CONTAINS);
        this.objectList = objectList;
    }

    @Override
    public boolean evaluate(String fieldName, Object value) {
        return super.evaluate(fieldName, value) && objectList.contains(value);
    }

    @Override
    public String getValidationErrorMessage() {
        return format(ServiceExceptionsMessages.NOT_CONTAINS_VALIDATION_ERROR, fieldName, value, objectList.toString());
    }
}
