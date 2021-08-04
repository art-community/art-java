package io.art.rsocket.communicator;

import io.art.core.exception.*;
import io.art.core.model.*;
import io.art.core.strategy.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
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
import reactor.netty.tcp.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.logging.module.LoggingModule.*;
import static io.art.meta.model.TypedObject.*;
import static io.art.meta.module.MetaModule.*;
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
import java.util.*;
import java.util.function.*;

@UtilityClass
public class RsocketCommunicationFactory {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(RSOCKET_COMMUNICATOR_LOGGER);


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
        Payload payload = create(common
                .getSetupPayloadWriter()
                .get()
                .write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload))
                .nioBuffer());
        RSocketConnector connector = createConnector(common, payload);
        RsocketTcpClientGroupConfiguration group = connectorConfiguration.getGroupConfiguration();
        if (nonNull(group) && isNotEmpty(group.getClientConfigurations())) {
            return configureSocket(common, createTcpBalancer(connector, group), setupPayload);
        }
        return configureSocket(common, createTcpClient(connectorConfiguration.getSingleConfiguration(), connector), setupPayload);
    }

    private static Mono<RSocket> createTcpBalancer(RSocketConnector connector, RsocketTcpClientGroupConfiguration group) {
        List<LoadbalanceTarget> targets = linkedList();
        for (RsocketTcpClientConfiguration clientConfiguration : group.getClientConfigurations()) {
            RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
            TcpClient client = clientConfiguration.getDecorator().apply(TcpClient.create()
                    .host(commonClientConfiguration.getHost())
                    .port(commonClientConfiguration.getPort()));
            TcpClientTransport transport = TcpClientTransport.create(client, clientConfiguration.getMaxFrameLength());
            String key = commonClientConfiguration.getConnector() + COLON + commonClientConfiguration.getHost() + COLON + commonClientConfiguration.getPort();
            targets.add(LoadbalanceTarget.from(key, transport));
        }
        return LoadbalanceRSocketClient.builder(Flux.just(targets))
                .loadbalanceStrategy(group.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                .connector(connector)
                .build()
                .source();
    }

    private static Mono<RSocket> createTcpClient(RsocketTcpClientConfiguration clientConfiguration, RSocketConnector connector) {
        RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
        TcpClient client = clientConfiguration.getDecorator().apply(TcpClient.create()
                .host(commonClientConfiguration.getHost())
                .port(commonClientConfiguration.getPort()));
        return connector.connect(TcpClientTransport.create(client, clientConfiguration.getMaxFrameLength()));
    }


    private static RSocketClient createHttpClient(RsocketHttpConnectorConfiguration connectorConfiguration, CommunicatorActionIdentifier identifier) {
        RsocketCommonConnectorConfiguration common = connectorConfiguration.getCommonConfiguration();
        RsocketSetupPayload setupPayload = createSetupPayload(common, identifier);
        Payload payload = create(common
                .getSetupPayloadWriter()
                .get()
                .write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload))
                .nioBuffer());
        RSocketConnector connector = createConnector(common, payload);
        RsocketHttpClientGroupConfiguration group = connectorConfiguration.getGroupConfiguration();
        if (nonNull(group) && isNotEmpty(group.getClientConfigurations())) {
            return configureSocket(common, createHttpBalancer(connector, group), setupPayload);
        }
        return configureSocket(common, createHttpClient(connectorConfiguration.getSingleConfiguration(), connector), setupPayload);
    }

    private static Mono<RSocket> createHttpBalancer(RSocketConnector connector, RsocketHttpClientGroupConfiguration group) {
        List<LoadbalanceTarget> targets = linkedList();
        for (RsocketHttpClientConfiguration clientConfiguration : group.getClientConfigurations()) {
            RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
            HttpClient client = clientConfiguration.getDecorator().apply(HttpClient.create()
                    .host(commonClientConfiguration.getHost())
                    .port(commonClientConfiguration.getPort()));
            WebsocketClientTransport transport = WebsocketClientTransport.create(client, clientConfiguration.getPath());
            String key = commonClientConfiguration.getConnector() + COLON + commonClientConfiguration.getHost() + COLON + commonClientConfiguration.getPort();
            targets.add(LoadbalanceTarget.from(key, transport));
        }
        return LoadbalanceRSocketClient.builder(Flux.just(targets))
                .loadbalanceStrategy(group.getBalancer() == ROUND_ROBIN ? new RoundRobinLoadbalanceStrategy() : WeightedLoadbalanceStrategy.builder().build())
                .connector(connector)
                .build()
                .source();
    }

    private static Mono<RSocket> createHttpClient(RsocketHttpClientConfiguration clientConfiguration, RSocketConnector connector) {
        RsocketCommonClientConfiguration commonClientConfiguration = clientConfiguration.getCommonConfiguration();
        HttpClient client = clientConfiguration.getDecorator().apply(HttpClient.create()
                .host(commonClientConfiguration.getHost())
                .port(commonClientConfiguration.getPort()));
        return connector.connect(WebsocketClientTransport.create(client, clientConfiguration.getPath()));
    }


    private static RsocketSetupPayload createSetupPayload(RsocketCommonConnectorConfiguration common, CommunicatorActionIdentifier identifier) {
        ServiceMethodIdentifier targetServiceMethod = common.getService().apply(new ServiceMethodStrategy()).id(identifier);
        RsocketSetupPayload.RsocketSetupPayloadBuilder payloadBuilder = RsocketSetupPayload.builder()
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
        socket = socket.timeout(common.getTimeout());
        if (common.isLogging()) {
            socket = socket
                    .doOnSubscribe(subscription -> getLogger().info(format(COMMUNICATOR_STARTED, common.getConnector(), setupPayload)))
                    .doOnError(throwable -> getLogger().error(throwable.getMessage(), throwable));
        }
        return from(socket.blockOptional().orElseThrow(ImpossibleSituationException::new));
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
        return connector.setupPayload(payload);
    }


    private static void configureInterceptors(RsocketCommonConnectorConfiguration connectorConfiguration, InterceptorRegistry registry) {
        registry.forResponder(new RsocketConnectorLoggingInterceptor(rsocketModule().configuration(), connectorConfiguration))
                .forRequester(new RsocketConnectorLoggingInterceptor(rsocketModule().configuration(), connectorConfiguration));
    }
}
