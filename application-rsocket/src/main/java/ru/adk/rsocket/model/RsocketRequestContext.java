package ru.adk.rsocket.model;

import io.rsocket.Payload;
import lombok.Builder;
import lombok.Getter;
import ru.adk.entity.Entity;
import ru.adk.entity.Value;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.reactive.service.model.ReactiveService;
import ru.adk.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.adk.service.model.ServiceMethodCommand;
import ru.adk.service.model.ServiceRequest;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Flux.just;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.entity.Value.asEntity;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.REACTIVE;
import static ru.adk.rsocket.constants.RsocketModuleConstants.REQUEST_DATA;
import static ru.adk.rsocket.extractor.EntityServiceCommandExtractor.extractServiceMethodCommand;
import static ru.adk.rsocket.model.RsocketReactiveMethods.fromCommand;
import static ru.adk.rsocket.reader.RsocketPayloadReader.readPayload;
import static ru.adk.service.factory.ServiceRequestFactory.newServiceRequest;

@Getter
@Builder
public class RsocketRequestContext {
    private final ServiceRequest<?> request;
    private final RsocketReactiveMethods rsocketReactiveMethods;

    @SuppressWarnings("Duplicates")
    public static RsocketRequestContext fromPayload(Payload payload, RsocketDataFormat dataFormat) {
        Entity serviceRequestEntity = asEntity(readPayload(payload, dataFormat));
        ServiceMethodCommand command = extractServiceMethodCommand(serviceRequestEntity);
        RsocketReactiveMethods rsocketServiceMethods = fromCommand(command);
        ValueToModelMapper<?, ?> requestMapper;
        Value requestDataValue;
        if (isNull(requestMapper = rsocketServiceMethods.getRsocketMethod().requestMapper()) || isEmpty(requestDataValue = serviceRequestEntity.getValue(REQUEST_DATA))) {
            return RsocketRequestContext.builder()
                    .request(newServiceRequest(command, rsocketServiceMethods.getRsocketMethod().validationPolicy()))
                    .rsocketReactiveMethods(rsocketServiceMethods)
                    .build();
        }
        Object requestData = requestMapper.map(cast(requestDataValue));

        ReactiveService.ReactiveMethod reactiveMethod = rsocketServiceMethods.getReactiveMethod();
        if (reactiveMethod.requestProcessingMode() == REACTIVE) {
            return RsocketRequestContext.builder()
                    .request(newServiceRequest(command, just(requestData), rsocketServiceMethods.getRsocketMethod().validationPolicy()))
                    .rsocketReactiveMethods(rsocketServiceMethods)
                    .build();
        }
        return RsocketRequestContext.builder()
                .request(newServiceRequest(command, requestData, rsocketServiceMethods.getRsocketMethod().validationPolicy()))
                .rsocketReactiveMethods(rsocketServiceMethods)
                .build();
    }
}
