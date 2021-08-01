package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.RsocketHttpConnectorConfiguration.*;
import io.art.rsocket.configuration.RsocketTcpConnectorConfiguration.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
public class RsocketCommunicatorConfigurator extends CommunicatorConfigurator {
    private Map<String, UnaryOperator<RsocketTcpConnectorConfigurationBuilder>> tcpConnectors = map();
    private Map<String, UnaryOperator<RsocketHttpConnectorConfigurationBuilder>> httpConnectors = map();

    public RsocketCommunicatorConfigurator tcp(Class<?> proxyClass, UnaryOperator<RsocketTcpConnectorConfigurationBuilder> configurator) {
        return tcp(proxyClass.getSimpleName(), configurator);
    }

    public RsocketCommunicatorConfigurator http(Class<?> proxyClass, UnaryOperator<RsocketHttpConnectorConfigurationBuilder> configurator) {
        return http(proxyClass.getSimpleName(), configurator);
    }

    public RsocketCommunicatorConfigurator tcp(String connector, UnaryOperator<RsocketTcpConnectorConfigurationBuilder> configurator) {
        this.tcpConnectors.put(connector, configurator);
        return this;
    }

    public RsocketCommunicatorConfigurator http(String connector, UnaryOperator<RsocketHttpConnectorConfigurationBuilder> configurator) {
        this.httpConnectors.put(connector, configurator);
        return this;
    }

    RsocketCommunicatorConfigurator() {
        super(() -> rsocketModule().configuration().getCommunicatorConfiguration(), RsocketCommunication::new);
    }

    ImmutableMap<String, RsocketTcpConnectorConfiguration> configureTcp() {
        return tcpConnectors.entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().apply(RsocketTcpConnectorConfiguration.builder().id(entry.getKey())).build()));
    }


    ImmutableMap<String, RsocketHttpConnectorConfiguration> configureHttp() {
        return httpConnectors.entrySet()
                .stream()
                .collect(immutableMapCollector(Map.Entry::getKey, entry -> entry.getValue().apply(RsocketHttpConnectorConfiguration.builder().id(entry.getKey())).build()));
    }

    LazyProperty<ImmutableMap<Class<?>, Object>> communicatorProxies() {
        return get();
    }
}
