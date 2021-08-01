package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.*;
import io.art.rsocket.configuration.communicator.RsocketTcpClientConfiguration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketTcpClientGroupConfigurator {
    private final String connector;
    private final BalancerMethod balancer;
    private final Set<RsocketTcpClientConfiguration> clients = set();

    public RsocketTcpClientGroupConfigurator client(UnaryOperator<RsocketTcpClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(RsocketTcpClientConfiguration.builder().connector(connector)).build());
        return this;
    }

    RsocketTcpClientGroupConfiguration configure() {
        return RsocketTcpClientGroupConfiguration.builder()
                .connector(connector)
                .balancer(balancer)
                .clientConfigurations(immutableSetOf(clients))
                .build();
    }
}
