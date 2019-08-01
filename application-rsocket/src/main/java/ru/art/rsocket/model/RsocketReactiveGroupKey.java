package ru.art.rsocket.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.service.constants.RequestValidationPolicy;
import ru.art.service.model.ServiceMethodCommand;

@Getter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RsocketReactiveGroupKey {
    @EqualsAndHashCode.Include
    private final ServiceMethodCommand serviceMethodCommand;
    private final RequestValidationPolicy validationPolicy;
    private ValueFromModelMapper<?, ?> responseMapper;
}