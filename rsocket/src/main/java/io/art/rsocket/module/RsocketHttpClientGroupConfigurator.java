package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.http.*;
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

    public RsocketHttpClientGroupConfigurator client(UnaryOperator<RsocketHttpClientConfigurator> configurator) {
        RsocketHttpClientConfigurator clientConfigurator = configurator.apply(new RsocketHttpClientConfigurator());
        clients.add(clientConfigurator.http()
                .apply(RsocketHttpClientConfiguration.defaults(connector).toBuilder())
                .commonConfiguration(clientConfigurator.common().apply(RsocketCommonClientConfiguration.defaults(connector).toBuilder()).build())
                .build());
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
