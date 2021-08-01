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
    private Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private Map<String, RsocketHttpConnectorConfiguration> httpConnectors = map();

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
