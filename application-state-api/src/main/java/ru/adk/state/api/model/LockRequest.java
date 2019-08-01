package ru.adk.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notEmptyString;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class LockRequest implements Validatable {
    private final String name;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("name", name, notEmptyString());
    }
}
