package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@ForUsing
@RequiredArgsConstructor
public class RsocketTcpConnectorConfigurator {
    private final String connector;
    private RsocketTcpClientGroupConfiguration group;
    private RsocketTcpClientConfiguration single;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketTcpConnectorConfigurator roundRobin(UnaryOperator<RsocketTcpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketTcpClientGroupConfigurator(connector, ROUND_ROBIN)).configure();
        return this;
    }

    public RsocketTcpConnectorConfigurator weighted(UnaryOperator<RsocketTcpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketTcpClientGroupConfigurator(connector, WEIGHTED)).configure();
        return this;
    }

    public RsocketTcpConnectorConfigurator single(UnaryOperator<RsocketTcpClientConfigurationBuilder> configurator) {
        single = configurator.apply(RsocketTcpClientConfiguration.defaults(connector).toBuilder()).build();
        return this;
    }

    public RsocketTcpConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketTcpConnectorConfiguration configure() {
        return RsocketTcpConnectorConfiguration.builder()
                .commonConfiguration(commonConfigurator.apply(RsocketCommonConnectorConfiguration.defaults(connector).toBuilder()).build())
                .groupConfiguration(orElse(group, RsocketTcpClientGroupConfiguration.defaults(connector)))
                .singleConfiguration(orElse(single, RsocketTcpClientConfiguration.defaults(connector)))
                .build();
    }
}
