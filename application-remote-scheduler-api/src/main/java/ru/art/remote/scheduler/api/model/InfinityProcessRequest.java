package ru.art.remote.scheduler.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import ru.art.entity.Value;
import ru.art.service.validation.Validatable;
import ru.art.service.validation.Validator;
import static ru.art.service.validation.ValidationExpressions.notNull;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class InfinityProcessRequest implements Validatable {
    private String executableServletPath;
    private String executableServiceId;
    private String executableMethodId;
    private Value executableRequest;
    private long executionPeriodSeconds;
    private long executionDelay;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("executableServletPath", executableServletPath, notNull());
        validator.validate("executableServiceId", executableServiceId, notNull());
        validator.validate("executableMethodId", executableMethodId, notNull());
    }
}
