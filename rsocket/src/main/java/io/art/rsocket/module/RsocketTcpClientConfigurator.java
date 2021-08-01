package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.common.RsocketCommonClientConfiguration.*;
import io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import java.util.function.*;

public class RsocketTcpClientConfigurator {
    private UnaryOperator<RsocketTcpClientConfigurationBuilder> tcpConfigurator = UnaryOperator.identity();
    private UnaryOperator<RsocketCommonClientConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketTcpClientConfigurator special(UnaryOperator<RsocketTcpClientConfigurationBuilder> configurator) {
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketTcpClientConfigurator common(UnaryOperator<RsocketCommonClientConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    UnaryOperator<RsocketTcpClientConfigurationBuilder> tcp() {
        return tcpConfigurator;
    }

    UnaryOperator<RsocketCommonClientConfigurationBuilder> common() {
        return commonConfigurator;
    }
}
