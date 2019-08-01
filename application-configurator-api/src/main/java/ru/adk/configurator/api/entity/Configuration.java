package ru.adk.configurator.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.entity.Entity;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Configuration implements Validatable {
    private final Entity configuration;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("configuration", configuration, notNull());
    }
}

