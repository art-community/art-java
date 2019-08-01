package ru.art.rsocket.model;

import io.rsocket.Payload;
import lombok.Builder;
import lombok.Getter;
import ru.art.entity.Entity;
import ru.art.entity.Value;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.reactive.service.model.ReactiveService;
import ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceRequest;
import static java.util.Objects.isNull;
import static reactor.core.publisher.Flux.just;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.entity.Value.asEntity;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.REACTIVE;
import static ru.art.rsocket.constants.RsocketModuleConstants.REQUEST_DATA;
import static ru.art.rsocket.extractor.EntityServiceCommandExtractor.extractServiceMethodCommand;
import static ru.art.rsocket.model.RsocketReactiveMethods.fromCommand;
import static ru.art.rsocket.reader.RsocketPayloadReader.readPayload;
import static ru.art.service.factory.ServiceRequestFactory.newServiceRequest;

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
