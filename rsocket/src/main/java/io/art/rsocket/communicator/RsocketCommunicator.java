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
import io.art.core.exception.*;
import io.art.core.lazy.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.manager.*;
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
import static io.art.core.operator.Operators.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.rsocket.core.RSocketClient.*;
import static lombok.AccessLevel.*;

@Builder
public class RsocketCommunicator implements CommunicatorImplementation {
    private final String connectorId;
    private final CommunicationMode communicationMode;

    @Builder.Default
    private final RsocketSetupPayload setupPayload = RsocketSetupPayload.builder().build();

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketPayloadWriter writer = new RsocketPayloadWriter(setupPayload().getDataFormat(), setupPayload().getMetadataFormat());

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketPayloadReader reader = new RsocketPayloadReader(setupPayload().getDataFormat(), setupPayload().getMetadataFormat());

    private final LazyValue<RSocketClient> client = lazy(this::createClient);

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketCommunicatorConfiguration communicatorConfiguration = rsocketModule().configuration().getCommunicatorConfiguration();

    @Override
    public void start() {
        client.initialize();
    }

    @Override
    public void stop() {
        client.dispose(client -> applyIf(client, socket -> !socket.isDisposed(), RsocketManager::disposeRsocket));
    }

    @Override
    public Flux<Value> communicate(Flux<Value> input) {
        RsocketPayloadWriter writer = getWriter();
        RsocketPayloadReader reader = getReader();
        switch (communicationMode) {
            case FIRE_AND_FORGET:
                return cast(client.get().fireAndForget(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE)).flux());
            case REQUEST_RESPONSE:
                return client.get()
                        .requestResponse(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE))
                        .flux()
                        .map(reader::readPayloadData)
                        .filter(data -> !data.isEmpty())
                        .map(RsocketPayloadValue::getValue);
            case REQUEST_STREAM:
                return client.get()
                        .requestStream(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE))
                        .map(reader::readPayloadData)
                        .filter(data -> !data.isEmpty())
                        .map(RsocketPayloadValue::getValue);
            case REQUEST_CHANNEL:
                return client.get()
                        .requestChannel(input.map(writer::writePayloadData))
                        .map(reader::readPayloadData)
                        .filter(data -> !data.isEmpty())
                        .map(RsocketPayloadValue::getValue);
            case METADATA_PUSH:
                return cast(client.get().metadataPush(input.map(writer::writePayloadMetaData).last(EmptyPayload.INSTANCE)).flux());
        }
        throw new ImpossibleSituation();
    }

    private RSocketClient createClient() {
        RsocketConnectorConfiguration connectorConfiguration = getCommunicatorConfiguration().getConnectors().get(connectorId);
        RSocketConnector connector = RSocketConnector.create()
                .dataMimeType(connectorConfiguration.getDataMimeType().toString())
                .metadataMimeType(connectorConfiguration.getMetaDataMimeType().toString())
                .fragment(connectorConfiguration.getFragment())
                .interceptors(connectorConfiguration.getInterceptors());
        apply(connectorConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(connectorConfiguration.getResume(), connector::resume);
        apply(connectorConfiguration.getRetry(), connector::reconnect);
        connector.setupPayload(getWriter().writePayloadMetaData(setupPayload().toEntity()));
        switch (connectorConfiguration.getTransport()) {
            case TCP:
                TcpClient tcpClient = connectorConfiguration.getTcpClient();
                int tcpMaxFrameLength = connectorConfiguration.getTcpMaxFrameLength();
                return from(connector.connect(TcpClientTransport.create(tcpClient, tcpMaxFrameLength)));
            case WS:
                HttpClient httpWebSocketClient = connectorConfiguration.getHttpWebSocketClient();
                String httpWebSocketPath = connectorConfiguration.getHttpWebSocketPath();
                return from(connector.connect(WebsocketClientTransport.create(httpWebSocketClient, httpWebSocketPath)));
        }
        throw new ImpossibleSituation();
    }

    private RsocketSetupPayload setupPayload() {
        return setupPayload
                .toBuilder()
                .dataFormat(orElse(setupPayload.getDataFormat(), getCommunicatorConfiguration().getDefaultDataFormat()))
                .metadataFormat(orElse(setupPayload.getMetadataFormat(), getCommunicatorConfiguration().getDefaultMetaDataFormat()))
                .build();
    }
}
