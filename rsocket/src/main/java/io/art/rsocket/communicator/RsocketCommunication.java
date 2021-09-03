/*
 * ART
 *
 * Copyright 2019-2021 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import io.art.logging.*;
import io.art.logging.logger.*;
import io.art.meta.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.transport.payload.*;
import io.rsocket.core.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.property.Property.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.CommunicationMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.transport.payload.TransportPayload.*;
import static io.art.transport.payload.TransportPayloadReader.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import static reactor.core.publisher.Sinks.*;
import java.util.function.*;

public class RsocketCommunication implements Communication {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = Logging.logger(RSOCKET_COMMUNICATOR_LOGGER);
    private final RsocketCommonConnectorConfiguration connectorConfiguration;
    private final Property<RSocketClient> client;
    private final Property<Function<Flux<Object>, Flux<Object>>> communication;
    private CommunicatorAction action;
    private MetaType<?> inputMappingType;
    private MetaType<?> outputMappingType;

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
        inputMappingType = action.getInputType();
        if (nonNull(inputMappingType) && (inputMappingType.internalKind() == MONO || inputMappingType.internalKind() == FLUX)) {
            inputMappingType = inputMappingType.parameters().get(0);
        }

        outputMappingType = action.getOutputType();
        if (nonNull(outputMappingType) && (outputMappingType.internalKind() == MONO || outputMappingType.internalKind() == FLUX)) {
            outputMappingType = outputMappingType.parameters().get(0);
        }
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
        if (withLogging()) {
            getLogger().info(format(RSOCKET_COMMUNICATOR_STOPPED, connectorConfiguration.getConnector()));
        }
    }

    private Function<Flux<Object>, Flux<Object>> communication() {
        TransportPayloadReader reader = transportPayloadReader(connectorConfiguration.getDataFormat());
        TransportPayloadWriter writer = transportPayloadWriter(connectorConfiguration.getDataFormat());
        switch (communicationMode()) {
            case FIRE_AND_FORGET:
                return fireAndForget(writer).andThen(output -> output
                        .filter(payload -> !payload.isEmpty())
                        .map(TransportPayload::getValue));
            case REQUEST_RESPONSE:
                return requestResponse(reader, writer).andThen(output -> output
                        .filter(payload -> !payload.isEmpty())
                        .map(TransportPayload::getValue));
            case REQUEST_STREAM:
                return requestStream(reader, writer).andThen(output -> output
                        .filter(payload -> !payload.isEmpty())
                        .map(TransportPayload::getValue));
            case REQUEST_CHANNEL:
                return requestChannel(reader, writer).andThen(output -> output
                        .filter(payload -> !payload.isEmpty())
                        .map(TransportPayload::getValue));
            case METADATA_PUSH:
                return metaDataPush(writer).andThen(output -> output
                        .filter(payload -> !payload.isEmpty())
                        .map(TransportPayload::getValue));
        }
        throw new ImpossibleSituationException();
    }


    private Function<Flux<Object>, Flux<TransportPayload>> fireAndForget(TransportPayloadWriter writer) {
        RSocketClient client = this.client.get();
        if (isNull(inputMappingType)) {
            return input -> Flux
                    .from(client.fireAndForget(EMPTY_PAYLOAD_MONO))
                    .switchMap(ignore -> emptyTransportPayloadFlux());
        }
        if (inputMappingType.internalKind() == FLUX || inputMappingType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeMono(input, sink);
                return client
                        .fireAndForget(sink.asMono().map(element -> create(writer.write(typed(inputMappingType, element)))))
                        .flux()
                        .switchMap(ignore -> emptyTransportPayloadFlux());
            };
        }
        return input -> client
                .fireAndForget(input.map(value -> create(writer.write(typed(inputMappingType, value)))).last(EMPTY_PAYLOAD))
                .flux()
                .switchMap(ignore -> emptyTransportPayloadFlux());
    }

    private Function<Flux<Object>, Flux<TransportPayload>> requestResponse(TransportPayloadReader reader, TransportPayloadWriter writer) {
        RSocketClient client = this.client.get();
        if (isNull(inputMappingType)) {
            return input -> Flux
                    .from(client.requestResponse(EMPTY_PAYLOAD_MONO))
                    .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
        }
        if (inputMappingType.internalKind() == FLUX || inputMappingType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeMono(input, sink);
                return client.requestResponse(sink.asMono().map(element -> create(writer.write(typed(inputMappingType, element)))))
                        .flux()
                        .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
            };
        }
        return input -> client
                .requestResponse(input.map(value -> create(writer.write(typed(inputMappingType, value)))).last(EMPTY_PAYLOAD))
                .flux()
                .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
    }

    private Function<Flux<Object>, Flux<TransportPayload>> requestStream(TransportPayloadReader reader, TransportPayloadWriter writer) {
        RSocketClient client = this.client.get();
        if (isNull(inputMappingType)) {
            return input -> client
                    .requestStream(EMPTY_PAYLOAD_MONO)
                    .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
        }
        if (inputMappingType.internalKind() == FLUX || inputMappingType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeMono(input, sink);
                return client
                        .requestStream(sink.asMono().map(element -> create(writer.write(typed(inputMappingType, element)))))
                        .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
            };
        }
        return input -> client
                .requestStream(input.map(value -> create(writer.write(typed(inputMappingType, value)))).last(EMPTY_PAYLOAD))
                .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
    }

    private Function<Flux<Object>, Flux<TransportPayload>> requestChannel(TransportPayloadReader reader, TransportPayloadWriter writer) {
        RSocketClient client = this.client.get();
        return input -> client
                .requestChannel(input.map(value -> create(writer.write(typed(inputMappingType, value)))))
                .map(payload -> readRsocketPayload(reader, payload, outputMappingType));
    }

    private Function<Flux<Object>, Flux<TransportPayload>> metaDataPush(TransportPayloadWriter writer) {
        RSocketClient client = this.client.get();
        if (isNull(inputMappingType)) {
            return input -> Flux
                    .from(client.metadataPush(EMPTY_PAYLOAD_MONO))
                    .switchMap(ignore -> emptyTransportPayloadFlux());
        }
        if (inputMappingType.internalKind() == FLUX || inputMappingType.internalKind() == MONO) {
            return input -> {
                Sinks.One<Object> sink = one();
                subscribeMono(input, sink);
                return client
                        .metadataPush(sink.asMono().map(element -> create(writer.write(typed(inputMappingType, element)))))
                        .flux()
                        .switchMap(ignore -> emptyTransportPayloadFlux());
            };
        }
        return input -> client
                .metadataPush(input.map(value -> create(writer.write(typed(inputMappingType, value)))).last(EMPTY_PAYLOAD))
                .flux()
                .switchMap(ignore -> emptyTransportPayloadFlux());
    }

    private void subscribeMono(Flux<Object> input, Sinks.One<Object> emitter) {
        input.doOnNext(element -> emitter.emitValue(element, FAIL_FAST))
                .doOnError(error -> emitter.emitError(error, FAIL_FAST))
                .subscribe();
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

    @Override
    public String toString() {
        return connectorConfiguration.getConnector();
    }
}
