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

import io.art.communicator.action.*;
import io.art.communicator.implementation.*;
import io.art.core.exception.*;
import io.art.core.lazy.*;
import io.art.core.model.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.payload.*;
import io.art.value.immutable.Value;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.client.*;
import io.rsocket.util.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.MethodProcessingMode.*;
import static io.art.core.lazy.ManagedValue.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.CommunicationMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.value.mime.MimeTypeDataFormatMapper.*;
import static io.rsocket.core.RSocketClient.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Builder
public class RsocketCommunicator implements CommunicatorActionImplementation {
    private final String connectorId;
    private final CommunicatorActionIdentifier communicatorActionId;

    @Getter(lazy = true, value = PRIVATE)
    private final Logger logger = logger(RsocketCommunicator.class);

    @Getter(lazy = true, value = PRIVATE)
    private final CommunicatorAction communicatorAction = communicatorAction();

    @Getter(lazy = true, value = PRIVATE)
    private final Function<Flux<Value>, Flux<Value>> communicate = adoptCommunicate();

    private final ManagedValue<RsocketCommunicatorConfiguration> communicatorConfiguration = managed(this::communicatorConfiguration);
    private final ManagedValue<RsocketConnectorConfiguration> connectorConfiguration = managed(this::connectorConfiguration);
    private final ManagedValue<RsocketSetupPayload> setupPayload = managed(this::setupPayload);
    private final ManagedValue<RsocketPayloadWriter> writer = managed(this::writer);
    private final ManagedValue<RsocketPayloadReader> reader = managed(this::reader);
    private final ManagedValue<RSocketClient> client = managed(this::createClient);

    @Override
    public void initialize() {
        communicatorConfiguration.initialize();
        connectorConfiguration.initialize();
        setupPayload.initialize();
        writer.initialize();
        reader.initialize();
        client.initialize();
    }

    @Override
    public void dispose() {
        client.dispose(this::disposeClient);
        reader.dispose();
        writer.dispose();
        setupPayload.dispose();
        connectorConfiguration.dispose();
        communicatorConfiguration.dispose();
    }

    @Override
    public Flux<Value> communicate(Flux<Value> input) {
        return getCommunicate().apply(input);
    }

    private RSocketClient createClient() {
        RsocketConnectorConfiguration connectorConfiguration = this.connectorConfiguration.get();
        RsocketSetupPayload setupPayload = this.setupPayload.get();
        RSocketConnector connector = RSocketConnector.create()
                .dataMimeType(toMimeType(setupPayload.getDataFormat()).toString())
                .metadataMimeType(toMimeType(setupPayload.getMetadataFormat()).toString())
                .fragment(connectorConfiguration.getFragment())
                .interceptors(registry -> configureInterceptors(connectorConfiguration, registry));
        apply(connectorConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(connectorConfiguration.getResume(), connector::resume);
        apply(connectorConfiguration.getRetry(), connector::reconnect);
        connector.setupPayload(this.writer.get().writePayloadMetaData(setupPayload.toEntity()));
        switch (connectorConfiguration.getTransport()) {
            case TCP:
                TcpClient tcpClient = connectorConfiguration.getTcpClient();
                int tcpMaxFrameLength = connectorConfiguration.getTcpMaxFrameLength();
                RSocket socket = connector
                        .connect(TcpClientTransport.create(tcpClient, tcpMaxFrameLength))
                        .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, connectorId, setupPayload)))
                        .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                        .blockOptional()
                        .orElseThrow(ImpossibleSituation::new);
                return from(socket);
            case WS:
                HttpClient httpWebSocketClient = connectorConfiguration.getHttpWebSocketClient();
                String httpWebSocketPath = connectorConfiguration.getHttpWebSocketPath();
                socket = connector
                        .connect(WebsocketClientTransport.create(httpWebSocketClient, httpWebSocketPath))
                        .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, connectorId, setupPayload)))
                        .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable))
                        .blockOptional()
                        .orElseThrow(ImpossibleSituation::new);
                return from(socket);
        }
        throw new ImpossibleSituation();
    }

    private void configureInterceptors(RsocketConnectorConfiguration configuration, InterceptorRegistry registry) {
        apply(configuration.getInterceptorConfigurator(), configurator -> configurator.accept(registry));
        registry
                .forResponder(new RsocketConnectorLoggingInterceptor(connectorId))
                .forRequester(new RsocketConnectorLoggingInterceptor(connectorId));
    }

    private void disposeClient(RSocketClient rsocket) {
        disposeRsocket(rsocket);
        getLogger().info(format(COMMUNICATOR_STOPPED, connectorId, setupPayload));
    }

    @SuppressWarnings(CONSTANT_CONDITIONS)
    private CommunicatorAction communicatorAction() {
        return communicatorModule()
                .configuration()
                .getRegistry()
                .findActionById(communicatorActionId)
                .orElseThrow(ImpossibleSituation::new);
    }

    private RsocketCommunicatorConfiguration communicatorConfiguration() {
        return rsocketModule().configuration().getCommunicatorConfiguration();
    }

    private RsocketConnectorConfiguration connectorConfiguration() {
        return communicatorConfiguration.get().getConnectors().get(connectorId);
    }

    private RsocketSetupPayload setupPayload() {
        RsocketConnectorConfiguration connectorConfiguration = this.connectorConfiguration.get();
        CommunicatorAction communicatorAction = getCommunicatorAction();
        return RsocketSetupPayload.builder()
                .serviceMethod(communicatorAction.getTargetServiceMethod())
                .dataFormat(connectorConfiguration.getSetupPayload().getDataFormat())
                .metadataFormat(connectorConfiguration.getSetupPayload().getMetadataFormat())
                .build();
    }

    private RsocketPayloadWriter writer() {
        return new RsocketPayloadWriter(setupPayload.get().getDataFormat(), setupPayload.get().getMetadataFormat());
    }

    private RsocketPayloadReader reader() {
        return new RsocketPayloadReader(setupPayload.get().getDataFormat(), setupPayload.get().getMetadataFormat());
    }

    private CommunicationMode communicationMode() {
        CommunicatorAction communicatorAction = getCommunicatorAction();
        if (isNull(communicatorAction.getOutputMapper())) {
            return FIRE_AND_FORGET;
        }
        if (communicatorAction.getInputMode() == FLUX) {
            return REQUEST_CHANNEL;
        }
        if (communicatorAction.getOutputMode() == FLUX) {
            return REQUEST_STREAM;
        }
        return REQUEST_RESPONSE;
    }

    private Function<Flux<Value>, Flux<Value>> adoptCommunicate() {
        switch (communicationMode()) {
            case FIRE_AND_FORGET:
                return input -> cast(client.get()
                        .fireAndForget(input.map(this.writer.get()::writePayloadData).last(EmptyPayload.INSTANCE))
                        .flux());
            case REQUEST_RESPONSE:
                return input -> {
                    RsocketPayloadWriter writer = this.writer.get();
                    RsocketPayloadReader reader = this.reader.get();
                    return client.get()
                            .requestResponse(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE))
                            .flux()
                            .map(reader::readPayloadData)
                            .filter(data -> !data.isEmpty())
                            .map(RsocketPayloadValue::getValue);
                };
            case REQUEST_STREAM:
                return input -> {
                    RsocketPayloadWriter writer = this.writer.get();
                    RsocketPayloadReader reader = this.reader.get();
                    return client.get()
                            .requestStream(input.map(writer::writePayloadData).last(EmptyPayload.INSTANCE))
                            .map(reader::readPayloadData)
                            .filter(data -> !data.isEmpty())
                            .map(RsocketPayloadValue::getValue);
                };
            case REQUEST_CHANNEL:
                return input -> {
                    RsocketPayloadWriter writer = this.writer.get();
                    RsocketPayloadReader reader = this.reader.get();
                    return client.get()
                            .requestChannel(input.map(writer::writePayloadData))
                            .map(reader::readPayloadData)
                            .filter(data -> !data.isEmpty())
                            .map(RsocketPayloadValue::getValue);
                };
            case METADATA_PUSH:
                return input -> cast(client.get()
                        .metadataPush(input.map(writer.get()::writePayloadMetaData).last(EmptyPayload.INSTANCE))
                        .flux());
        }
        throw new ImpossibleSituation();
    }
}
