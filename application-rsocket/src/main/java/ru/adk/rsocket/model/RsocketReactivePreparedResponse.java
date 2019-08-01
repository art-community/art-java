package ru.adk.rsocket.model;

import lombok.Builder;
import lombok.Getter;
import reactor.core.publisher.GroupedFlux;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.service.model.ServiceRequest;
import static ru.adk.service.factory.ServiceRequestFactory.newServiceRequest;

@Getter
@Builder
public class RsocketReactivePreparedResponse {
    private final ServiceRequest<?> serviceRequest;
    private ValueFromModelMapper<?, ?> responseMapper;

    @SuppressWarnings("ConstantConditions")
    public static RsocketReactivePreparedResponse fromGroupedFlux(GroupedFlux<RsocketReactiveGroupKey, RsocketRequestReactiveContext> group) {
        return RsocketReactivePreparedResponse.builder()
                .responseMapper(group.key().getResponseMapper())
                .serviceRequest(newServiceRequest(group.key().getServiceMethodCommand(), group.map(RsocketRequestReactiveContext::getRequestData), group.key().getValidationPolicy()))
                .build();
    }
}
