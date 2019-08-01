package ru.adk.rsocket.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.service.constants.RequestValidationPolicy;
import ru.adk.service.model.ServiceMethodCommand;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketReactiveGroupKey {
    @EqualsAndHashCode.Include
    private final ServiceMethodCommand serviceMethodCommand;
    private final RequestValidationPolicy validationPolicy;
    private ValueFromModelMapper<?, ?> responseMapper;
}