package io.art.rsocket.communicator;

import io.art.core.model.*;
import io.art.core.property.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.model.RsocketSetupPayload.*;
import io.netty.handler.ssl.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.loadbalance.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.client.*;
import io.rsocket.util.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.Meta.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Errors.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Messages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.transport.extensions.TransportExtensions.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.art.transport.payload.TransportPayloadWriter.*;
import static io.rsocket.core.RSocketClient.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class RsocketCommunicationFactory {
    private final static LazyProperty<Logger> logger = lazy(() -> Logging.logger(RSOCKET_COMMUNICATOR_LOGGER));

    public static RsocketCommunication createConfiguredTcpCommunication(RsocketTcpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        String connector = connectorConfiguration.getCommonConfiguration().getConnector();
        RsocketModuleConfiguration moduleConfiguration = rsocketModule().configuration();
        Supplier<RSocketClient> client = () -> configureTcpClient(identifier, connector, moduleConfiguration);
        return new RsocketCommunication(client, moduleConfiguration, connectorConfiguration.getCommonConfiguration());
    }

    public static RsocketCommunication createConfiguredWsCommunication(RsocketWsConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        String connector = connectorConfiguration.getCommonConfiguration().getConnector();
        RsocketModuleConfiguration moduleConfiguration = rsocketModule().configuration();
        Supplier<RSocketClient> client = () -> configureWsClient(identifier, connector, moduleConfiguration);
        return new RsocketCommunication(client, moduleConfiguration, connectorConfiguration.getCommonConfiguration());
    }

    public static RsocketCommunication createDefaultTcpCommunication(RsocketTcpConnectorConfiguration connectorConfiguration, RsocketSetupPayload setupPayload) {
        Supplier<RSocketClient> client = () -> createTcpClient(connectorConfiguration, setupPayload);
        return new RsocketCommunication(client, connectorConfiguration.getCommonConfiguration());
    }

    public static RsocketCommunication createDefaultWsCommunication(RsocketWsConnectorConfiguration connectorConfiguration, RsocketSetupPayload setupPayload) {
        Supplier<RSocketClient> client = () -> createWsClient(connectorConfiguration, setupPayload);
        return new RsocketCommunication(client, connectorConfiguration.getCommonConfiguration());
    }

    public static RsocketSetupPayload createSetupPayload(RsocketCommonConnectorConfiguration common, ServiceMethodIdentifier targetServiceMethod) {
        RsocketSetupPayloadBuilder payloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(common.getDataFormat())
                .metadataFormat(common.getMetaDataFormat());
        if (nonNull(targetServiceMethod)) {
            payloadBuilder
                    .serviceId(targetServiceMethod.getServiceId())
                    .methodId(targetServiceMethod.getMethodId());
        }
        return payloadBuilder.build();
    }

    private static RSocketClient createTcpClient(RsocketTcpConnectorConfiguration connectorConfiguration, RsocketSetupPayload setupPayload) {
        RsocketCommonConnectorConfiguration common = connectorConfiguration.getCommonConfiguration();
        ByteBuffer payloadData = transportPayloadWriter(common.getDataFormat())
                .write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload))
                .nioBuffer();
        Payload payload = DefaultPayload.create(payloadData);
        RSocketConnector connector = createConnector(common, payload);
        if (connectorConfiguration.getClientConfigurations().size() > 1) {
            return createTcpBalancer(connector, connectorConfiguration);
        }
        if (connectorConfiguration.getClientConfigurations().isEmpty()) {
            throw new RsocketException(CLIENTS_EMPTY);
        }
        RsocketTcpClientConfiguration firstClient = connectorConfiguration.getClientConfigurations().asArray().get(0);
        return configureSocket(common, createTcpClient(common, firstClient, connector), setupPayload);
    }

    private static LoadbalanceRSocketClient createTcpBalancer(RSocketConnector connector, RsocketTcpConnectorConfiguration connectorConfiguration) {
        List<LoadbalanceTarget> targets = linkedList();
        for (RsocketTcpClientConfiguration clientConfiguration : connectorConfiguration.getClientConfigurations()) {
            TcpClient client = clientConfiguration.getClientDecorator().apply(TcpClient.create()
                    .host(clientConfiguration.getHost())
                    .port(clientConfiguration.getPort()));
            UnaryOperator<TcpClient> clientDecorator = connectorConfiguration.getClientDecorator();
            UnaryOperator<TcpClientTransport> transportDecorator = connectorConfiguration.getTransportDecorator();
            TcpClientTransport transport = transportDecorator.apply(TcpClientTransport.create(clientDecorator.apply(client), clientConfiguration.getMaxFrameLength()));
            String key = clientConfiguration.getConnector() + COLON + clientConfiguration.getHost() + COLON + clientConfiguration.getPort();
            targets.add(LoadbalanceTarget.from(key, transport));
        }
        return LoadbalanceRSocketClient.builder(Flux.just(targets))
                .loadbalanceStrategy(connectorConfiguration.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                .connector(connector)
                .build();
    }

    private static Mono<RSocket> createTcpClient(RsocketCommonConnectorConfiguration connectorConfiguration,
                                                 RsocketTcpClientConfiguration clientConfiguration,
                                                 RSocketConnector connector) {
        UnaryOperator<TcpClient> clientDecorator = clientConfiguration.getClientDecorator();
        UnaryOperator<TcpClientTransport> transportDecorator = clientConfiguration.getTransportDecorator();
        TcpClient client = clientDecorator.apply(TcpClient.create()
                .host(clientConfiguration.getHost())
                .port(clientConfiguration.getPort()));
        RsocketSslConfiguration ssl = connectorConfiguration.getSsl();
        if (nonNull(ssl)) client.secure(createSslContext(ssl));
        return connector.connect(transportDecorator.apply(TcpClientTransport.create(client, clientConfiguration.getMaxFrameLength())));
    }

    private static RSocketClient createWsClient(RsocketWsConnectorConfiguration connectorConfiguration, RsocketSetupPayload setupPayload) {
        RsocketCommonConnectorConfiguration common = connectorConfiguration.getCommonConfiguration();
        ByteBuffer payloadData = transportPayloadWriter(common.getDataFormat())
                .write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload))
                .nioBuffer();
        Payload payload = create(payloadData);
        RSocketConnector connector = createConnector(common, payload);
        if (connectorConfiguration.getClientConfigurations().size() > 1) {
            return createWsBalancer(connector, connectorConfiguration);
        }
        if (connectorConfiguration.getClientConfigurations().isEmpty()) {
            throw new RsocketException(CLIENTS_EMPTY);
        }
        RsocketWsClientConfiguration firstClient = connectorConfiguration.getClientConfigurations().asArray().get(0);
        return configureSocket(common, createWsClient(common, firstClient, connector), setupPayload);
    }

    private static LoadbalanceRSocketClient createWsBalancer(RSocketConnector connector, RsocketWsConnectorConfiguration connectorConfiguration) {
        List<LoadbalanceTarget> targets = linkedList();
        for (RsocketWsClientConfiguration clientConfiguration : connectorConfiguration.getClientConfigurations()) {
            HttpClient client = clientConfiguration.getClientDecorator().apply(HttpClient.create()
                    .host(clientConfiguration.getHost())
                    .port(clientConfiguration.getPort()));
            UnaryOperator<WebsocketClientTransport> transportDecorator = connectorConfiguration.getTransportDecorator();
            UnaryOperator<HttpClient> clientDecorator = connectorConfiguration.getClientDecorator();
            WebsocketClientTransport transport = transportDecorator.apply(WebsocketClientTransport.create(clientDecorator.apply(client), clientConfiguration.getPath()));
            String key = clientConfiguration.getConnector() + COLON + clientConfiguration.getHost() + COLON + clientConfiguration.getPort();
            targets.add(LoadbalanceTarget.from(key, transport));
        }
        return LoadbalanceRSocketClient.builder(Flux.just(targets))
                .loadbalanceStrategy(connectorConfiguration.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                .connector(connector)
                .build();
    }

    private static RSocketClient configureWsClient(CommunicatorActionIdentifier identifier, String connector, RsocketModuleConfiguration moduleConfiguration) {
        RsocketWsConnectorConfiguration configuration = moduleConfiguration.getWsConnectors().get(connector);
        return createWsClient(configuration, createSetupPayload(configuration.getCommonConfiguration(), identifier));
    }

    private static RSocketClient configureTcpClient(CommunicatorActionIdentifier identifier, String connector, RsocketModuleConfiguration moduleConfiguration) {
        RsocketWsConnectorConfiguration configuration = moduleConfiguration.getWsConnectors().get(connector);
        return createTcpClient(moduleConfiguration.getTcpConnectors().get(connector), createSetupPayload(configuration.getCommonConfiguration(), identifier));
    }

    private static Mono<RSocket> createWsClient(RsocketCommonConnectorConfiguration connectorConfiguration,
                                                RsocketWsClientConfiguration clientConfiguration,
                                                RSocketConnector connector) {
        UnaryOperator<HttpClient> clientDecorator = clientConfiguration.getClientDecorator();
        UnaryOperator<WebsocketClientTransport> transportDecorator = clientConfiguration.getTransportDecorator();
        HttpClient client = clientDecorator.apply(HttpClient.create()
                .host(clientConfiguration.getHost())
                .port(clientConfiguration.getPort()));
        RsocketSslConfiguration ssl = connectorConfiguration.getSsl();
        if (nonNull(ssl)) client.secure(createSslContext(ssl));
        return connector.connect(transportDecorator.apply(WebsocketClientTransport.create(client, clientConfiguration.getPath())));
    }

    private static SslProvider createSslContext(RsocketSslConfiguration ssl) {
        try {
            File certificate = ssl.getCertificate();
            File key = ssl.getKey();
            SslContextBuilder sslBuilder = SslContextBuilder.forClient();
            if (nonNull(key) && key.exists()) {
                sslBuilder.keyManager(nonNull(certificate) && certificate.exists() ? certificate : null, key);
            }
            String password = ssl.getPassword();
            if (isNotEmpty(password)) {
                sslBuilder.keyManager(
                        nonNull(certificate) && certificate.exists() ? certificate : null,
                        nonNull(key) && key.exists() ? key : null,
                        password);
            }
            return SslProvider.builder().sslContext(sslBuilder.build()).build();
        } catch (Throwable throwable) {
            throw new RsocketException(throwable);
        }
    }

    private static RsocketSetupPayload createSetupPayload(RsocketCommonConnectorConfiguration common, CommunicatorActionIdentifier identifier) {
        ServiceMethodIdentifier targetServiceMethod = common.getService().id(identifier);
        return createSetupPayload(common, targetServiceMethod);
    }

    private static RSocketClient configureSocket(RsocketCommonConnectorConfiguration common, Mono<RSocket> socket, RsocketSetupPayload setupPayload) {
        Mono<RSocket> configured = socket.timeout(common.getTimeout());
        if (withLogging() && common.isVerbose()) {
            configured = configured
                    .doOnSubscribe(subscription -> logger.get().info(format(RSOCKET_COMMUNICATOR_STARTED, common.getConnector(), toPrettyString(setupPayload))))
                    .doOnError(throwable -> logger.get().error(throwable.getMessage(), throwable));
        }
        return from(configured);
    }

    private static RSocketConnector createConnector(RsocketCommonConnectorConfiguration commonConfiguration, Payload payload) {
        RSocketConnector connector = RSocketConnector.create()
                .payloadDecoder(commonConfiguration.getPayloadDecoderMode() == ZERO_COPY ? PayloadDecoder.ZERO_COPY : PayloadDecoder.DEFAULT)
                .dataMimeType(toMimeType(commonConfiguration.getDataFormat()).toString())
                .metadataMimeType(toMimeType(commonConfiguration.getMetaDataFormat()).toString())
                .fragment(commonConfiguration.getFragment())
                .maxInboundPayloadSize(commonConfiguration.getMaxInboundPayloadSize())
                .interceptors(registry -> configureInterceptors(commonConfiguration, registry));
        apply(commonConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(commonConfiguration.getResume(), resume -> connector.resume(resume.toResume()));
        apply(commonConfiguration.getRetry(), retry -> connector.reconnect(retry.toRetry()));
        return commonConfiguration.getDecorator().apply(connector.setupPayload(payload));
    }

    private static void configureInterceptors(RsocketCommonConnectorConfiguration connectorConfiguration, InterceptorRegistry registry) {
        UnaryOperator<InterceptorRegistry> interceptors = connectorConfiguration.getInterceptors();
        if (withLogging()) {
            interceptors.apply(registry
                    .forResponder(new RsocketConnectorLoggingInterceptor(rsocketModule().configuration(), connectorConfiguration))
                    .forRequester(new RsocketConnectorLoggingInterceptor(rsocketModule().configuration(), connectorConfiguration)));
        }
    }
}
