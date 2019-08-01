package ru.art.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notEmptyString;
import static ru.art.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ModuleConnectionRequest implements Validatable {
    private final String profile;
    private final String modulePath;
    private final ModuleEndpoint moduleEndpoint;

    @Override
    public void onValidating(Validator validator) {
        validator
                .validate("profile", profile, notEmptyString())
                .validate("modulePath", modulePath, notEmptyString())
                .validate("moduleEndpoint", moduleEndpoint, notNull());
        moduleEndpoint.onValidating(validator);
    }
}
