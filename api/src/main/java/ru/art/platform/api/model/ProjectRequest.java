package ru.art.platform.api.model;

import lombok.*;
import ru.art.service.validation.*;
import static ru.art.service.validation.ValidationExpressions.*;

@Value
@Builder
public class ProjectRequest implements Validatable {
    private final String title;
    private final String gitUrl;
    private final String jiraUrl;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("title", title, notEmptyString())
                .validate("gitUrl", gitUrl, notEmptyString());
    }
}
