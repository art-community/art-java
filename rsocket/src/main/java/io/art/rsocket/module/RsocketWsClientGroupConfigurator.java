package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import lombok.*;
import static io.art.core.factory.SetFactory.*;
import java.util.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class RsocketWsClientGroupConfigurator {
    private final String connector;
    private final BalancerMethod balancer;
    private final Set<RsocketWsClientConfiguration> clients = set();

    public RsocketWsClientGroupConfigurator client(UnaryOperator<RsocketWsClientConfigurationBuilder> configurator) {
        clients.add(configurator.apply(RsocketWsClientConfiguration.wsClientConfiguration(connector).toBuilder()).build());
        return this;
    }

    RsocketWsClientGroupConfiguration configure() {
        return RsocketWsClientGroupConfiguration.wsClientGroupConfiguration(connector)
                .toBuilder()
                .balancer(balancer)
                .clientConfigurations(immutableSetOf(clients))
                .build();
    }
}
