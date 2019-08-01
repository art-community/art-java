package ru.art.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notEmptyString;

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
