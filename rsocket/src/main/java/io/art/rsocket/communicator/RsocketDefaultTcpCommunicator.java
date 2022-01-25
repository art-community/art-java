package io.art.rsocket.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.meta.*;
import io.art.meta.model.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.meta.MetaRsocket.MetaIoPackage.MetaArtPackage.MetaRsocketPackage.MetaPortalPackage.MetaRsocketDefaultPortalClass.*;
import io.art.rsocket.portal.RsocketDefaultPortal.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.rsocket.core.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.client.*;
import reactor.core.publisher.*;
import reactor.netty.tcp.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.rsocket.communicator.RsocketCommunicationFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpConnectorConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.time.*;
import java.util.function.*;

@Public
public class RsocketDefaultTcpCommunicator implements RsocketDefaultCommunicator {
    private final static LazyProperty<MetaRsocketExecutionCommunicatorClass> communicatorClass = lazy(() -> Meta.declaration(RsocketBuiltinCommunicator.class));
    private final RsocketCommonConnectorConfigurationBuilder commonConnector = commonConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private RsocketTcpConnectorConfigurationBuilder tcpConnector = tcpConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();

    private ServiceMethodIdentifier serviceMethodId;

    private CommunicatorProxy<RsocketBuiltinCommunicator> proxy;

    public RsocketDefaultTcpCommunicator from(String connectorId) {
        return from(rsocketModule().configuration().getTcpConnectors().get(connectorId));
    }

    public RsocketDefaultTcpCommunicator from(RsocketTcpConnectorConfiguration from) {
        tcpConnector = from.toBuilder();
        return this;
    }

    public RsocketDefaultTcpCommunicator verbose(boolean value) {
        commonConnector.verbose(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator fragment(int value) {
        commonConnector.fragment(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator keepAlive(RsocketKeepAliveConfiguration value) {
        commonConnector.keepAlive(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator resume(RsocketResumeConfiguration value) {
        commonConnector.resume(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator retry(RsocketRetryConfiguration value) {
        commonConnector.retry(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator payloadDecoderMode(PayloadDecoderMode value) {
        commonConnector.payloadDecoderMode(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator maxInboundPayloadSize(int value) {
        commonConnector.maxInboundPayloadSize(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator timeout(Duration value) {
        commonConnector.timeout(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator target(String serviceId, String methodId) {
        serviceMethodId = serviceMethodId(serviceId, methodId);
        return this;
    }

    public RsocketDefaultTcpCommunicator target(Class<?> serviceIdMarker, String methodId) {
        serviceMethodId = serviceMethodId(idByDash(serviceIdMarker), methodId);
        return this;
    }

    public <T extends MetaClass<?>> RsocketDefaultTcpCommunicator target(Class<?> serviceIdMarker, Function<T, MetaMethod<?>> methodId) {
        serviceMethodId = serviceMethodId(idByDash(serviceIdMarker), methodId.apply(cast(Meta.declaration(serviceIdMarker))).name());
        return this;
    }

    public RsocketDefaultTcpCommunicator interceptors(UnaryOperator<InterceptorRegistry> interceptors) {
        commonConnector.interceptors(interceptors);
        return this;
    }

    public RsocketDefaultTcpCommunicator connectorDecorator(UnaryOperator<RSocketConnector> decorator) {
        commonConnector.decorator(decorator);
        return this;
    }

    public RsocketDefaultTcpCommunicator ssl(RsocketSslConfiguration ssl) {
        commonConnector.ssl(ssl);
        return this;
    }

    public RsocketDefaultTcpCommunicator balancer(BalancerMethod value) {
        tcpConnector.balancer(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator clientDecorator(UnaryOperator<TcpClient> clientDecorator) {
        tcpConnector.clientDecorator(clientDecorator);
        return this;
    }

    public RsocketDefaultTcpCommunicator transportDecorator(UnaryOperator<TcpClientTransport> transportDecorator) {
        tcpConnector.transportDecorator(transportDecorator);
        return this;
    }

    public RsocketDefaultTcpCommunicator client(String host, int port) {
        RsocketTcpClientConfiguration clientConfiguration = tcpClientConfiguration(DEFAULT_CONNECTOR_ID).toBuilder()
                .host(host)
                .port(port)
                .connector(DEFAULT_CONNECTOR_ID)
                .build();
        tcpConnector.clientConfigurations(immutableSetOf(addToSet(tcpConnector.build().getClientConfigurations().toMutable(), clientConfiguration)));
        return this;
    }


    public void fireAndForget() {
        RsocketTcpConnectorConfiguration configuration = tcpConnector
                .commonConfiguration(commonConnector.build())
                .build();
        createCommunicator(configuration).fireAndForget(Mono.empty());
    }

    public void fireAndForget(RsocketDefaultRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        createCommunicator(configuration).fireAndForget(Mono.just(request.getInput()));
    }

    public void fireAndForget(RsocketMonoRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        createCommunicator(configuration).fireAndForget(request.getInput());
    }


    public RsocketDefaultResponse requestResponse(DataFormat dataFormat) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(dataFormat);
        return new RsocketDefaultResponse(this, createCommunicator(configuration).requestResponse(Mono.empty()).flux(), dataFormat);
    }

    public RsocketDefaultResponse requestResponse(RsocketDefaultRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        return new RsocketDefaultResponse(this, createCommunicator(configuration).requestResponse(Mono.just(request.getInput())).flux(), request.getDataFormat());
    }

    public RsocketDefaultResponse requestResponse(RsocketMonoRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        return new RsocketDefaultResponse(this, createCommunicator(configuration).requestResponse(request.getInput()).flux(), request.getDataFormat());
    }


    public RsocketReactiveResponse requestStream(DataFormat dataFormat) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(dataFormat);
        return new RsocketReactiveResponse(this, createCommunicator(configuration).requestStream(Mono.empty()), dataFormat);
    }

    public RsocketReactiveResponse requestStream(RsocketDefaultRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        return new RsocketReactiveResponse(this, createCommunicator(configuration).requestStream(Mono.just(request.getInput())), request.getDataFormat());
    }

    public RsocketReactiveResponse requestStream(RsocketMonoRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        return new RsocketReactiveResponse(this, createCommunicator(configuration).requestStream(request.getInput()), request.getDataFormat());
    }


    public RsocketReactiveResponse requestChannel(DataFormat dataFormat) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(dataFormat);
        return new RsocketReactiveResponse(this, createCommunicator(configuration).requestChannel(Flux.empty()), dataFormat);
    }

    public RsocketReactiveResponse requestChannel(RsocketFluxRequest request) {
        RsocketTcpConnectorConfiguration configuration = createConfiguration(request.getDataFormat());
        return new RsocketReactiveResponse(this, createCommunicator(configuration).requestChannel(request.getInput()), request.getDataFormat());
    }


    @Override
    public void dispose() {
        apply(proxy, proxy -> proxy.getActions().values().forEach(CommunicatorAction::dispose));
    }

    private RsocketTcpConnectorConfiguration createConfiguration(DataFormat dataFormat) {
        return tcpConnector.commonConfiguration(commonConnector.dataFormat(dataFormat).build()).build();
    }

    private RsocketBuiltinCommunicator createCommunicator(RsocketTcpConnectorConfiguration connector) {
        return (proxy = communicatorProxy(communicatorClass.get(), () -> createCommunication(connector))).getCommunicator();
    }

    private RsocketCommunication createCommunication(RsocketTcpConnectorConfiguration connector) {
        return createDefaultTcpCommunication(connector, createSetupPayload(connector.getCommonConfiguration(), serviceMethodId));
    }
}
