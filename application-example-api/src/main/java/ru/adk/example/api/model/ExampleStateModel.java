package ru.adk.example.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@EqualsAndHashCode
public class ExampleStateModel implements Validatable {
    private Integer serviceRequests;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("serviceRequests", serviceRequests, notNull());
    }
}
