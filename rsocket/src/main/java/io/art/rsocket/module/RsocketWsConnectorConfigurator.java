package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import io.art.rsocket.configuration.communicator.ws.*;
import io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@ForUsing
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
        single = configurator.apply(RsocketWsClientConfiguration.defaults(connector).toBuilder()).build();
        return this;
    }

    public RsocketWsConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketWsConnectorConfiguration configure() {
        return RsocketWsConnectorConfiguration.builder()
                .commonConfiguration(commonConfigurator.apply(RsocketCommonConnectorConfiguration.defaults(connector).toBuilder()).build())
                .groupConfiguration(orElse(group, RsocketWsClientGroupConfiguration.defaults(connector)))
                .singleConfiguration(orElse(single, RsocketWsClientConfiguration.defaults(connector)))
                .build();
    }
}
