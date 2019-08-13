/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import static ru.art.rsocket.constants.RsocketModuleConstants.RsocketDataFormat;
import static ru.art.rsocket.model.RsocketRequestContext.fromPayload;
import static ru.art.rsocket.module.RsocketModule.rsocketModuleState;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.fromMimeType;
import static ru.art.rsocket.writer.ServiceResponsePayloadWriter.writeResponseReactive;
import static ru.art.rsocket.writer.ServiceResponsePayloadWriter.writeServiceResponse;
import static ru.art.service.ServiceController.executeServiceMethodUnchecked;
import static ru.art.service.ServiceModule.serviceModule;

public class RsocketAcceptor extends AbstractRSocket {
    public RsocketAcceptor(RSocket socket, ConnectionSetupPayload setupPayload) {
        rsocketModuleState().currentRocketState(new CurrentRsocketState(setupPayload.dataMimeType(), setupPayload.metadataMimeType(), socket));
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        executeServiceMethodUnchecked(fromPayload(payload, dataFormat).getRequest());
        return never();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        RsocketRequestContext context = fromPayload(payload, dataFormat);
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        RsocketService.RsocketMethod rsocketMethod = context.getRsocketReactiveMethods().getRsocketMethod();
        ValueFromModelMapper<?, ?> responseMapper = rsocketMethod.responseMapper();
        return context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT ?
                Mono.just(writeServiceResponse(responseMapper, serviceResponse,
                        getOrElse(rsocketMethod.overrideResponseDataFormat(), dataFormat))) :
                writeResponseReactive(rsocketMethod.responseMapper(), cast(serviceResponse), dataFormat).next();
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
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        return from(payloads)
                .map(payload -> RsocketRequestReactiveContext.fromPayload(payload, dataFormat))
                .filter(context -> nonNull(context.getRsocketReactiveMethods().getRsocketMethod()))
                .filter(context -> nonNull(context.getRsocketReactiveMethods().getReactiveMethod()))
                .filter(context -> context.getRsocketReactiveMethods()
                        .getReactiveMethod()
                        .requestProcessingMode() == REACTIVE)
                .filter(context -> context.getRsocketReactiveMethods()
                        .getReactiveMethod()
                        .responseProcessingMode() == REACTIVE)
                .groupBy(RsocketRequestReactiveContext::getRsocketReactiveGroupKey, serviceModule()
                        .getServiceRegistry()
                        .getServices()
                        .size())
                .map(RsocketReactivePreparedResponse::fromGroupedFlux)
                .flatMap(preparedResponse -> writeResponseReactive(preparedResponse.getResponseMapper(), executeServiceMethodUnchecked(preparedResponse.getServiceRequest()), dataFormat));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        executeServiceMethodUnchecked(fromPayload(payload, dataFormat).getRequest());
        return never();
    }

}
