package ru.adk.service.model;

import lombok.*;
import ru.adk.service.constants.RequestValidationPolicy;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@RequiredArgsConstructor
public class ServiceRequest<T> {
    @EqualsAndHashCode.Include
    private final ServiceMethodCommand serviceMethodCommand;
    private final RequestValidationPolicy validationPolicy;
    private T requestData;
}
