package io.art.rsocket.module;

import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.RsocketTcpClientConfiguration.*;
import lombok.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketTcpConnectorConfigurator {
    private final String connector;
    private RsocketTcpClientGroupConfiguration group;
    private RsocketTcpClientConfiguration single;

    public RsocketTcpConnectorConfigurator roundRobin(UnaryOperator<RsocketTcpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketTcpClientGroupConfigurator(connector, ROUND_ROBIN)).configure();
        return this;
    }

    public RsocketTcpConnectorConfigurator weighted(UnaryOperator<RsocketTcpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketTcpClientGroupConfigurator(connector, WEIGHTED)).configure();
        return this;
    }

    public RsocketTcpConnectorConfigurator single(UnaryOperator<RsocketTcpClientConfigurationBuilder> configurator) {
        single = configurator.apply(RsocketTcpClientConfiguration.builder().connector(connector)).build();
        return this;
    }

    RsocketTcpConnectorConfiguration configure() {
        return RsocketTcpConnectorConfiguration.builder()
                .connector(connector)
                .groupConfiguration(group)
                .singleConfiguration(single)
                .build();
    }
}
