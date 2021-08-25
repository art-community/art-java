package io.art.rsocket.module;

import io.art.core.annotation.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientGroupConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@Public
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
        single = configurator.apply(tcpClientConfiguration(connector).toBuilder()).build();
        return this;
    }

    public RsocketTcpConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketTcpConnectorConfiguration configure() {
        return RsocketTcpConnectorConfiguration.builder()
                .commonConfiguration(commonConfigurator.apply(commonConnectorConfiguration(connector).toBuilder()).build())
                .groupConfiguration(orElse(group, tcpClientGroupConfiguration(connector)))
                .singleConfiguration(orElse(single, tcpClientConfiguration(connector)))
                .build();
    }
}
