/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.rsocket.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.exception.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.meta.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.transport.payload.*;
import io.rsocket.core.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.property.Property.*;
import static io.art.logging.module.LoggingModule.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.CommunicationMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import java.util.function.*;

public class RsocketCommunication implements Communication {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketCommunication.class);
    private final RsocketCommonConnectorConfiguration connectorConfiguration;
    private final Property<RSocketClient> client;
    private final Property<Function<Flux<Object>, Flux<Object>>> communication;
    private CommunicatorAction action;

    public RsocketCommunication(Supplier<RSocketClient> client, RsocketModuleConfiguration module, RsocketCommonConnectorConfiguration connector) {
        this.connectorConfiguration = connector;
        this.client = property(client, this::disposeClient).listenConsumer(() -> module.getConsumer()
                .connectorConsumers()
                .consumerFor(connector.getConnector()));
        communication = property(this::communication).listenProperties(this.client);
    }

    @Override
    public void initialize(CommunicatorAction action) {
        this.action = action;
        client.initialize();
    }

    @Override
    public void dispose() {
        client.dispose();
    }

    @Override
    public Flux<Object> communicate(Flux<Object> input) {
        return communication.get().apply(input);
    }

    private void disposeClient(RSocketClient rsocket) {
        disposeRsocket(rsocket);
        if (withLogging() && connectorConfiguration.isLogging()) {
            getLogger().info(format(COMMUNICATOR_STOPPED, connectorConfiguration.getConnector()));
        }
    }

    private Function<Flux<Object>, Flux<Object>> communication() {
        TransportPayloadReader reader = action.getReader().apply(connectorConfiguration.getDataFormat());
        TransportPayloadWriter writer = action.getWriter().apply(connectorConfiguration.getDataFormat());
        RSocketClient client = this.client.get();
        MetaType<?> inputType = action.getInputType();
        MetaType<?> outputType = action.getOutputType();
        switch (communicationMode()) {
            case FIRE_AND_FORGET:
                if (isNull(inputType)) {
                    return input -> cast(Flux.from(client.fireAndForget(Mono.just(EMPTY_PAYLOAD))));
                }
                if (inputType.internalKind() == FLUX || inputType.internalKind() == MONO) {
                    return input -> {
                        Sinks.One<Object> sink = Sinks.one();
                        input.doOnNext(element -> sink.emitValue(element, FAIL_FAST))
                                .doOnError(error -> sink.emitError(error, FAIL_FAST))
                                .subscribe();
                        return cast(client.fireAndForget(sink.asMono().map(element -> create(writer.write(typed(inputType.parameters().get(0), action))))));
                    };
                }
                return input -> cast(client.fireAndForget(input.map(value -> create(writer.write(typed(inputType, value)))).last(EMPTY_PAYLOAD)).flux());
            case REQUEST_RESPONSE:
                if (isNull(inputType)) {
                    return input -> cast(Flux.from(client.requestResponse(Mono.just(EMPTY_PAYLOAD))));
                }
                if (inputType.internalKind() == FLUX || inputType.internalKind() == MONO) {
                    return input -> {
                        Sinks.One<Object> sink = Sinks.one();
                        input.doOnNext(element -> sink.emitValue(element, FAIL_FAST))
                                .doOnError(error -> sink.emitError(error, FAIL_FAST))
                                .subscribe();
                        return client.requestResponse(sink.asMono().map(element -> create(writer.write(typed(inputType.parameters().get(0), action)))))
                                .flux()
                                .map(payload -> readRsocketPayload(reader, payload, outputType))
                                .filter(data -> !data.isEmpty())
                                .map(TransportPayload::getValue);
                    };
                }
                return input -> client
                        .requestResponse(input.map(value -> create(writer.write(typed(inputType, value)))).last(EMPTY_PAYLOAD))
                        .flux()
                        .map(payload -> readRsocketPayload(reader, payload, outputType))
                        .filter(data -> !data.isEmpty())
                        .map(TransportPayload::getValue);
            case REQUEST_STREAM:
                if (isNull(inputType)) {
                    return input -> cast(client.requestStream(Mono.just(EMPTY_PAYLOAD)));
                }
                if (inputType.internalKind() == FLUX || inputType.internalKind() == MONO) {
                    return input -> {
                        Sinks.One<Object> sink = Sinks.one();
                        input.doOnNext(element -> sink.emitValue(element, FAIL_FAST))
                                .doOnError(error -> sink.emitError(error, FAIL_FAST))
                                .subscribe();
                        return client.requestStream(sink.asMono().map(element -> create(writer.write(typed(inputType.parameters().get(0), action)))))
                                .map(payload -> readRsocketPayload(reader, payload, outputType))
                                .filter(data -> !data.isEmpty())
                                .map(TransportPayload::getValue);
                    };
                }
                return input -> client
                        .requestStream(input.map(value -> create(writer.write(typed(inputType, value)))).last(EMPTY_PAYLOAD))
                        .map(payload -> readRsocketPayload(reader, payload, outputType))
                        .filter(data -> !data.isEmpty())
                        .map(TransportPayload::getValue);
            case REQUEST_CHANNEL:
                return input -> client
                        .requestChannel(input.map(value -> create(writer.write(typed(inputType.parameters().get(0), value)))).switchIfEmpty(EMPTY_PAYLOAD_MONO.get()))
                        .map(payload -> readRsocketPayload(reader, payload, outputType))
                        .filter(data -> !data.isEmpty())
                        .map(TransportPayload::getValue);
            case METADATA_PUSH:
                if (inputType.internalKind() == FLUX || inputType.internalKind() == MONO) {
                    return input -> {
                        Sinks.One<Object> sink = Sinks.one();
                        input.doOnNext(element -> sink.emitValue(element, FAIL_FAST))
                                .doOnError(error -> sink.emitError(error, FAIL_FAST))
                                .subscribe();
                        return cast(client.metadataPush(sink.asMono().map(element -> create(writer.write(typed(inputType.parameters().get(0), action))))));
                    };
                }
                return input -> cast(client.metadataPush(input.map(value -> create(writer.write(typed(inputType, value)))).last(EMPTY_PAYLOAD)).flux());
        }
        throw new ImpossibleSituationException();
    }

    private CommunicationMode communicationMode() {
        if (isNull(action.getOutputType()) || action.getOutputType().internalKind() == VOID) {
            return FIRE_AND_FORGET;
        }
        if (isNull(action.getInputType())) {
            if (action.getOutputType().internalKind() == FLUX) {
                return REQUEST_STREAM;
            }
            return REQUEST_RESPONSE;
        }
        if (action.getInputType().internalKind() == FLUX) {
            return REQUEST_CHANNEL;
        }
        if (action.getOutputType().internalKind() == FLUX) {
            return REQUEST_STREAM;
        }
        return REQUEST_RESPONSE;
    }
}
