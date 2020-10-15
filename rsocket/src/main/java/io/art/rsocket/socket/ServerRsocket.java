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
import io.art.rsocket.exception.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.art.rsocket.state.*;
import io.art.server.specification.*;
import io.rsocket.*;
import lombok.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.entity.constants.EntityConstants.*;
import static io.art.entity.mime.MimeTypeDataFormatMapper.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ContextKeys.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ExceptionMessages.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.state.RsocketModuleState.RsocketThreadLocalState.*;
import static io.art.server.module.ServerModule.*;
import static java.util.Objects.*;
import static reactor.core.publisher.Mono.*;
import java.util.*;
import java.util.function.*;

public class ServerRsocket implements RSocket {
    private final ServiceMethodSpecification specification;
    private final RsocketPayloadReader reader;
    private final RsocketPayloadWriter writer;
    private final RSocket requesterSocket;
    @Getter(lazy = true)
    private final RsocketModuleState state = rsocketModule().state();

    public ServerRsocket(ConnectionSetupPayload payload, RSocket requesterSocket) {
        DataFormat dataFormat = fromMimeType(MimeType.valueOf(payload.dataMimeType()), rsocketModule().configuration().getDefaultDataFormat());
        DataFormat metaDataFormat = fromMimeType(MimeType.valueOf(payload.metadataMimeType()), rsocketModule().configuration().getDefaultDataFormat());
        reader = new RsocketPayloadReader(dataFormat, metaDataFormat);
        writer = new RsocketPayloadWriter(dataFormat, metaDataFormat);
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        RsocketModuleConfiguration configuration = rsocketModule().configuration();
        if (isNull(payloadValue) && isNull(configuration.getDefaultServiceMethod())) {
            throw new RsocketServerException(SPECIFICATION_NOT_FOUND);
        }
        Optional<ServiceMethodSpecification> possibleSpecification = specifications().findMethodByValue(payloadValue.getValue());
        if (!possibleSpecification.isPresent() && isNull(configuration.getDefaultServiceMethod())) {
            throw new RsocketServerException(SPECIFICATION_NOT_FOUND);
        }
        Optional<ServiceMethodSpecification> defaultSpecification = specifications().findMethodById(configuration.getDefaultServiceMethod());
        if (!possibleSpecification.isPresent() && !defaultSpecification.isPresent()) {
            throw new RsocketServerException(SPECIFICATION_NOT_FOUND);
        }
        this.specification = possibleSpecification.orElseGet(defaultSpecification::get);
        this.requesterSocket = requesterSocket;
    }

    @Override
    public Mono<Void> fireAndForget(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        if (isNull(payloadValue)) {
            return never();
        }
        Flux<Value> input = addContext(Flux.just(payloadValue.getValue()));
        return specification.serve(input).then();
    }

    @Override
    public Mono<Payload> requestResponse(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        if (isNull(payloadValue)) {
            return empty();
        }
        Flux<Value> input = addContext(Flux.just(payloadValue.getValue()));
        return specification.serve(input).map(writer::writePayloadData).last();
    }

    @Override
    public Flux<Payload> requestStream(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadData(payload);
        if (isNull(payloadValue)) {
            return Flux.empty();
        }
        Flux<Value> input = addContext(Flux.just(payloadValue.getValue()));
        return specification.serve(input).map(writer::writePayloadData);
    }

    @Override
    public Flux<Payload> requestChannel(Publisher<Payload> payloads) {
        EmitterProcessor<Value> inputProcessor = EmitterProcessor.create();
        FluxSink<Value> inputEmitter = inputProcessor.sink();
        Consumer<Payload> sendPayload = payload -> apply(reader.readPayloadData(payload), value -> inputEmitter.next(value.getValue()));
        Flux<Value> inputFlux = addContext(inputProcessor)
                .doOnSubscribe(subscription -> {
                    inputEmitter.onRequest(subscription::request);
                    inputEmitter.onCancel(subscription::cancel);
                    inputEmitter.onDispose(Flux.from(payloads).subscribe(sendPayload, inputEmitter::error, inputEmitter::complete));
                });
        return specification.serve(inputFlux).map(writer::writePayloadData);
    }

    @Override
    public Mono<Void> metadataPush(Payload payload) {
        RsocketPayloadValue payloadValue = reader.readPayloadMetaData(payload);
        if (isNull(payloadValue)) {
            return never();
        }
        Flux<Value> input = addContext(Flux.just(payloadValue.getValue()));
        return specification.serve(input).then();
    }

    private <T> Flux<T> addContext(Flux<T> flux) {
        return flux.subscriberContext(context -> context.putNonNull(REQUESTER_RSOCKET_KEY, requesterSocket))
                .flatMap(value -> subscriberContext()
                        .doOnNext(context -> getState().localState(fromContext(context)))
                        .map(context -> value).flux());
    }
}
