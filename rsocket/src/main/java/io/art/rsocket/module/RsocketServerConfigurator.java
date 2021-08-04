package io.art.rsocket.module;

import io.art.core.property.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.configuration.server.RsocketHttpServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import io.art.server.configuration.*;
import io.art.server.configurator.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

public class RsocketServerConfigurator extends ServerConfigurator {
    private boolean tcp;
    private boolean http;
    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpConfigurator = identity();
    private UnaryOperator<RsocketHttpServerConfigurationBuilder> httpConfigurator = identity();

    public RsocketServerConfigurator tcp() {
        this.tcp = true;
        return this;
    }

    public RsocketServerConfigurator http() {
        this.http = true;
        return this;
    }

    public RsocketServerConfigurator tcp(UnaryOperator<RsocketTcpServerConfigurationBuilder> configurator) {
        this.tcp = true;
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketServerConfigurator http(UnaryOperator<RsocketHttpServerConfigurationBuilder> configurator) {
        this.http = true;
        this.httpConfigurator = configurator;
        return this;
    }

    RsocketTcpServerConfiguration configure(RsocketTcpServerConfiguration current) {
        return tcpConfigurator.apply(current.toBuilder()).build();
    }

    RsocketHttpServerConfiguration configure(RsocketHttpServerConfiguration current) {
        return httpConfigurator.apply(current.toBuilder()).build();
    }

    boolean enableTcp() {
        return tcp;
    }

    boolean enableHttp() {
        return http;
    }

    ServerConfiguration configureServer(LazyProperty<ServerConfiguration> provider, ServerConfiguration current) {
        return configure(provider, current);
    }
}
