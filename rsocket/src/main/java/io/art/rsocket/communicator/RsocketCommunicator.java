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
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.core.operator.Operators.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static io.rsocket.core.RSocketClient.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;

@Builder
public class RsocketCommunicator implements CommunicatorImplementation {
    private final String connectorId;
    private final CommunicationMode communicationMode;

    private final LazyValue<RSocketClient> client = lazy(this::createClient);

    @Builder.Default
    private final RsocketSetupPayload setupPayload = RsocketSetupPayload.builder().build();

    @Getter(lazy = true, value = PRIVATE)
    private final Logger logger = logger(RsocketCommunicator.class);

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketPayloadWriter writer = new RsocketPayloadWriter(getAdoptedSetupPayload().getDataFormat(), getAdoptedSetupPayload().getMetadataFormat());

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketPayloadReader reader = new RsocketPayloadReader(getAdoptedSetupPayload().getDataFormat(), getAdoptedSetupPayload().getMetadataFormat());

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketCommunicatorConfiguration communicatorConfiguration = rsocketModule().configuration().getCommunicatorConfiguration();

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketConnectorConfiguration connectorConfiguration = getCommunicatorConfiguration().getConnectors().get(connectorId);

    @Getter(lazy = true, value = PRIVATE)
    private final RsocketSetupPayload adoptedSetupPayload = setupPayload
            .toBuilder()
            .dataFormat(orElse(setupPayload.getDataFormat(), getConnectorConfiguration().getSetupPayload().getDataFormat()))
            .metadataFormat(orElse(setupPayload.getMetadataFormat(), getConnectorConfiguration().getSetupPayload().getMetadataFormat()))
            .build();

    @Override
    public void start() {
        client.initialize();
    }

    @Override
    public void stop() {
        client.dispose(this::dispose);
    }

    private void dispose(RSocketClient client) {
        applyIf(client, socket -> !socket.isDisposed(), RsocketManager::disposeRsocket);
        getLogger().info(format(COMMUNICATOR_STOPPED, connectorId));
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
        RsocketConnectorConfiguration connectorConfiguration = getConnectorConfiguration();
        RsocketSetupPayload setupPayload = getAdoptedSetupPayload();
        RSocketConnector connector = RSocketConnector.create()
                .dataMimeType(toMimeType(setupPayload.getDataFormat()).toString())
                .metadataMimeType(toMimeType(setupPayload.getMetadataFormat()).toString())
                .fragment(connectorConfiguration.getFragment())
                .interceptors(connectorConfiguration.getInterceptors());
        apply(connectorConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(connectorConfiguration.getResume(), connector::resume);
        apply(connectorConfiguration.getRetry(), connector::reconnect);
        connector.setupPayload(getWriter().writePayloadMetaData(setupPayload.toEntity()));
        switch (connectorConfiguration.getTransport()) {
            case TCP:
                TcpClient tcpClient = connectorConfiguration.getTcpClient();
                int tcpMaxFrameLength = connectorConfiguration.getTcpMaxFrameLength();
                return from(connector
                        .connect(TcpClientTransport.create(tcpClient, tcpMaxFrameLength))
                        .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, connectorId, setupPayload))));
            case WS:
                HttpClient httpWebSocketClient = connectorConfiguration.getHttpWebSocketClient();
                String httpWebSocketPath = connectorConfiguration.getHttpWebSocketPath();
                return from(connector
                        .connect(WebsocketClientTransport.create(httpWebSocketClient, httpWebSocketPath))
                        .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, connectorId, setupPayload))));
        }
        throw new ImpossibleSituation();
    }

}
