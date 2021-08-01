package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.*;
import io.art.rsocket.configuration.communicator.RsocketHttpClientConfiguration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketHttpClientGroupConfigurator {
    private final String connector;
    private final BalancerMethod balancer;
    private final Set<RsocketHttpClientConfiguration> clients = set();

    public RsocketHttpClientGroupConfigurator client(UnaryOperator<RsocketHttpClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(RsocketHttpClientConfiguration.builder().connector(connector)).build());
        return this;
    }

    RsocketHttpClientGroupConfiguration configure() {
        return RsocketHttpClientGroupConfiguration.builder()
                .connector(connector)
                .balancer(balancer)
                .clientConfigurations(immutableSetOf(clients))
                .build();
    }
}
