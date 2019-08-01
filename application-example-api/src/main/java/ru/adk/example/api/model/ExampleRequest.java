package ru.adk.example.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notNull;
import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExampleRequest implements Validatable {
    private ExampleModel innerModel;
    private Integer primitiveTypeInt;
    private List<String> collection;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("innerModel", innerModel, notNull());
        validator.validate("primitiveTypeInt", primitiveTypeInt, notNull());
        validator.validate("collection", collection, notNull());
    }
}
