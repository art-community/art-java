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
public class ClusterProfileRequest implements Validatable {
    private final String profile;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("profile", profile, notEmptyString());
    }
}
