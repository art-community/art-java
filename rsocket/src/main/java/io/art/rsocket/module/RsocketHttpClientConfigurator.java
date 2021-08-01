package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.RsocketCommonClientConfiguration.*;
import io.art.rsocket.configuration.communicator.RsocketHttpClientConfiguration.*;
import java.util.function.*;

public class RsocketHttpClientConfigurator {
    private UnaryOperator<RsocketHttpClientConfigurationBuilder> httpConfigurator = UnaryOperator.identity();
    private UnaryOperator<RsocketCommonClientConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketHttpClientConfigurator special(UnaryOperator<RsocketHttpClientConfigurationBuilder> configurator) {
        this.httpConfigurator = configurator;
        return this;
    }

    public RsocketHttpClientConfigurator common(UnaryOperator<RsocketCommonClientConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    UnaryOperator<RsocketHttpClientConfigurationBuilder> http() {
        return httpConfigurator;
    }

    UnaryOperator<RsocketCommonClientConfigurationBuilder> common() {
        return commonConfigurator;
    }
}
