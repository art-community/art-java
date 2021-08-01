package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.RsocketCommonServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import java.util.function.*;

public class RsocketTcpServerConfigurator {
    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpConfigurator = UnaryOperator.identity();
    private UnaryOperator<RsocketCommonServerConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketTcpServerConfigurator special(UnaryOperator<RsocketTcpServerConfigurationBuilder> configurator) {
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketTcpServerConfigurator common(UnaryOperator<RsocketCommonServerConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    UnaryOperator<RsocketTcpServerConfigurationBuilder> tcp() {
        return tcpConfigurator;
    }

    UnaryOperator<RsocketCommonServerConfigurationBuilder> common() {
        return commonConfigurator;
    }
}
