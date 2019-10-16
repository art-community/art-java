package ru.art.platform.api.model;

import lombok.*;
import ru.art.service.validation.*;
import static ru.art.service.validation.ValidationExpressions.*;

@Value
@Builder
public class BuildRequest implements Validatable {
    private final Long projectId;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("projectId", projectId, notNull());
    }
}
