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
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.meta.MetaRsocket.MetaIoPackage.MetaArtPackage.MetaRsocketPackage.MetaCommunicatorPackage.*;
import io.art.transport.constants.TransportModuleConstants.*;
import io.rsocket.core.*;
import io.rsocket.plugins.*;
import io.rsocket.transport.netty.client.*;
import reactor.core.publisher.*;
import reactor.netty.http.client.*;
import static io.art.communicator.factory.CommunicatorProxyFactory.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.core.model.ServiceMethodIdentifier.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.core.property.Property.*;
import static io.art.rsocket.communicator.RsocketCommunicationFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsConnectorConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Defaults.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.Objects.*;
import java.time.*;
import java.util.function.*;

@Public
public class RsocketDefaultWsCommunicator implements RsocketDefaultCommunicator {
    private final static LazyProperty<MetaRsocketBuiltinCommunicatorClass> communicatorClass = lazy(() -> Meta.declaration(RsocketBuiltinCommunicator.class));
    private final RsocketCommonConnectorConfigurationBuilder commonConnector = commonConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private final Property<CommunicatorProxy<RsocketBuiltinCommunicator>> proxy = property(this::createCommunicator);
    private RsocketWsConnectorConfiguration currentWsConnector;
    private RsocketWsConnectorConfigurationBuilder wsConnectorBuilder = wsConnectorConfiguration(DEFAULT_CONNECTOR_ID).toBuilder();
    private ServiceMethodIdentifier serviceMethodId;

    public RsocketDefaultWsCommunicator from(String connectorId) {
        return from(rsocketModule().configuration().getWsConnectors().get(connectorId));
    }

    public RsocketDefaultWsCommunicator from(RsocketWsConnectorConfiguration from) {
        refreshCommunicator();
        wsConnectorBuilder = from.toBuilder();
        return this;
    }

    public RsocketDefaultWsCommunicator verbose(boolean value) {
        refreshCommunicator();
        commonConnector.verbose(value);
        return this;
    }

    public RsocketDefaultWsCommunicator fragment(int value) {
        refreshCommunicator();
        commonConnector.fragment(value);
        return this;
    }

    public RsocketDefaultWsCommunicator keepAlive(RsocketKeepAliveConfiguration value) {
        refreshCommunicator();
        commonConnector.keepAlive(value);
        return this;
    }

    public RsocketDefaultWsCommunicator resume(RsocketResumeConfiguration value) {
        refreshCommunicator();
        commonConnector.resume(value);
        return this;
    }

    public RsocketDefaultWsCommunicator retry(RsocketRetryConfiguration value) {
        refreshCommunicator();
        commonConnector.retry(value);
        return this;
    }

    public RsocketDefaultWsCommunicator payloadDecoderMode(PayloadDecoderMode value) {
        refreshCommunicator();
        commonConnector.payloadDecoderMode(value);
        return this;
    }

    public RsocketDefaultWsCommunicator maxInboundPayloadSize(int value) {
        refreshCommunicator();
        commonConnector.maxInboundPayloadSize(value);
        return this;
    }

    public RsocketDefaultWsCommunicator service(Class<?> value) {
        refreshCommunicator();
        commonConnector.service(ServiceMethodStrategy.manual(value));
        return this;
    }

    public RsocketDefaultWsCommunicator timeout(Duration value) {
        refreshCommunicator();
        commonConnector.timeout(value);
        return this;
    }

    public RsocketDefaultWsCommunicator target(String serviceId, String methodId) {
        refreshCommunicator();
        serviceMethodId = serviceMethodId(serviceId, methodId);
        return this;
    }

    public RsocketDefaultWsCommunicator target(Class<?> serviceIdMarker, String methodId) {
        refreshCommunicator();
        serviceMethodId = serviceMethodId(idByDash(serviceIdMarker), methodId);
        return this;
    }

    public RsocketDefaultWsCommunicator target(Class<?> serviceIdMarker, MetaMethod<? extends MetaClass<?>, ?> methodId) {
        refreshCommunicator();
        serviceMethodId = serviceMethodId(idByDash(serviceIdMarker), methodId.name());
        return this;
    }

    public RsocketDefaultWsCommunicator interceptors(UnaryOperator<InterceptorRegistry> interceptors) {
        refreshCommunicator();
        commonConnector.interceptors(interceptors);
        return this;
    }

    public RsocketDefaultWsCommunicator connectorDecorator(UnaryOperator<RSocketConnector> decorator) {
        refreshCommunicator();
        commonConnector.decorator(decorator);
        return this;
    }

    public RsocketDefaultWsCommunicator ssl(RsocketSslConfiguration ssl) {
        refreshCommunicator();
        commonConnector.ssl(ssl);
        return this;
    }

    public RsocketDefaultWsCommunicator balancer(BalancerMethod value) {
        refreshCommunicator();
        wsConnectorBuilder.balancer(value);
        return this;
    }

    public RsocketDefaultWsCommunicator clientDecorator(UnaryOperator<HttpClient> clientDecorator) {
        refreshCommunicator();
        wsConnectorBuilder.clientDecorator(clientDecorator);
        return this;
    }

    public RsocketDefaultWsCommunicator transportDecorator(UnaryOperator<WebsocketClientTransport> transportDecorator) {
        refreshCommunicator();
        wsConnectorBuilder.transportDecorator(transportDecorator);
        return this;
    }

    public RsocketDefaultWsCommunicator dataFormat(DataFormat dataFormat) {
        refreshCommunicator();
        commonConnector.dataFormat(dataFormat);
        return this;
    }

    public RsocketDefaultWsCommunicator client(String host, int port, String path) {
        refreshCommunicator();
        RsocketWsClientConfiguration clientConfiguration = wsClientConfiguration(DEFAULT_CONNECTOR_ID).toBuilder()
                .host(host)
                .port(port)
                .path(path)
                .connector(DEFAULT_CONNECTOR_ID)
                .build();
        ImmutableSet<RsocketWsClientConfiguration> clients = immutableSetOf(addToSet(wsConnectorBuilder.build().getClientConfigurations().toMutable(), clientConfiguration));
        wsConnectorBuilder.clientConfigurations(clients);
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
        DataFormat dataFormat = currentWsConnector.getCommonConfiguration().getDataFormat();
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
        DataFormat dataFormat = currentWsConnector.getCommonConfiguration().getDataFormat();
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
        DataFormat dataFormat = currentWsConnector.getCommonConfiguration().getDataFormat();
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
        return createDefaultWsCommunication(currentWsConnector, createSetupPayload(currentWsConnector.getCommonConfiguration(), serviceMethodId));
    }

    private void refreshCommunicator() {
        if (proxy.initialized()) {
            dispose();
            proxy.dispose();
        }
    }

    private void prepareCommunicator(DataFormat dataFormat) {
        if (isNull(currentWsConnector)) {
            currentWsConnector = wsConnectorBuilder.commonConfiguration(commonConnector.dataFormat(dataFormat).build()).build();
            return;
        }

        if (proxy.initialized() && currentWsConnector.getCommonConfiguration().getDataFormat() != dataFormat) {
            currentWsConnector = wsConnectorBuilder.commonConfiguration(commonConnector.dataFormat(dataFormat).build()).build();
            dispose();
            proxy.dispose();
        }
    }

    private void prepareCommunicator() {
        if (isNull(currentWsConnector)) {
            RsocketCommonConnectorConfiguration commonConfiguration = commonConnector.build();
            currentWsConnector = wsConnectorBuilder.commonConfiguration(commonConfiguration).build();
            return;
        }

        prepareCommunicator(currentWsConnector.getCommonConfiguration().getDataFormat());
    }
}
