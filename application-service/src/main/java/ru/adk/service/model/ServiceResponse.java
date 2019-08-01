package ru.adk.service.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.adk.service.exception.ServiceExecutionException;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ServiceResponse<T> {
    private final ServiceMethodCommand command;
    private T responseData;
    private ServiceExecutionException serviceException;
}
