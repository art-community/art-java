package io.art.rsocket.module;

import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.RsocketHttpClientConfiguration.*;
import lombok.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketHttpConnectorConfigurator {
    private final String connector;
    private RsocketHttpClientGroupConfiguration group;
    private RsocketHttpClientConfiguration single;

    public RsocketHttpConnectorConfigurator roundRobin(UnaryOperator<RsocketHttpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketHttpClientGroupConfigurator(connector, ROUND_ROBIN)).configure();
        return this;
    }

    public RsocketHttpConnectorConfigurator weighted(UnaryOperator<RsocketHttpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketHttpClientGroupConfigurator(connector, WEIGHTED)).configure();
        return this;
    }

    public RsocketHttpConnectorConfigurator single(UnaryOperator<RsocketHttpClientConfigurationBuilder> configurator) {
        single = configurator.apply(RsocketHttpClientConfiguration.builder().connector(connector)).build();
        return this;
    }

    RsocketHttpConnectorConfiguration configure() {
        return RsocketHttpConnectorConfiguration.builder()
                .connector(connector)
                .groupConfiguration(group)
                .singleConfiguration(single)
                .build();
    }
}
