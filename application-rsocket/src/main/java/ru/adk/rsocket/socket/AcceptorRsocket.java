package ru.adk.rsocket.socket;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.rsocket.model.RsocketReactivePreparedResponse;
import ru.adk.rsocket.model.RsocketRequestContext;
import ru.adk.rsocket.model.RsocketRequestReactiveContext;
import ru.adk.rsocket.service.RsocketService;
import ru.adk.rsocket.state.RsocketModuleState.CurrentRsocketState;
import ru.adk.service.model.ServiceResponse;
import static java.util.Objects.nonNull;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.never;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.extension.NullCheckingExtensions.getOrElse;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.REACTIVE;
import static ru.adk.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.STRAIGHT;
import static ru.adk.rsocket.model.RsocketRequestContext.fromPayload;
import static ru.adk.rsocket.module.RsocketModule.rsocketModuleState;
import static ru.adk.rsocket.selector.RsocketDataFormatMimeTypeConverter.fromMimeType;
import static ru.adk.rsocket.writer.ServiceResponsePayloadWriter.writeResponseReactive;
import static ru.adk.rsocket.writer.ServiceResponsePayloadWriter.writeServiceResponse;
import static ru.adk.service.ServiceController.executeServiceMethodUnchecked;
import static ru.adk.service.ServiceModule.serviceModule;

public class AcceptorRsocket extends AbstractRSocket {
    public AcceptorRsocket(RSocket socket, ConnectionSetupPayload setupPayload) {
        rsocketModuleState().currentRocketState(new CurrentRsocketState(setupPayload.dataMimeType(), setupPayload.metadataMimeType(), socket));
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        executeServiceMethodUnchecked(fromPayload(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType())).getRequest());
        return never();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketRequestContext context = fromPayload(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType()));
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        RsocketService.RsocketMethod rsocketMethod = context.getRsocketReactiveMethods().getRsocketMethod();
        ValueFromModelMapper<?, ?> responseMapper = rsocketMethod.responseMapper();
        return context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT ?
                Mono.just(writeServiceResponse(responseMapper, serviceResponse, getOrElse(rsocketMethod.overrideResponseDataFormat(), fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType())))) :
                writeResponseReactive(rsocketMethod.responseMapper(), cast(serviceResponse), fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType())).next();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketRequestContext context = fromPayload(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType()));
        if (context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT) {
            return Flux.never();
        }
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        RsocketService.RsocketMethod rsocketMethod = context.getRsocketReactiveMethods().getRsocketMethod();
        return writeResponseReactive(rsocketMethod.responseMapper(), cast(serviceResponse), fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType()));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return from(payloads)
                .map(payload -> RsocketRequestReactiveContext.fromPayload(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType())))
                .filter(context -> nonNull(context.getRsocketReactiveMethods().getRsocketMethod()))
                .filter(context -> nonNull(context.getRsocketReactiveMethods().getReactiveMethod()))
                .filter(context -> context.getRsocketReactiveMethods().getReactiveMethod().requestProcessingMode() == REACTIVE)
                .filter(context -> context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == REACTIVE)
                .groupBy(RsocketRequestReactiveContext::getRsocketReactiveGroupKey, serviceModule().getServiceRegistry().getServices().size())
                .map(RsocketReactivePreparedResponse::fromGroupedFlux)
                .flatMap(preparedResponse -> writeResponseReactive(preparedResponse.getResponseMapper(), executeServiceMethodUnchecked(preparedResponse.getServiceRequest()), fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType())));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        executeServiceMethodUnchecked(fromPayload(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType())).getRequest());
        return never();
    }

}
