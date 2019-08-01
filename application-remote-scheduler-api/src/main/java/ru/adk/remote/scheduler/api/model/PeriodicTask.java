package ru.adk.remote.scheduler.api.model;

import lombok.*;
import ru.adk.entity.Value;
import ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus;
import java.time.LocalDateTime;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PeriodicTask {
    @Setter
    private String id;

    private TaskStatus status;
    private LocalDateTime creationDateTime;
    private int executionCount;
    private String executableServletPath;
    private String executableServiceId;
    private String executableMethodId;
    private Value executableRequest;
    private LocalDateTime executionDateTime;
    private int maxExecutionCount;
    private long executionPeriodSeconds;
    private boolean finishAfterCompletion;
}
