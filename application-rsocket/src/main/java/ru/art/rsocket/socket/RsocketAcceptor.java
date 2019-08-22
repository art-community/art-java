/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.rsocket.socket;

import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import ru.art.rsocket.model.*;
import ru.art.rsocket.service.*;
import ru.art.rsocket.state.RsocketModuleState.*;
import ru.art.service.model.*;

import static java.util.Objects.*;
import static reactor.core.publisher.Flux.from;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.never;
import static ru.art.core.caster.Caster.*;
import static ru.art.core.extension.NullCheckingExtensions.*;
import static ru.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.*;
import static ru.art.rsocket.constants.RsocketModuleConstants.*;
import static ru.art.rsocket.model.RsocketRequestContext.*;
import static ru.art.rsocket.module.RsocketModule.*;
import static ru.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.*;
import static ru.art.rsocket.writer.RsocketPayloadWriter.*;
import static ru.art.rsocket.writer.ServiceResponsePayloadWriter.*;
import static ru.art.service.ServiceController.*;
import static ru.art.service.ServiceModule.*;

public class RsocketAcceptor extends AbstractRSocket {
    public RsocketAcceptor(RSocket socket, ConnectionSetupPayload setupPayload) {
        rsocketModuleState().currentRocketState(new CurrentRsocketState(setupPayload.dataMimeType(), setupPayload.metadataMimeType(), socket));
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        RsocketRequestContext context = fromPayload(payload, dataFormat);
        if (context.isStopHandling()) {
            return never();
        }
        executeServiceMethodUnchecked(context.getRequest());
        return never();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        RsocketRequestContext context = fromPayload(payload, dataFormat);
        if (context.isStopHandling()) {
            return just(writePayload(context.getAlternativeResponse(), dataFormat));
        }
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        RsocketService.RsocketMethod rsocketMethod = context.getRsocketReactiveMethods().getRsocketMethod();
        return context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT
                ? just(writeServiceResponse(rsocketMethod, serviceResponse, getOrElse(rsocketMethod.overrideResponseDataFormat(), dataFormat)))
                : writeResponseReactive(rsocketMethod, cast(serviceResponse), dataFormat).next();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        RsocketRequestContext context = fromPayload(payload, fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType()));
        if (context.isStopHandling()) {
            return Flux.just(writePayload(context.getAlternativeResponse(), dataFormat));
        }
        if (context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT) {
            return Flux.never();
        }
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        RsocketService.RsocketMethod rsocketMethod = context.getRsocketReactiveMethods().getRsocketMethod();
        return writeResponseReactive(rsocketMethod, cast(serviceResponse),
                fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType()));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        return from(payloads)
                .map(payload -> RsocketRequestReactiveContext.fromPayload(payload, dataFormat))
                .flatMap(requestReactiveContext -> requestReactiveContext.isStopHandling()
                        ? Flux.just(writePayload(requestReactiveContext.getAlternativeResponse(), dataFormat))
                        : Flux.just(requestReactiveContext)
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
                        .flatMap(preparedResponse -> writeResponseReactive(preparedResponse.getRsocketMethod(),
                                executeServiceMethodUnchecked(preparedResponse.getServiceRequest()),
                                dataFormat)));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        RsocketDataFormat dataFormat = fromMimeType(rsocketModuleState().currentRocketState().getDataMimeType());
        executeServiceMethodUnchecked(fromPayload(payload, dataFormat).getRequest());
        return never();
    }

}
