package io.art.rsocket.communicator;

import io.art.communicator.action.*;
import io.art.communicator.model.*;
import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.core.strategy.*;
import io.art.meta.*;
import io.art.meta.model.*;
import io.art.rsocket.configuration.common.*;
import io.art.rsocket.configuration.communicator.common.*;
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
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.property.Property.*;
import static io.art.rsocket.communicator.RsocketCommunicationFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpConnectorConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.Objects.*;
import java.time.*;
import java.util.function.*;

@Public
public class RsocketDefaultTcpCommunicator implements RsocketDefaultCommunicator {
    private final static LazyProperty<MetaRsocketExecutionCommunicatorClass> communicatorClass = lazy(() -> Meta.declaration(RsocketBuiltinCommunicator.class));
    private final RsocketCommonConnectorConfigurationBuilder commonConnector = commonConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private final Property<CommunicatorProxy<RsocketBuiltinCommunicator>> proxy = property(this::createCommunicator);
    private RsocketTcpConnectorConfiguration currentTcpConnector;
    private RsocketTcpConnectorConfigurationBuilder tcpConnectorBuilder = tcpConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private ServiceMethodIdentifier serviceMethodId;

    public RsocketDefaultTcpCommunicator from(String connectorId) {
        return from(rsocketModule().configuration().getTcpConnectors().get(connectorId));
    }

    public RsocketDefaultTcpCommunicator from(RsocketTcpConnectorConfiguration from) {
        refreshCommunicator();
        tcpConnectorBuilder = from.toBuilder();
        return this;
    }

    public RsocketDefaultTcpCommunicator verbose(boolean value) {
        refreshCommunicator();
        commonConnector.verbose(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator fragment(int value) {
        refreshCommunicator();
        commonConnector.fragment(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator keepAlive(RsocketKeepAliveConfiguration value) {
        refreshCommunicator();
        commonConnector.keepAlive(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator resume(RsocketResumeConfiguration value) {
        refreshCommunicator();
        commonConnector.resume(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator retry(RsocketRetryConfiguration value) {
        refreshCommunicator();
        commonConnector.retry(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator payloadDecoderMode(PayloadDecoderMode value) {
        refreshCommunicator();
        commonConnector.payloadDecoderMode(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator maxInboundPayloadSize(int value) {
        refreshCommunicator();
        commonConnector.maxInboundPayloadSize(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator service(Class<?> value) {
        refreshCommunicator();
        commonConnector.service(ServiceMethodStrategy.manual(value));
        return this;
    }

    public RsocketDefaultTcpCommunicator timeout(Duration value) {
        refreshCommunicator();
        commonConnector.timeout(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator target(String serviceId, String methodId) {
        refreshCommunicator();
        serviceMethodId = serviceMethodId(serviceId, methodId);
        return this;
    }

    public RsocketDefaultTcpCommunicator target(Class<?> serviceIdMarker, String methodId) {
        refreshCommunicator();
        serviceMethodId = serviceMethodId(idByDash(serviceIdMarker), methodId);
        return this;
    }

    public <T extends MetaClass<?>> RsocketDefaultTcpCommunicator target(Class<?> serviceIdMarker, Function<T, MetaMethod<?>> methodId) {
        refreshCommunicator();
        serviceMethodId = serviceMethodId(idByDash(serviceIdMarker), methodId.apply(cast(Meta.declaration(serviceIdMarker))).name());
        return this;
    }

    public RsocketDefaultTcpCommunicator interceptors(UnaryOperator<InterceptorRegistry> interceptors) {
        refreshCommunicator();
        commonConnector.interceptors(interceptors);
        return this;
    }

    public RsocketDefaultTcpCommunicator connectorDecorator(UnaryOperator<RSocketConnector> decorator) {
        refreshCommunicator();
        commonConnector.decorator(decorator);
        return this;
    }

    public RsocketDefaultTcpCommunicator ssl(RsocketSslConfiguration ssl) {
        refreshCommunicator();
        commonConnector.ssl(ssl);
        return this;
    }

    public RsocketDefaultTcpCommunicator balancer(BalancerMethod value) {
        refreshCommunicator();
        tcpConnectorBuilder.balancer(value);
        return this;
    }

    public RsocketDefaultTcpCommunicator clientDecorator(UnaryOperator<TcpClient> clientDecorator) {
        refreshCommunicator();
        tcpConnectorBuilder.clientDecorator(clientDecorator);
        return this;
    }

    public RsocketDefaultTcpCommunicator transportDecorator(UnaryOperator<TcpClientTransport> transportDecorator) {
        refreshCommunicator();
        tcpConnectorBuilder.transportDecorator(transportDecorator);
        return this;
    }

    public RsocketDefaultTcpCommunicator dataFormat(DataFormat dataFormat) {
        refreshCommunicator();
        commonConnector.dataFormat(dataFormat);
        return this;
    }

    public RsocketDefaultTcpCommunicator client(String host, int port) {
        refreshCommunicator();
        RsocketTcpClientConfiguration clientConfiguration = tcpClientConfiguration(DEFAULT_CONNECTOR_ID).toBuilder()
                .host(host)
                .port(port)
                .connector(DEFAULT_CONNECTOR_ID)
                .build();
        ImmutableSet<RsocketTcpClientConfiguration> clients = immutableSetOf(addToSet(tcpConnectorBuilder.build().getClientConfigurations().toMutable(), clientConfiguration));
        tcpConnectorBuilder.clientConfigurations(clients);
        return this;
    }


    public void fireAndForget() {
        prepareCommunicator();
        proxy.get().getCommunicator().fireAndForget(Mono.empty());
    }

    public void fireAndForget(RsocketDefaultRequest request) {
        prepareCommunicator(request.getDataFormat());
        proxy.get().getCommunicator().fireAndForget(Mono.just(request.getInput()));
    }

    public void fireAndForget(RsocketMonoRequest request) {
        prepareCommunicator(request.getDataFormat());
        proxy.get().getCommunicator().fireAndForget(request.getInput());
    }


    public RsocketDefaultResponse requestResponse() {
        prepareCommunicator();
        DataFormat dataFormat = currentTcpConnector.getCommonConfiguration().getDataFormat();
        return new RsocketDefaultResponse(this, proxy.get().getCommunicator().requestResponse(Mono.empty()).flux(), dataFormat);
    }

    public RsocketDefaultResponse requestResponse(RsocketDefaultRequest request) {
        prepareCommunicator(request.getDataFormat());
        return new RsocketDefaultResponse(this, proxy.get().getCommunicator().requestResponse(Mono.just(request.getInput())).flux(), request.getDataFormat());
    }

    public RsocketDefaultResponse requestResponse(RsocketMonoRequest request) {
        prepareCommunicator(request.getDataFormat());
        return new RsocketDefaultResponse(this, proxy.get().getCommunicator().requestResponse(request.getInput()).flux(), request.getDataFormat());
    }


    public RsocketReactiveResponse requestStream() {
        prepareCommunicator();
        DataFormat dataFormat = currentTcpConnector.getCommonConfiguration().getDataFormat();
        return new RsocketReactiveResponse(this, proxy.get().getCommunicator().requestStream(Mono.empty()), dataFormat);
    }

    public RsocketReactiveResponse requestStream(RsocketDefaultRequest request) {
        prepareCommunicator(request.getDataFormat());
        return new RsocketReactiveResponse(this, proxy.get().getCommunicator().requestStream(Mono.just(request.getInput())), request.getDataFormat());
    }

    public RsocketReactiveResponse requestStream(RsocketMonoRequest request) {
        prepareCommunicator(request.getDataFormat());
        return new RsocketReactiveResponse(this, proxy.get().getCommunicator().requestStream(request.getInput()), request.getDataFormat());
    }


    public RsocketReactiveResponse requestChannel() {
        prepareCommunicator();
        DataFormat dataFormat = currentTcpConnector.getCommonConfiguration().getDataFormat();
        Flux<byte[]> output = proxy.get().getCommunicator().requestChannel(Flux.empty());
        return new RsocketReactiveResponse(this, output, dataFormat);
    }

    public RsocketReactiveResponse requestChannel(RsocketFluxRequest request) {
        prepareCommunicator(request.getDataFormat());
        Flux<byte[]> output = proxy.get().getCommunicator().requestChannel(request.getInput());
        return new RsocketReactiveResponse(this, output, request.getDataFormat());
    }

    @Override
    public void dispose() {
        if (proxy.initialized()) {
            proxy.get().getActions().values().forEach(CommunicatorAction::dispose);
        }
    }

    private CommunicatorProxy<RsocketBuiltinCommunicator> createCommunicator() {
        return communicatorProxy(communicatorClass.get(), this::createCommunication);
    }

    private RsocketCommunication createCommunication() {
        return createDefaultTcpCommunication(currentTcpConnector, createSetupPayload(currentTcpConnector.getCommonConfiguration(), serviceMethodId));
    }

    private void refreshCommunicator() {
        if (proxy.initialized()) {
            dispose();
            proxy.dispose();
        }
    }

    private void prepareCommunicator(DataFormat dataFormat) {
        if (isNull(currentTcpConnector)) {
            currentTcpConnector = tcpConnectorBuilder.commonConfiguration(commonConnector.dataFormat(dataFormat).build()).build();
            return;
        }

        if (proxy.initialized() && currentTcpConnector.getCommonConfiguration().getDataFormat() != dataFormat) {
            currentTcpConnector = tcpConnectorBuilder.commonConfiguration(commonConnector.dataFormat(dataFormat).build()).build();
            dispose();
            proxy.dispose();
        }
    }

    private void prepareCommunicator() {
        if (isNull(currentTcpConnector)) {
            RsocketCommonConnectorConfiguration commonConfiguration = commonConnector.build();
            currentTcpConnector = tcpConnectorBuilder.commonConfiguration(commonConfiguration).build();
            return;
        }

        prepareCommunicator(currentTcpConnector.getCommonConfiguration().getDataFormat());
    }
}
