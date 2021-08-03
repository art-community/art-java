package io.art.rsocket.module;

import io.art.rsocket.configuration.server.RsocketCommonServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketHttpServerConfiguration.*;
import java.util.function.*;

public class RsocketHttpServerConfigurator {
    private UnaryOperator<RsocketHttpServerConfigurationBuilder> httpConfigurator = UnaryOperator.identity();
    private UnaryOperator<RsocketCommonServerConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketHttpServerConfigurator special(UnaryOperator<RsocketHttpServerConfigurationBuilder> configurator) {
        this.httpConfigurator = configurator;
        return this;
    }

    public RsocketHttpServerConfigurator common(UnaryOperator<RsocketCommonServerConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketHttpServerConfigurationBuilder http(RsocketHttpServerConfigurationBuilder builder) {
        return httpConfigurator.apply(builder);
    }

    RsocketCommonServerConfigurationBuilder common(RsocketCommonServerConfigurationBuilder builder) {
        return commonConfigurator.apply(builder);
    }
}
