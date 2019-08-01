package ru.adk.state.api.model;

import lombok.*;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.moreThanInt;
import static ru.adk.service.validation.ValidationExpressions.notEmptyString;

@Getter
@Builder
@ToString
@EqualsAndHashCode(of = {"host", "port"})
public class ModuleEndpoint implements Validatable {
    private final String host;
    private final Integer port;

    @Setter
    @Builder.Default
    @SuppressWarnings("UnusedAssignment")
    private Integer sessions = 0;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("host", host, notEmptyString()).validate("port", port, moreThanInt(0));
    }
}
