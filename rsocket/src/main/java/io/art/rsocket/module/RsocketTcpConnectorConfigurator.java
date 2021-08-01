package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import lombok.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketTcpConnectorConfigurator {
    private final String connector;
    private RsocketTcpClientGroupConfiguration group;
    private RsocketTcpClientConfiguration single;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator;

    public RsocketTcpConnectorConfigurator roundRobin(UnaryOperator<RsocketTcpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketTcpClientGroupConfigurator(connector, ROUND_ROBIN)).configure();
        return this;
    }

    public RsocketTcpConnectorConfigurator weighted(UnaryOperator<RsocketTcpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketTcpClientGroupConfigurator(connector, WEIGHTED)).configure();
        return this;
    }

    public RsocketTcpConnectorConfigurator single(UnaryOperator<RsocketTcpClientConfigurator> configurator) {
        RsocketTcpClientConfigurator clientConfigurator = configurator.apply(new RsocketTcpClientConfigurator());
        single = clientConfigurator
                .tcp()
                .apply(RsocketTcpClientConfiguration.builder())
                .commonConfiguration(clientConfigurator.common().apply(RsocketCommonClientConfiguration.defaults(connector).toBuilder()).build())
                .build();
        return this;
    }

    public RsocketTcpConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketTcpConnectorConfiguration configure() {
        return RsocketTcpConnectorConfiguration.builder()
                .commonConfiguration(commonConfigurator.apply(RsocketCommonConnectorConfiguration.defaults(connector).toBuilder()).build())
                .groupConfiguration(group)
                .singleConfiguration(single)
                .build();
    }
}
