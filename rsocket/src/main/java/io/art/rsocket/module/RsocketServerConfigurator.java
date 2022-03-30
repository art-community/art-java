package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketWsServerConfiguration.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.rsocket.module.RsocketModule.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Public
public class RsocketServerConfigurator {
    private boolean tcp;
    private boolean ws;
    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpConfigurator = identity();
    private UnaryOperator<RsocketWsServerConfigurationBuilder> wsConfigurator = identity();
    private final ServerConfiguratorImplementation delegate = new ServerConfiguratorImplementation();

    public RsocketServerConfigurator tcp(Class<?> serviceClass) {
        return tcp().configure(configurator -> configurator.service(serviceClass));
    }

    public RsocketServerConfigurator ws(Class<?> serviceClass) {
        return ws().configure(configurator -> configurator.service(serviceClass));
    }

    public RsocketServerConfigurator tcp() {
        this.tcp = true;
        return this;
    }

    public RsocketServerConfigurator ws() {
        this.ws = true;
        return this;
    }

    public RsocketServerConfigurator tcp(UnaryOperator<RsocketTcpServerConfigurationBuilder> configurator) {
        this.tcp = true;
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketServerConfigurator ws(UnaryOperator<RsocketWsServerConfigurationBuilder> configurator) {
        this.ws = true;
        this.wsConfigurator = configurator;
        return this;
    }

    public RsocketServerConfigurator configure(UnaryOperator<ServerConfigurator> configurator) {
        configurator.apply(delegate);
        return this;
    }

    RsocketTcpServerConfiguration createTcpConfiguration(RsocketTcpServerConfiguration current) {
        return tcpConfigurator.apply(current.toBuilder()).build();
    }

    RsocketWsServerConfiguration createWsConfiguration(RsocketWsServerConfiguration current) {
        return wsConfigurator.apply(current.toBuilder()).build();
    }

    ServerConfiguration createServerConfiguration(ServerConfiguration current) {
        return delegate.createConfiguration(lazy(() -> rsocketModule().configuration().getServer()), current);
    }

    boolean isTcpEnabled() {
        return tcp;
    }

    boolean isWsEnabled() {
        return ws;
    }
}
