package io.art.rsocket.module;

import io.art.rsocket.configuration.server.RsocketCommonServerConfiguration.*;
import io.art.rsocket.configuration.server.RsocketHttpServerConfiguration.*;
import lombok.*;
import lombok.experimental.*;
import java.util.function.*;

@Setter
@Accessors(fluent = true)
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

    UnaryOperator<RsocketHttpServerConfigurationBuilder> http() {
        return httpConfigurator;
    }

    UnaryOperator<RsocketCommonServerConfigurationBuilder> common() {
        return commonConfigurator;
    }
}
