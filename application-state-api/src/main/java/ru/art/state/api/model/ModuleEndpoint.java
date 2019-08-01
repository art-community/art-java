package ru.art.state.api.model;

import lombok.*;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.moreThanInt;
import static ru.art.service.validation.ValidationExpressions.notEmptyString;

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
