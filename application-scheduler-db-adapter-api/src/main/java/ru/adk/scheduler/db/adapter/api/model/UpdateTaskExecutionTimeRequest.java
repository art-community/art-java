package ru.adk.scheduler.db.adapter.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class UpdateTaskExecutionTimeRequest {
    private String taskId;
    private long executionPeriodSeconds;
}
