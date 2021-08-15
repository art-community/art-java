package io.art.rsocket.communicator;

import io.art.core.model.*;
import io.art.core.strategy.*;
import io.art.logging.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.exception.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.rsocket.model.RsocketSetupPayload.*;
import io.art.transport.payload.*;
import io.netty.handler.ssl.*;
import io.rsocket.*;
import io.rsocket.core.*;
import io.rsocket.frame.decoder.*;
import io.rsocket.loadbalance.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import lombok.experimental.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import reactor.netty.tcp.SslProvider;
import reactor.netty.tcp.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.handler.ExceptionHandler.*;
import static io.art.meta.Meta.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.LoggingMessages.*;
import static io.art.rsocket.constants.RsocketModuleConstants.PayloadDecoderMode.*;
import static io.art.rsocket.module.RsocketModule.*;
import static io.art.transport.mime.MimeTypeDataFormatMapper.*;
import static io.rsocket.core.RSocketClient.*;
import static io.rsocket.util.DefaultPayload.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.function.*;

@UtilityClass
public class RsocketCommunicationFactory {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = Logging.logger(RSOCKET_COMMUNICATOR_LOGGER);


    public static RsocketCommunication createTcpCommunication(RsocketTcpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        String connector = connectorConfiguration.getCommonConfiguration().getConnector();
        RsocketModuleConfiguration moduleConfiguration = rsocketModule().configuration();
        Supplier<RSocketClient> client = () -> createTcpClient(moduleConfiguration.getTcpConnectors().get(connector), identifier);
        return new RsocketCommunication(client, moduleConfiguration, connectorConfiguration.getCommonConfiguration());
    }

    public static RsocketCommunication createHttpCommunication(RsocketHttpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        String connector = connectorConfiguration.getCommonConfiguration().getConnector();
        RsocketModuleConfiguration moduleConfiguration = rsocketModule().configuration();
        Supplier<RSocketClient> client = () -> createHttpClient(moduleConfiguration.getHttpConnectors().get(connector), identifier);
        return new RsocketCommunication(client, moduleConfiguration, connectorConfiguration.getCommonConfiguration());
    }


    private static RSocketClient createTcpClient(RsocketTcpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        RsocketCommonConnectorConfiguration common = connectorConfiguration.getCommonConfiguration();
        RsocketSetupPayload setupPayload = createSetupPayload(common, identifier);
        ByteBuffer payloadData = new TransportPayloadWriter(common.getDataFormat())
                .write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload))
                .nioBuffer();
        Payload payload = create(payloadData);
        RSocketConnector connector = createConnector(common, payload);
        RsocketTcpClientGroupConfiguration group = connectorConfiguration.getGroupConfiguration();
        if (nonNull(group) && isNotEmpty(group.getClientConfigurations())) {
            return createTcpBalancer(connector, group);
        }
        return configureSocket(common, createTcpClient(common, connectorConfiguration.getSingleConfiguration(), connector), setupPayload);
    }

    private static LoadbalanceRSocketClient createTcpBalancer(RSocketConnector connector, RsocketTcpClientGroupConfiguration group) {
        List<LoadbalanceTarget> targets = linkedList();
        for (RsocketTcpClientConfiguration clientConfiguration : group.getClientConfigurations()) {
            TcpClient client = clientConfiguration.getClientDecorator().apply(TcpClient.create()
                    .host(clientConfiguration.getHost())
                    .port(clientConfiguration.getPort()));
            UnaryOperator<TcpClient> groupClientDecorator = group.getClientDecorator();
            UnaryOperator<TcpClientTransport> transportDecorator = group.getTransportDecorator();
            TcpClientTransport transport = transportDecorator.apply(TcpClientTransport.create(groupClientDecorator.apply(client), clientConfiguration.getMaxFrameLength()));
            String key = clientConfiguration.getConnector() + COLON + clientConfiguration.getHost() + COLON + clientConfiguration.getPort();
            targets.add(LoadbalanceTarget.from(key, transport));
        }
        return LoadbalanceRSocketClient.builder(Flux.just(targets))
                .loadbalanceStrategy(group.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                .connector(connector)
                .build();
    }

    private static Mono<RSocket> createTcpClient(RsocketCommonConnectorConfiguration connectorConfiguration, RsocketTcpClientConfiguration clientConfiguration, RSocketConnector connector) {
        UnaryOperator<TcpClient> clientDecorator = clientConfiguration.getClientDecorator();
        UnaryOperator<TcpClientTransport> transportDecorator = clientConfiguration.getTransportDecorator();
        TcpClient client = clientDecorator.apply(TcpClient.create()
                .host(clientConfiguration.getHost())
                .port(clientConfiguration.getPort()));
        RsocketSslConfiguration ssl = connectorConfiguration.getSsl();
        if (nonNull(ssl)) {
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
                        password
                );
            }
            client.secure(SslProvider.builder().sslContext((SslContext) wrapException(RsocketException::new).call(sslBuilder::build)).build());
        }

        return connector.connect(transportDecorator.apply(TcpClientTransport.create(client, clientConfiguration.getMaxFrameLength())));
    }


    private static RSocketClient createHttpClient(RsocketHttpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        RsocketCommonConnectorConfiguration common = connectorConfiguration.getCommonConfiguration();
        RsocketSetupPayload setupPayload = createSetupPayload(common, identifier);
        ByteBuffer payloadData = new TransportPayloadWriter(common.getDataFormat())
                .write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload))
                .nioBuffer();
        Payload payload = create(payloadData);
        RSocketConnector connector = createConnector(common, payload);
        RsocketHttpClientGroupConfiguration group = connectorConfiguration.getGroupConfiguration();
        if (nonNull(group) && isNotEmpty(group.getClientConfigurations())) {
            return createHttpBalancer(connector, group);
        }
        return configureSocket(common, createHttpClient(common, connectorConfiguration.getSingleConfiguration(), connector), setupPayload);
    }

    private static LoadbalanceRSocketClient createHttpBalancer(RSocketConnector connector, RsocketHttpClientGroupConfiguration group) {
        List<LoadbalanceTarget> targets = linkedList();
        for (RsocketHttpClientConfiguration clientConfiguration : group.getClientConfigurations()) {
            HttpClient client = clientConfiguration.getClientDecorator().apply(HttpClient.create()
                    .host(clientConfiguration.getHost())
                    .port(clientConfiguration.getPort()));
            UnaryOperator<WebsocketClientTransport> transportDecorator = group.getTransportDecorator();
            UnaryOperator<HttpClient> groupClientDecorator = group.getClientDecorator();
            WebsocketClientTransport transport = transportDecorator.apply(WebsocketClientTransport.create(groupClientDecorator.apply(client), clientConfiguration.getPath()));
            String key = clientConfiguration.getConnector() + COLON + clientConfiguration.getHost() + COLON + clientConfiguration.getPort();
            targets.add(LoadbalanceTarget.from(key, transport));
        }
        return LoadbalanceRSocketClient.builder(Flux.just(targets))
                .loadbalanceStrategy(group.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                .connector(connector)
                .build();
    }

    private static Mono<RSocket> createHttpClient(RsocketCommonConnectorConfiguration connectorConfiguration, RsocketHttpClientConfiguration clientConfiguration, RSocketConnector connector) {
        UnaryOperator<HttpClient> clientDecorator = clientConfiguration.getClientDecorator();
        UnaryOperator<WebsocketClientTransport> transportDecorator = clientConfiguration.getTransportDecorator();
        HttpClient client = clientDecorator.apply(HttpClient.create()
                .host(clientConfiguration.getHost())
                .port(clientConfiguration.getPort()));
        RsocketSslConfiguration ssl = connectorConfiguration.getSsl();
        if (nonNull(ssl)) {
            File certificate = ssl.getCertificate();
            File key = ssl.getKey();
            SslContextBuilder sslBuilder = SslContextBuilder.forClient();
            if (nonNull(certificate) && certificate.exists() && nonNull(key) && key.exists()) {
                sslBuilder.keyManager(certificate, key);
            }
            String password = ssl.getPassword();
            if (isNotEmpty(password)) {
                sslBuilder.keyManager(certificate, key, password);
            }
            client.secure(SslProvider.builder().sslContext((SslContext) wrapException(RsocketException::new).call(sslBuilder::build)).build());
        }
        return connector.connect(transportDecorator.apply(WebsocketClientTransport.create(client, clientConfiguration.getPath())));
    }


    private static RsocketSetupPayload createSetupPayload(RsocketCommonConnectorConfiguration common, CommunicatorActionIdentifier identifier) {
        ServiceMethodIdentifier targetServiceMethod = common.getService().apply(new ServiceMethodStrategy()).id(identifier);
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

    private static RSocketClient configureSocket(RsocketCommonConnectorConfiguration common, Mono<RSocket> socket, RsocketSetupPayload setupPayload) {
        Mono<RSocket> configured = socket.timeout(common.getTimeout());
        if (common.isLogging()) {
            configured = configured
                    .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, common.getConnector(), setupPayload)))
                    .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
        }
        return from(configured);
    }

    private static RSocketConnector createConnector(RsocketCommonConnectorConfiguration commonConfiguration, Payload payload) {
        RSocketConnector connector = RSocketConnector.create()
                .payloadDecoder(commonConfiguration.getPayloadDecoderMode() == ZERO_COPY ? PayloadDecoder.ZERO_COPY : PayloadDecoder.DEFAULT)
                .dataMimeType(toMimeType(commonConfiguration.getDataFormat()).toString())
                .metadataMimeType(toMimeType(commonConfiguration.getMetaDataFormat()).toString())
                .fragment(commonConfiguration.getFragment())
                .interceptors(registry -> configureInterceptors(commonConfiguration, registry));
        apply(commonConfiguration.getKeepAlive(), keepAlive -> connector.keepAlive(keepAlive.getInterval(), keepAlive.getMaxLifeTime()));
        apply(commonConfiguration.getResume(), resume -> connector.resume(resume.toResume()));
        apply(commonConfiguration.getRetry(), retry -> connector.reconnect(retry.toRetry()));
        return commonConfiguration.getDecorator().apply(connector.setupPayload(payload));
    }


    private static void configureInterceptors(RsocketCommonConnectorConfiguration connectorConfiguration, InterceptorRegistry registry) {
        UnaryOperator<InterceptorRegistry> interceptors = connectorConfiguration.getInterceptors();
        interceptors.apply(registry
                .forResponder(new RsocketConnectorLoggingInterceptor(rsocketModule().configuration(), connectorConfiguration))
                .forRequester(new RsocketConnectorLoggingInterceptor(rsocketModule().configuration(), connectorConfiguration)));
    }
}
