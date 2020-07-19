/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.rsocket.socket;

import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import io.art.rsocket.flux.*;
import io.art.rsocket.model.*;
import io.art.rsocket.service.*;
import io.art.rsocket.state.*;
import static reactor.core.publisher.Mono.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.reactive.service.constants.ReactiveServiceModuleConstants.ReactiveMethodProcessingMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.model.RsocketRequestContext.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.selector.RsocketDataFormatMimeTypeConverter.*;
import static io.art.rsocket.state.RsocketModuleState.*;
import static io.art.rsocket.writer.RsocketPayloadWriter.*;
import static io.art.rsocket.writer.ServiceResponsePayloadWriter.*;
import static io.art.server.ServiceController.*;

public class RsocketAcceptor extends AbstractRSocket {
    private final CurrentRsocketState state;

    public RsocketAcceptor(RSocket socket, ConnectionSetupPayload setupPayload) {
        state = CurrentRsocketState
                .builder()
                .dataMimeType(setupPayload.dataMimeType())
                .metadataMimeType(setupPayload.metadataMimeType())
                .rsocket(socket)
                .dataFormat(fromMimeType(setupPayload.dataMimeType()))
                .build();
        updateState();
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketDataFormat dataFormat = updateState().getDataFormat();
        RsocketRequestContext context = fromPayload(payload, dataFormat);
        if (context.isStopHandling()) {
            return never();
        }
        executeServiceMethodUnchecked(context.getRequest());
        return never();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketDataFormat dataFormat = updateState().getDataFormat();
        RsocketRequestContext context = fromPayload(payload, dataFormat);
        if (context.isStopHandling()) {
            return just(writePayloadData(context.getAlternativeResponse(), dataFormat));
        }
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        RsocketService.RsocketMethod rsocketMethod = context.getRsocketReactiveMethods().getRsocketMethod();
        return context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT
                ? just(writeServiceResponse(rsocketMethod, serviceResponse, getOrElse(rsocketMethod.overrideResponseDataFormat(), dataFormat)))
                : writeResponseReactive(context.getRsocketReactiveMethods(), cast(serviceResponse), dataFormat).next();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketDataFormat dataFormat = updateState().getDataFormat();
        RsocketRequestContext context = fromPayload(payload, fromMimeType(state.getDataMimeType()));
        if (context.isStopHandling()) {
            return Flux.just(writePayloadData(context.getAlternativeResponse(), dataFormat));
        }
        if (context.getRsocketReactiveMethods().getReactiveMethod().responseProcessingMode() == STRAIGHT) {
            return Flux.empty();
        }
        ServiceResponse<?> serviceResponse = executeServiceMethodUnchecked(context.getRequest());
        return writeResponseReactive(context.getRsocketReactiveMethods(), cast(serviceResponse), fromMimeType(state.getDataMimeType()));
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        return Flux.create(emitter -> new RsocketRequestChannelEmitter(emitter, payloads, updateState().getDataFormat()));
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        RsocketDataFormat dataFormat = updateState().getDataFormat();
        executeServiceMethodUnchecked(fromPayload(payload, dataFormat).getRequest());
        return never();
    }

    private CurrentRsocketState updateState() {
        RsocketModuleState moduleState = rsocketModuleState();
        moduleState.currentRocketState(state);
        return state;
    }
}
