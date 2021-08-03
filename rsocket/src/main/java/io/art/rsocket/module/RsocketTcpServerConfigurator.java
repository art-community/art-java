package io.art.rsocket.module;

import io.art.rsocket.configuration.server.RsocketCommonServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketTcpServerConfiguration.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

public class RsocketTcpServerConfigurator {
    private UnaryOperator<RsocketTcpServerConfigurationBuilder> tcpConfigurator = identity();
    private UnaryOperator<RsocketCommonServerConfigurationBuilder> commonConfigurator = identity();

    public RsocketTcpServerConfigurator special(UnaryOperator<RsocketTcpServerConfigurationBuilder> configurator) {
        this.tcpConfigurator = configurator;
        return this;
    }

    public RsocketTcpServerConfigurator common(UnaryOperator<RsocketCommonServerConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketTcpServerConfigurationBuilder tcp(RsocketTcpServerConfigurationBuilder builder) {
        return tcpConfigurator.apply(builder);
    }

    RsocketCommonServerConfigurationBuilder common(RsocketCommonServerConfigurationBuilder builder) {
        return commonConfigurator.apply(builder);
    }
}
