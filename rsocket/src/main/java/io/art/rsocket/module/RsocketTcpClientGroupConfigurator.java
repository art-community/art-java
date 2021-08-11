package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;
import java.util.function.*;

@ForUsing
@RequiredArgsConstructor
public class RsocketTcpClientGroupConfigurator {
    private final String connector;
    private final BalancerMethod balancer;
    private final Set<RsocketTcpClientConfiguration> clients = set();

    public RsocketTcpClientGroupConfigurator client(UnaryOperator<RsocketTcpClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(RsocketTcpClientConfiguration.defaults(connector).toBuilder()).build());
        return this;
    }

    RsocketTcpClientGroupConfiguration configure() {
        return RsocketTcpClientGroupConfiguration.defaults(connector)
                .toBuilder()
                .balancer(balancer)
                .clientConfigurations(immutableSetOf(clients))
                .build();
    }
}
