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

package io.art.rsocket.communicator;

import io.art.communicator.implementation.*;
import io.art.core.lazy.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.art.value.immutable.Value;
import io.rsocket.core.*;
import io.rsocket.transport.netty.client.*;
import io.rsocket.util.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.value.mapping.ServiceIdMapping.*;
import static io.rsocket.core.RSocketClient.from;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Mono.*;
import java.util.function.*;

@Getter
@Builder
public class RsocketCommunicator implements CommunicatorImplementation<RsocketCommunicatorConfiguration> {
    private final String connectorId;
    private final CommunicationMode communicationMode;
    private final RsocketSetupPayload setupPayload;

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketPayloadWriter writer = new RsocketPayloadWriter(setupPayload().getDataFormat(), setupPayload().getMetadataFormat());

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketPayloadReader reader = new RsocketPayloadReader(setupPayload().getDataFormat(), setupPayload().getMetadataFormat());

    @Getter(lazy = true, value = PRIVATE)
    private final LazyValue<RSocketClient> client = createClient();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Value>, Flux<Value>> implementation = isNull(setupPayload().getServiceMethod()) ? this::processCommunication : input -> getClient()
            .get()
            .metadataPush(just(getWriter().writePayloadMetaData(fromServiceMethod(setupPayload().getServiceMethod()))))
            .thenMany(processCommunication(input));

    private RsocketCommunicatorConfiguration communicatorConfiguration;

    @Override
    public void start(RsocketCommunicatorConfiguration configuration) {
        this.communicatorConfiguration = configuration;
        getClient().get();
    }

    @Override
    public void stop() {
        getClient().ifInitialized(RSocketClient::dispose);
    }

    @Override
    public Flux<Value> communicate(Flux<Value> input) {
        return getImplementation().apply(input);
    }

    private Flux<Value> processCommunication(Flux<Value> input) {
        RsocketPayloadWriter writer = getWriter();
        RsocketPayloadReader reader = getReader();
        switch (communicationMode) {
            case FIRE_AND_FORGET:
                return cast(getClient().get().fireAndForget(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE)).flux());
            case REQUEST_RESPONSE:
                return getClient().get()
                        .requestResponse(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE))
                        .flux()
                        .map(payload -> reader.readPayloadData(payload).getValue());
            case REQUEST_STREAM:
                return getClient().get()
                        .requestStream(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE))
                        .map(payload -> reader.readPayloadData(payload).getValue());
            case REQUEST_CHANNEL:
                return getClient().get()
                        .requestChannel(input.map(writer::writePayloadData))
                        .map(payload -> reader.readPayloadData(payload).getValue());
            case METADATA_PUSH:
                return cast(getClient().get().metadataPush(input.map(writer::writePayloadMetaData).last(EmptyPayload.INSTANCE)).flux());
        }
        throw new IllegalStateException();
    }

    private LazyValue<RSocketClient> createClient() {
        RsocketConnectorConfiguration connectorConfiguration = communicatorConfiguration.getConnectors().get(connectorId);
        RSocketConnector connector = connectorConfiguration.getConnector().setupPayload(getWriter().writePayloadMetaData(setupPayload().toEntity()));
        switch (connectorConfiguration.getTransport()) {
            case TCP:
                TcpClient tcpClient = connectorConfiguration.getTcpClient();
                int tcpMaxFrameLength = connectorConfiguration.getTcpMaxFrameLength();
                return lazy(() -> from(connector.connect(TcpClientTransport.create(tcpClient, tcpMaxFrameLength))));
            case WS:
                HttpClient httpWebSocketClient = connectorConfiguration.getHttpWebSocketClient();
                String httpWebSocketPath = connectorConfiguration.getHttpWebSocketPath();
                return lazy(() -> from(connector.connect(WebsocketClientTransport.create(httpWebSocketClient, httpWebSocketPath))));
        }
        throw new IllegalStateException();
    }

    private RsocketSetupPayload setupPayload() {
        return setupPayload
                .toBuilder()
                .dataFormat(orElse(setupPayload.getDataFormat(), communicatorConfiguration.getDefaultDataFormat()))
                .metadataFormat(orElse(setupPayload.getMetadataFormat(), communicatorConfiguration.getDefaultMetaDataFormat()))
                .build();
    }
}
