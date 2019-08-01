package ru.art.rsocket.socket;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.rsocket.model.RsocketReactivePreparedResponse;
import ru.art.rsocket.model.RsocketRequestContext;
import ru.art.rsocket.model.RsocketRequestReactiveContext;
import ru.art.rsocket.service.RsocketService;
import ru.art.rsocket.state.RsocketModuleState.CurrentRsocketState;
import ru.art.service.model.ServiceResponse;
import static java.util.Objects.nonNull;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.never;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.REACTIVE;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.STRAIGHT;
import static ru.art.rsocket.model.RsocketRequestContext.fromPayload;
import static ru.art.rsocket.module.RsocketModule.rsocketModuleState;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.fromMimeType;
import static ru.art.rsocket.writer.ServiceResponsePayloadWriter.writeResponseReactive;
import static ru.art.rsocket.writer.ServiceResponsePayloadWriter.writeServiceResponse;
import static ru.art.service.ServiceController.executeServiceMethodUnchecked;
import static ru.art.service.ServiceModule.serviceModule;

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
