package ru.art.example.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExampleModel implements Validatable {
    private String exampleStringField;
    private Integer exampleIntegerField;
    private boolean exampleBooleanField;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("exampleStringField", exampleStringField, notNull());
        validator.validate("exampleIntegerField", exampleIntegerField, notNull());
        validator.validate("exampleBooleanField", exampleBooleanField, notNull());
    }
}
