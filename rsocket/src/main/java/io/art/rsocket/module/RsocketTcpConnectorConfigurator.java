package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.constants.*;
import io.rsocket.transport.netty.client.*;
import reactor.netty.tcp.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpConnectorConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Public
public class RsocketTcpConnectorConfigurator {
    private final String connector;
    private final Set<RsocketTcpClientConfiguration> clients;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator;
    private RsocketModuleConstants.BalancerMethod balancer;
    private UnaryOperator<TcpClient> clientDecorator;
    private UnaryOperator<TcpClientTransport> transportDecorator;

    public RsocketTcpConnectorConfigurator(String connector) {
        this.connector = connector;
        clients = setOf(tcpClientConfiguration(connector));
        commonConfigurator = identity();
        balancer = ROUND_ROBIN;
        clientDecorator = identity();
        transportDecorator = identity();
    }

    public RsocketTcpConnectorConfigurator roundRobin() {
        balancer = ROUND_ROBIN;
        return this;
    }

    public RsocketTcpConnectorConfigurator weighted() {
        balancer = WEIGHTED;
        return this;
    }

    public RsocketTcpConnectorConfigurator decorateClient(UnaryOperator<TcpClient> decorator) {
        clientDecorator = decorator;
        return this;
    }

    public RsocketTcpConnectorConfigurator decorateTransport(UnaryOperator<TcpClientTransport> decorator) {
        transportDecorator = decorator;
        return this;
    }

    public RsocketTcpConnectorConfigurator client(UnaryOperator<RsocketTcpClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(tcpClientConfiguration(connector).toBuilder()).build());
        return this;
    }

    public RsocketTcpConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketTcpConnectorConfiguration configure() {
        return tcpConnectorConfiguration(connector)
                .toBuilder()
                .commonConfiguration(commonConfigurator.apply(commonConnectorConfiguration(connector).toBuilder()).build())
                .balancer(balancer)
                .clientConfigurations(immutableSetOf(clients))
                .transportDecorator(transportDecorator)
                .clientDecorator(clientDecorator)
                .build();
    }
}
