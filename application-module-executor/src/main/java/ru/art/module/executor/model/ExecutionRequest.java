package ru.art.module.executor.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.art.entity.Value;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notEmptyString;

/**
 * Request with information about calling module
 */

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ExecutionRequest implements Validatable {
    private String moduleHost;
    @Builder.Default
    private final int modulePort = 80; //default port
    private String executableServletPath;
    private String executableServiceId;
    private String executableMethodId;
    private Value executableRequest;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("moduleHost", moduleHost, notEmptyString());
        validator.validate("executableServletPath", executableServletPath, notEmptyString());
        validator.validate("executableServiceId", executableServiceId, notEmptyString());
        validator.validate("executableMethodId", executableMethodId, notEmptyString());
    }
}

