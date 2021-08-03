package io.art.rsocket.communicator;

import io.art.core.exception.*;
import io.art.core.model.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.interceptor.*;
import io.art.rsocket.model.*;
import io.art.transport.payload.*;
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
    private final static Logger logger = logger(RsocketCommunication.class);


    public static RsocketCommunication createTcpCommunication(RsocketTcpConnectorConfiguration connectorConfiguration) {
        Supplier<RSocketClient> client = () -> createTcpClient(connectorConfiguration);
        return new RsocketCommunication(client, rsocketModule().configuration(), connectorConfiguration.getCommonConfiguration());
    }

    public static RsocketCommunication createHttpCommunication(RsocketHttpConnectorConfiguration connectorConfiguration) {
        Supplier<RSocketClient> client = () -> createHttpClient(connectorConfiguration);
        return new RsocketCommunication(client, rsocketModule().configuration(), connectorConfiguration.getCommonConfiguration());
    }


    private static RSocketClient createTcpClient(RsocketTcpConnectorConfiguration connectorConfiguration) {
        RsocketCommonConnectorConfiguration commonConfiguration = connectorConfiguration.getCommonConfiguration();
        ServiceMethodIdentifier targetServiceMethod = commonConfiguration.getTarget();
        TransportPayloadWriter setupPayloadWriter = commonConfiguration.getSetupPayloadWriter().get();
        RsocketSetupPayload.RsocketSetupPayloadBuilder payloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(commonConfiguration.getDataFormat())
                .metadataFormat(commonConfiguration.getMetaDataFormat());
        if (nonNull(targetServiceMethod)) {
            payloadBuilder.serviceId(targetServiceMethod.getServiceId()).methodId(targetServiceMethod.getMethodId());
        }
        RsocketSetupPayload setupPayload = payloadBuilder.build();
        Payload payload = create(setupPayloadWriter.write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload)).nioBuffer());
        RSocketConnector connector = createConnector(commonConfiguration, payload);
        RsocketTcpClientGroupConfiguration groupConfiguration = connectorConfiguration.getGroupConfiguration();
        if (nonNull(groupConfiguration) && isNotEmpty(groupConfiguration.getClientConfigurations())) {
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

    private static RSocketClient createHttpClient(RsocketHttpConnectorConfiguration connectorConfiguration) {
        RsocketCommonConnectorConfiguration commonConfiguration = connectorConfiguration.getCommonConfiguration();
        ServiceMethodIdentifier targetServiceMethod = commonConfiguration.getTarget();
        TransportPayloadWriter setupPayloadWriter = commonConfiguration.getSetupPayloadWriter().get();
        RsocketSetupPayload.RsocketSetupPayloadBuilder payloadBuilder = RsocketSetupPayload.builder()
                .dataFormat(commonConfiguration.getDataFormat())
                .metadataFormat(commonConfiguration.getMetaDataFormat());
        if (nonNull(targetServiceMethod)) {
            payloadBuilder.serviceId(targetServiceMethod.getServiceId()).methodId(targetServiceMethod.getMethodId());
        }
        RsocketSetupPayload setupPayload = payloadBuilder.build();
        Payload payload = create(setupPayloadWriter.write(typed(declaration(RsocketSetupPayload.class).definition(), setupPayload)).nioBuffer());
        RSocketConnector connector = createConnector(commonConfiguration, payload);
        RsocketHttpClientGroupConfiguration groupConfiguration = connectorConfiguration.getGroupConfiguration();
        if (nonNull(groupConfiguration) && isNotEmpty(groupConfiguration.getClientConfigurations())) {
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
