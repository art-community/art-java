package ru.adk.example.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExampleResponse implements Validatable {
    private String responseMessage;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("responseMessage", responseMessage, notNull());
    }
}
