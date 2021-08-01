package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.core.collection.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.model.CommunicatorActionIdentifier.*;
import java.util.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketCommunicatorConfigurator {
    private Map<Class<?>, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private Map<Class<?>, RsocketHttpConnectorConfiguration> httpConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<?> connectorClass) {
        return tcp(connectorClass, UnaryOperator.identity());
    }

    public RsocketCommunicatorConfigurator http(Class<?> connectorClass) {
        return http(connectorClass, UnaryOperator.identity());
    }

    public RsocketCommunicatorConfigurator tcp(Class<?> connectorClass, Function<RsocketTcpConnectorConfigurator, CommunicatorConfigurator> configurator) {
        return tcp(communicatorId(connectorClass), cast(configurator));
    }

    public RsocketCommunicatorConfigurator http(Class<?> connectorClass, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        return http(communicatorId(connectorClass), configurator);
    }

    private RsocketCommunicatorConfigurator tcp(Class<?> connector, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        tcpConnectors.put(connector, configurator.apply(new RsocketTcpConnectorConfigurator(connector)).configure());
        return this;
    }

    private RsocketCommunicatorConfigurator http(Class<?> connector, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
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
