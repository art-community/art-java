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
import io.art.core.collection.*;
import io.art.core.exception.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.model.RsocketSetupPayload.*;
import io.art.transport.payload.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.loadbalance.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.property.Property.*;
import static io.art.logging.module.LoggingModule.*;
import static io.art.meta.constants.MetaConstants.MetaTypeInternalKind.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaModule.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.*;
import static io.art.rsocket.constants.RsocketModuleConstants.CommunicationMode.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.manager.RsocketManager.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.rsocket.reader.RsocketPayloadReader.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.rsocket.core.RSocketClient.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

public class RsocketCommunication implements Communication {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RsocketCommunication.class);
    private final RsocketModuleConfiguration configuration;
    private final Property<RSocketClient> client;
    private final Property<Function<Flux<Object>, Flux<Object>>> communication;
    private final String connector;
    private CommunicatorAction action;

    public RsocketCommunication(RsocketModuleConfiguration configuration, String connector) {
        this.configuration = configuration;
        client = property(this::createClient, this::disposeClient).listenConsumer(() -> configuration.getConsumer()
                .connectorConsumers()
                .consumerFor(connectorConfiguration().getConnector()));
        this.connector = connector;
        communication = property(this::communication)
                .listenProperties(client);
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

    private RSocketClient createClient() {
        RsocketTcpConnectorConfiguration tcpConnectorConfiguration = configuration.getTcpConnectors().get(connector);
        RsocketHttpConnectorConfiguration httpConnectorConfiguration = configuration.getHttpConnectors().get(connector);
        if (nonNull(tcpConnectorConfiguration)) {
            return createTcpClient(tcpConnectorConfiguration);
        }
        if (nonNull(httpConnectorConfiguration)) {
            return createHttpClient(httpConnectorConfiguration);
        }
        throw new ImpossibleSituationException();
    }

    private RSocketClient createTcpClient(RsocketTcpConnectorConfiguration connectorConfiguration) {
        RsocketCommonConnectorConfiguration commonConfiguration = connectorConfiguration.getCommonConfiguration();
        TransportPayloadWriter setupPayloadWriter = action.getWriter().apply(commonConfiguration.getDataFormat());
        RsocketSetupPayloadBuilder payloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(commonConfiguration.getDataFormat())
                .metadataFormat(commonConfiguration.getMetaDataFormat());
        ServiceMethodIdentifier targetServiceMethod = action.getTargetServiceMethod();
        if (nonNull(targetServiceMethod)) {
            payloadBuilder.serviceId(targetServiceMethod.getServiceId()).methodId(targetServiceMethod.getMethodId());
        }
        RsocketSetupPayload setupPayload = payloadBuilder.build();
        Payload payload = create(setupPayloadWriter.write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload)).nioBuffer());
        RSocketConnector connector = createConnector(commonConfiguration, payload);
        RsocketTcpClientGroupConfiguration groupConfiguration = connectorConfiguration.getGroupConfiguration();
        if (nonNull(groupConfiguration)) {
            List<LoadbalanceTarget> targets = linkedList();
            for (RsocketTcpClientConfiguration clientConfiguration : groupConfiguration.getClientConfigurations()) {
                RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
                TcpClient client = clientConfiguration.getDecorator().apply(TcpClient.create()
                        .host(commonClientConfiguration.getHost())
                        .port(commonClientConfiguration.getPort()));
                TcpClientTransport transport = TcpClientTransport.create(client, clientConfiguration.getMaxFrameLength());
                String key = commonClientConfiguration.getConnector() + COLON + commonClientConfiguration.getHost() + COLON + commonClientConfiguration.getPort();
                targets.add(LoadbalanceTarget.from(key, transport));
            }
            Mono<RSocket> socket = LoadbalanceRSocketClient.builder(Flux.just(targets))
                    .loadbalanceStrategy(groupConfiguration.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                    .connector(connector)
                    .build()
                    .source();
            socket = socket
                    .doOnTerminate(payload::release)
                    .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
            if (commonConfiguration.isLogging()) {
                socket = socket.doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, commonConfiguration.getConnector(), setupPayload)));
            }
            return from(socket.blockOptional().orElseThrow(ImpossibleSituationException::new));
        }
        RsocketTcpClientConfiguration clientConfiguration = connectorConfiguration.getSingleConfiguration();
        RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
        TcpClient client = clientConfiguration.getDecorator().apply(TcpClient.create()
                .host(commonClientConfiguration.getHost())
                .port(commonClientConfiguration.getPort()));
        Mono<RSocket> socket = connector
                .connect(TcpClientTransport.create(client, clientConfiguration.getMaxFrameLength()))
                .doOnTerminate(payload::release)
                .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
        if (commonConfiguration.isLogging()) {
            socket = socket.doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, commonConfiguration.getConnector(), setupPayload)));
        }
        return from(socket.blockOptional().orElseThrow(ImpossibleSituationException::new));
    }

    private RSocketClient createHttpClient(RsocketHttpConnectorConfiguration connectorConfiguration) {
        RsocketCommonConnectorConfiguration commonConfiguration = connectorConfiguration.getCommonConfiguration();
        TransportPayloadWriter setupPayloadWriter = action.getWriter().apply(commonConfiguration.getDataFormat());
        RsocketSetupPayloadBuilder payloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(commonConfiguration.getDataFormat())
                .metadataFormat(commonConfiguration.getMetaDataFormat());
        ServiceMethodIdentifier targetServiceMethod = action.getTargetServiceMethod();
        if (nonNull(targetServiceMethod)) {
            payloadBuilder.serviceId(targetServiceMethod.getServiceId()).methodId(targetServiceMethod.getMethodId());
        }
        RsocketSetupPayload setupPayload = payloadBuilder.build();
        Payload payload = create(setupPayloadWriter.write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload)).nioBuffer());
        RSocketConnector connector = createConnector(commonConfiguration, payload);
        RsocketHttpClientGroupConfiguration groupConfiguration = connectorConfiguration.getGroupConfiguration();
        if (nonNull(groupConfiguration)) {
            List<LoadbalanceTarget> targets = linkedList();
            for (RsocketHttpClientConfiguration clientConfiguration : groupConfiguration.getClientConfigurations()) {
                RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
                HttpClient client = clientConfiguration.getDecorator().apply(HttpClient.create()
                        .host(commonClientConfiguration.getHost())
                        .port(commonClientConfiguration.getPort()));
                WebsocketClientTransport transport = WebsocketClientTransport.create(client, clientConfiguration.getPath());
                String key = commonClientConfiguration.getConnector() + COLON + commonClientConfiguration.getHost() + COLON + commonClientConfiguration.getPort();
                targets.add(LoadbalanceTarget.from(key, transport));
            }
            Mono<RSocket> socket = LoadbalanceRSocketClient.builder(Flux.just(targets))
                    .loadbalanceStrategy(groupConfiguration.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                    .connector(connector)
                    .build()
                    .source();
            socket = socket
                    .doOnTerminate(payload::release)
                    .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
            if (commonConfiguration.isLogging()) {
                socket = socket.doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, commonConfiguration.getConnector(), setupPayload)));
            }
            return from(socket.blockOptional().orElseThrow(ImpossibleSituationException::new));
        }
        RsocketHttpClientConfiguration clientConfiguration = connectorConfiguration.getSingleConfiguration();
        RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
        HttpClient client = clientConfiguration.getDecorator().apply(HttpClient.create()
                .host(commonClientConfiguration.getHost())
                .port(commonClientConfiguration.getPort()));
        Mono<RSocket> socket = connector
                .connect(WebsocketClientTransport.create(client, clientConfiguration.getPath()))
                .doOnTerminate(payload::release)
                .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
        if (commonConfiguration.isLogging()) {
            socket = socket.doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, commonConfiguration.getConnector(), setupPayload)));
        }
        return from(socket.blockOptional().orElseThrow(ImpossibleSituationException::new));
    }

    private RSocketConnector createConnector(RsocketCommonConnectorConfiguration commonConfiguration, Payload payload) {
        RSocketConnector connector = RSocketConnector.create()
                .payloadDecoder(commonConfiguration.getPayloadDecoderMode() == ZERO_COPY ? PayloadDecoder.ZERO_COPY : PayloadDecoder.DEFAULT)
                .dataMimeType(toMimeType(commonConfiguration.getDataFormat()).toString())
                .metadataMimeType(toMimeType(commonConfiguration.getMetaDataFormat()).toString())
                .fragment(commonConfiguration.getFragment())
                .interceptors(registry -> configureInterceptors(commonConfiguration, registry));
        apply(commonConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(commonConfiguration.getResume(), resume -> connector.resume(resume.toResume()));
        apply(commonConfiguration.getRetry(), retry -> connector.reconnect(retry.toRetry()));

        return connector.setupPayload(payload);
    }

    private void configureInterceptors(RsocketCommonConnectorConfiguration connectorConfiguration, InterceptorRegistry registry) {
        registry.forResponder(new RsocketConnectorLoggingInterceptor(configuration, connectorConfiguration))
                .forRequester(new RsocketConnectorLoggingInterceptor(configuration, connectorConfiguration));
    }

    private void disposeClient(RSocketClient rsocket) {
        RsocketCommonConnectorConfiguration connectorConfiguration = connectorConfiguration();
        disposeRsocket(rsocket);
        if (withLogging() && connectorConfiguration.isLogging()) {
            getLogger().info(format(COMMUNICATOR_STOPPED, connectorConfiguration.getConnector()));
        }
    }

    private RsocketCommonConnectorConfiguration connectorConfiguration() {
        ImmutableMap<String, RsocketTcpConnectorConfiguration> tcpConnectorConfigurations = rsocketModule().configuration().getTcpConnectors();
        ImmutableMap<String, RsocketHttpConnectorConfiguration> httpConnectorConfigurations = rsocketModule().configuration().getHttpConnectors();
        RsocketTcpConnectorConfiguration tcpConnectorConfiguration = tcpConnectorConfigurations.get(connector);
        RsocketHttpConnectorConfiguration httpConnectorConfiguration = httpConnectorConfigurations.get(connector);
        return let(tcpConnectorConfiguration,
                (Function<RsocketTcpConnectorConfiguration, RsocketCommonConnectorConfiguration>) RsocketTcpConnectorConfiguration::getCommonConfiguration,
                ((Supplier<RsocketCommonConnectorConfiguration>) () -> httpConnectorConfiguration.getCommonConfiguration())
        );
    }

    private Function<Flux<Object>, Flux<Object>> communication() {
        TransportPayloadReader reader = action.getReader().apply(connectorConfiguration().getDataFormat());
        TransportPayloadWriter writer = action.getWriter().apply(connectorConfiguration().getDataFormat());
        RSocketClient client = this.client.get();
        switch (communicationMode()) {
            case FIRE_AND_FORGET:
                return input -> cast(client.fireAndForget(input.map(value -> create(writer.write(typed(action.getInputType(), value)))).last(EMPTY_PAYLOAD)).flux());
            case REQUEST_RESPONSE:
                return input -> client
                        .requestResponse(input.map(value -> create(writer.write(typed(action.getInputType(), value)))).last(EMPTY_PAYLOAD))
                        .flux()
                        .map(payload -> readRsocketPayload(reader, payload, action.getOutputType()))
                        .filter(data -> !data.isEmpty())
                        .map(TransportPayload::getValue);
            case REQUEST_STREAM:
                return input -> client
                        .requestStream(input.map(value -> create(writer.write(typed(action.getInputType(), value)))).last(EMPTY_PAYLOAD))
                        .map(payload -> readRsocketPayload(reader, payload, action.getOutputType()))
                        .filter(data -> !data.isEmpty())
                        .map(TransportPayload::getValue);
            case REQUEST_CHANNEL:
                return input -> client
                        .requestChannel(input.map(value -> create(writer.write(typed(action.getInputType(), value)))).switchIfEmpty(EMPTY_PAYLOAD_MONO.get()))
                        .map(payload -> readRsocketPayload(reader, payload, action.getOutputType()))
                        .filter(data -> !data.isEmpty())
                        .map(TransportPayload::getValue);
            case METADATA_PUSH:
                return input -> cast(client.metadataPush(input.map(value -> create(writer.write(typed(action.getInputType(), value)))).last(EMPTY_PAYLOAD)).flux());
        }
        throw new ImpossibleSituationException();
    }

    private CommunicationMode communicationMode() {
        if (isNull(action.getOutputType()) || action.getOutputType().internalKind() == VOID) {
            return FIRE_AND_FORGET;
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
