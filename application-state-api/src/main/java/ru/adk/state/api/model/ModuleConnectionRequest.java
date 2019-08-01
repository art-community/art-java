package ru.adk.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notEmptyString;
import static ru.adk.service.validation.ValidationExpressions.notNull;

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
