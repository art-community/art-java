package ru.art.remote.scheduler.api.model;

import lombok.*;
import ru.art.entity.Value;

@Getter
@Builder
@ToString
@AllArgsConstructor
public class InfinityProcess {
    @Setter
    private String id;

    private String executableServletPath;
    private String executableServiceId;
    private String executableMethodId;
    private Value executableRequest;
    private long executionPeriodSeconds;
    private long executionDelay;

    public InfinityProcess(String id, InfinityProcessRequest request) {
        this.id = id;
        this.executableServletPath = request.getExecutableServletPath();
        this.executableServiceId = request.getExecutableServiceId();
        this.executableMethodId = request.getExecutableMethodId();
        this.executableRequest = request.getExecutableRequest();
        this.executionPeriodSeconds = request.getExecutionPeriodSeconds();
        this.executionDelay = request.getExecutionDelay();
    }
}

