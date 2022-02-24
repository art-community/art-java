package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.server.*;
import io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketWsServerConfiguration.*;
import io.art.server.configurator.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Public
public class RsocketServerConfigurator extends ServerConfigurator<RsocketServerConfigurator> {
    private boolean tcp;
    private boolean ws;
    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpConfigurator = identity();
    private UnaryOperator<RsocketWsServerConfigurationBuilder> wsConfigurator = identity();

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

    RsocketTcpServerConfiguration configureTcp(RsocketTcpServerConfiguration current) {
        return tcpConfigurator.apply(current.toBuilder()).build();
    }

    RsocketWsServerConfiguration configureWs(RsocketWsServerConfiguration current) {
        return wsConfigurator.apply(current.toBuilder()).build();
    }

    boolean enableTcp() {
        return tcp;
    }

    boolean enableWs() {
        return ws;
    }
}
