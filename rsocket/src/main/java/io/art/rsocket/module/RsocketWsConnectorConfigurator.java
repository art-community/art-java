package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.ws.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientGroupConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@Public
@RequiredArgsConstructor
public class RsocketWsConnectorConfigurator {
    private final String connector;
    private RsocketWsClientGroupConfiguration group;
    private RsocketWsClientConfiguration single;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketWsConnectorConfigurator roundRobin(UnaryOperator<RsocketWsClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketWsClientGroupConfigurator(connector, ROUND_ROBIN)).configure();
        return this;
    }

    public RsocketWsConnectorConfigurator weighted(UnaryOperator<RsocketWsClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketWsClientGroupConfigurator(connector, WEIGHTED)).configure();
        return this;
    }

    public RsocketWsConnectorConfigurator single(UnaryOperator<RsocketWsClientConfigurationBuilder> configurator) {
        single = configurator.apply(wsClientConfiguration(connector).toBuilder()).build();
        return this;
    }

    public RsocketWsConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketWsConnectorConfiguration configure() {
        return RsocketWsConnectorConfiguration.builder()
                .commonConfiguration(commonConfigurator.apply(commonConnectorConfiguration(connector).toBuilder()).build())
                .groupConfiguration(orElse(group, wsClientGroupConfiguration(connector)))
                .singleConfiguration(orElse(single, wsClientConfiguration(connector)))
                .build();
    }
}
