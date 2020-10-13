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

import io.art.core.mime.*;
import io.art.entity.immutable.Value;
import io.art.rsocket.configuration.*;
import io.art.rsocket.flux.*;
import io.art.rsocket.model.*;
import io.art.rsocket.service.*;
import io.art.rsocket.state.*;
import io.art.server.specification.*;
import io.rsocket.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.entity.constants.EntityConstants.*;
import static io.art.entity.mime.MimeTypeDataFormatMapper.*;
import static io.art.rsocket.model.RsocketRequestContext.*;
import static io.art.rsocket.payload.RsocketPayloadReader.*;
import static io.art.rsocket.payload.RsocketPayloadWriter.*;
import static io.art.rsocket.state.RsocketModuleState.*;
import static reactor.core.publisher.Mono.*;

public class ServerRsocket implements RSocket {
    private ServiceMethodSpecification specification;
    private RsocketModuleConfiguration configuration;

    public ServerRsocket(ConnectionSetupPayload payload, RSocket socket) {
        Value value = readPayloadData(payload, fromMimeType(MimeType.valueOf(payload.dataMimeType())));
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        DataFormat dataFormat = updateState().getDataFormat();
        specification.serve(let(readPayloadData(payload, dataFormat), Flux::just, Flux.empty())).blockFirst();
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
                ? just(writeServiceResponse(rsocketMethod, serviceResponse, orElse(rsocketMethod.overrideResponseDataFormat(), dataFormat)))
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
