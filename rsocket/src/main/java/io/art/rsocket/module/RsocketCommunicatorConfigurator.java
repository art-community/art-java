package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.communicator.model.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.*;
import io.art.rsocket.configuration.communicator.http.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.communicator.factory.ConnectorProxyFactory.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.normalizer.ClassIdentifierNormalizer.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.meta.module.MetaModule.*;
import java.util.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketCommunicatorConfigurator {
    private Map<Class<? extends Connector>, LazyProperty<Connector>> connectors = map();
    private Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private Map<String, RsocketHttpConnectorConfiguration> httpConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<? extends Connector> connectorClass) {
        return tcp(connectorClass, cast(Function.identity()));
    }

    public RsocketCommunicatorConfigurator http(Class<? extends Connector> connectorClass) {
        return http(connectorClass, UnaryOperator.identity());
    }

    private RsocketCommunicatorConfigurator tcp(Class<? extends Connector> connector, Function<RsocketTcpConnectorConfigurator, CommunicatorConfigurator> configurator) {
        RsocketTcpConnectorConfigurator connectorConfigurator = cast(configurator.apply(new RsocketTcpConnectorConfigurator(connector)));
        connectors.put(connector, lazy(() -> createConnectorProxy(declaration(connector), Rsocket::rsocketCommunicator)));
        tcpConnectors.put(normalizeToId(connector), connectorConfigurator.configure());
        return this;
    }

    private RsocketCommunicatorConfigurator http(Class<? extends Connector> connector, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        connectors.put(connector, lazy(() -> createConnectorProxy(declaration(connector), Rsocket::rsocketCommunicator)));
        httpConnectors.put(normalizeToId(connector), configurator.apply(new RsocketHttpConnectorConfigurator(normalizeToId(connector))).configure());
        return this;
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> configureTcp() {
        return immutableMapOf(tcpConnectors);
    }

    ImmutableMap<String, RsocketHttpConnectorConfiguration> configureHttp() {
        return immutableMapOf(httpConnectors);
    }
}
