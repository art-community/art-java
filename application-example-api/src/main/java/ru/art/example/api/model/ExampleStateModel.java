package ru.art.example.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notNull;

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
