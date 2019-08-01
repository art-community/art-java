package ru.adk.remote.scheduler.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.entity.Value;
import ru.adk.service.validation.Validatable;
import ru.adk.service.validation.Validator;
import static ru.adk.service.validation.ValidationExpressions.notNull;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PeriodicTaskRequest implements Validatable {
    private String executableServletPath;
    private String executableServiceId;
    private String executableMethodId;
    private Value executableRequest;
    private LocalDateTime executionDateTime;
    private int maxExecutionCount;
    private long executionPeriodSeconds;
    private boolean finishAfterCompletion;

    @Override
    public void onValidating(Validator validator) {
        validator.validate("executableServletPath", executableServletPath, notNull());
        validator.validate("executableServiceId", executableServiceId, notNull());
        validator.validate("executableMethodId", executableMethodId, notNull());
    }
}
