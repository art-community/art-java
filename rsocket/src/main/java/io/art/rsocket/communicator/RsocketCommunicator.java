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

    @Builder.Default
    private final RsocketSetupPayload setupPayload = RsocketSetupPayload.builder().build();

    @Getter(lazy = true, value = PRIVATE)
    private final Logger logger = logger(RsocketCommunicator.class);

    private final LazyValue<RsocketCommunicatorConfiguration> communicatorConfiguration = lazy(this::communicatorConfiguration);

    private final LazyValue<RsocketConnectorConfiguration> connectorConfiguration = lazy(this::connectorConfiguration);

    private final LazyValue<RsocketSetupPayload> adoptedSetupPayload = lazy(this::adoptedSetupPayload);

    private final LazyValue<RsocketPayloadWriter> writer = lazy(this::writer);

    private final LazyValue<RsocketPayloadReader> reader = lazy(this::reader);

    private final LazyValue<RSocketClient> client = lazy(this::createClient);

    @Override
    public void initialize() {
        communicatorConfiguration.initialize();
        connectorConfiguration.initialize();
        adoptedSetupPayload.initialize();
        writer.initialize();
        reader.initialize();
        client.initialize();
    }

    @Override
    public void dispose() {
        client.dispose(RsocketManager::disposeRsocket);
        reader.dispose();
        writer.dispose();
        adoptedSetupPayload.dispose();
        connectorConfiguration.dispose();
        communicatorConfiguration.dispose();
    }

    @Override
    public Flux<Value> communicate(Flux<Value> input) {
        RsocketPayloadWriter writer = this.writer.get();
        RsocketPayloadReader reader = this.reader.get();
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
        RsocketConnectorConfiguration connectorConfiguration = this.connectorConfiguration.get();
        RsocketSetupPayload setupPayload = this.adoptedSetupPayload.get();
        RSocketConnector connector = RSocketConnector.create()
                .dataMimeType(toMimeType(setupPayload.getDataFormat()).toString())
                .metadataMimeType(toMimeType(setupPayload.getMetadataFormat()).toString())
                .fragment(connectorConfiguration.getFragment())
                .interceptors(connectorConfiguration.getInterceptors());
        apply(connectorConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(connectorConfiguration.getResume(), connector::resume);
        apply(connectorConfiguration.getRetry(), connector::reconnect);
        connector.setupPayload(this.writer.get().writePayloadMetaData(setupPayload.toEntity()));
        switch (connectorConfiguration.getTransport()) {
            case TCP:
                TcpClient tcpClient = connectorConfiguration.getTcpClient();
                int tcpMaxFrameLength = connectorConfiguration.getTcpMaxFrameLength();
                return from(connector
                        .connect(TcpClientTransport.create(tcpClient, tcpMaxFrameLength))
                        .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, connectorId, setupPayload)))
                        .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                        .doOnTerminate(() -> getLogger().info(format(COMMUNICATOR_STOPPED, connectorId, setupPayload))));
            case WS:
                HttpClient httpWebSocketClient = connectorConfiguration.getHttpWebSocketClient();
                String httpWebSocketPath = connectorConfiguration.getHttpWebSocketPath();
                return from(connector
                        .connect(WebsocketClientTransport.create(httpWebSocketClient, httpWebSocketPath))
                        .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, connectorId, setupPayload)))
                        .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                        .doOnTerminate(() -> getLogger().info(format(COMMUNICATOR_STOPPED, connectorId, setupPayload))));
        }
        throw new ImpossibleSituation();
    }

    private RsocketCommunicatorConfiguration communicatorConfiguration() {
        return rsocketModule().configuration().getCommunicatorConfiguration();
    }

    private RsocketConnectorConfiguration connectorConfiguration() {
        return communicatorConfiguration.get().getConnectors().get(connectorId);
    }

    private RsocketSetupPayload adoptedSetupPayload() {
        RsocketConnectorConfiguration connectorConfiguration = this.connectorConfiguration.get();
        return setupPayload
                .toBuilder()
                .dataFormat(orElse(setupPayload.getDataFormat(), connectorConfiguration.getSetupPayload().getDataFormat()))
                .metadataFormat(orElse(setupPayload.getMetadataFormat(), connectorConfiguration.getSetupPayload().getMetadataFormat()))
                .build();
    }

    private RsocketPayloadWriter writer() {
        return new RsocketPayloadWriter(adoptedSetupPayload.get().getDataFormat(), adoptedSetupPayload.get().getMetadataFormat());
    }

    private RsocketPayloadReader reader() {
        return new RsocketPayloadReader(adoptedSetupPayload.get().getDataFormat(), adoptedSetupPayload.get().getMetadataFormat());
    }
}
