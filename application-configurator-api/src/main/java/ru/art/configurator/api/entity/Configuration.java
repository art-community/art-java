package ru.art.configurator.api.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.entity.Entity;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notNull;

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

