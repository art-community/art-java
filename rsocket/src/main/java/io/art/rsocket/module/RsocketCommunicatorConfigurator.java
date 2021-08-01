package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.configuration.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketCommunicatorConfigurator extends CommunicatorConfigurator {
    private Map<String, RsocketTcpConnectorConfiguration> tcpConnectors = map();
    private Map<String, RsocketHttpConnectorConfiguration> httpConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<?> proxyClass, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        return tcp(proxyClass.getSimpleName(), configurator);
    }

    public RsocketCommunicatorConfigurator http(Class<?> proxyClass, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        return http(proxyClass.getSimpleName(), configurator);
    }

    public RsocketCommunicatorConfigurator tcp(String connector, UnaryOperator<RsocketTcpConnectorConfigurator> configurator) {
        tcpConnectors.put(connector, configurator.apply(new RsocketTcpConnectorConfigurator(connector)).configure());
        return this;
    }

    public RsocketCommunicatorConfigurator http(String connector, UnaryOperator<RsocketHttpConnectorConfigurator> configurator) {
        httpConnectors.put(connector, configurator.apply(new RsocketHttpConnectorConfigurator(connector)).configure());
        return this;
    }

    RsocketCommunicatorConfigurator() {
        super(() -> rsocketModule().configuration().getCommunicatorConfiguration(), RsocketCommunication::new);
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> configureTcp() {
        return immutableMapOf(tcpConnectors);
    }


    ImmutableMap<String, RsocketHttpConnectorConfiguration> configureHttp() {
        return immutableMapOf(httpConnectors);
    }

    LazyProperty<ImmutableMap<Class<?>, Object>> communicatorProxies() {
        return get();
    }
}
