package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.constants.*;
import io.rsocket.transport.netty.client.*;
import reactor.netty.http.client.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsConnectorConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class RsocketWsConnectorConfigurator {
    private final String connector;
    private final Set<RsocketWsClientConfiguration> clients;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator = identity();
    private RsocketModuleConstants.BalancerMethod balancer = ROUND_ROBIN;
    private UnaryOperator<HttpClient> clientDecorator = identity();
    private UnaryOperator<WebsocketClientTransport> transportDecorator = identity();

    public RsocketWsConnectorConfigurator(String connector) {
        this.connector = connector;
        this.clients = setOf(wsClientConfiguration(connector));
    }

    public RsocketWsConnectorConfigurator roundRobin() {
        balancer = ROUND_ROBIN;
        return this;
    }

    public RsocketWsConnectorConfigurator weighted() {
        balancer = WEIGHTED;
        return this;
    }

    public RsocketWsConnectorConfigurator decorateClient(UnaryOperator<HttpClient> decorator) {
        clientDecorator = decorator;
        return this;
    }

    public RsocketWsConnectorConfigurator decorateTransport(UnaryOperator<WebsocketClientTransport> decorator) {
        transportDecorator = decorator;
        return this;
    }

    public RsocketWsConnectorConfigurator client(UnaryOperator<RsocketWsClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(RsocketWsClientConfiguration.wsClientConfiguration(connector).toBuilder()).build());
        return this;
    }

    public RsocketWsConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketWsConnectorConfiguration configure() {
        return wsConnectorConfiguration(connector)
                .toBuilder()
                .commonConfiguration(commonConfigurator.apply(commonConnectorConfiguration(connector).toBuilder()).build())
                .balancer(balancer)
                .clientConfigurations(immutableSetOf(clients))
                .transportDecorator(transportDecorator)
                .clientDecorator(clientDecorator)
                .build();
    }
}
