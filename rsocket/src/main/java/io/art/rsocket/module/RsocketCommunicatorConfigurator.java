package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.communicator.proxy.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.model.CommunicatorActionIdentifier.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketCommunicatorConfigurator  {
    private Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private Map<String, RsocketHttpConnectorConfiguration> httpConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<?> proxyClass) {
        return tcp(communicatorId(proxyClass));
    }

    public RsocketCommunicatorConfigurator http(Class<?> proxyClass) {
        return http(communicatorId(proxyClass));
    }

    public RsocketCommunicatorConfigurator tcp(String connector) {
        return tcp(connector, identity());
    }

    public RsocketCommunicatorConfigurator http(String connector) {
        return http(connector, identity());
    }

    public RsocketCommunicatorConfigurator tcp(Class<?> proxyClass, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        return tcp(communicatorId(proxyClass), configurator);
    }

    public RsocketCommunicatorConfigurator http(Class<?> proxyClass, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        return http(communicatorId(proxyClass), configurator);
    }

    public RsocketCommunicatorConfigurator tcp(String connector, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        tcpConnectors.put(connector, configurator.apply(new RsocketTcpConnectorConfigurator(connector)).configure());
        return this;
    }

    public RsocketCommunicatorConfigurator http(String connector, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        httpConnectors.put(connector, configurator.apply(new RsocketHttpConnectorConfigurator(connector)).configure());
        return this;
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> configureTcp() {
        return immutableMapOf(tcpConnectors);
    }

    ImmutableMap<String, RsocketHttpConnectorConfiguration> configureHttp() {
        return immutableMapOf(httpConnectors);
    }
}
