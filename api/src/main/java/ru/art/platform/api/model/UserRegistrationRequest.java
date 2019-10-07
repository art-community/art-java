package ru.art.platform.api.model;

import lombok.*;
import ru.art.service.validation.*;
import static ru.art.service.validation.ValidationExpressions.notEmptyString;

@Value
@Builder
public class UserRegistrationRequest implements Validatable {
    private final String name;
    private final String password;
    private final String email;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("name", name, notEmptyString())
                .validate("email", email, notEmptyString())
                .validate("password", password, notEmptyString());
    }
}
