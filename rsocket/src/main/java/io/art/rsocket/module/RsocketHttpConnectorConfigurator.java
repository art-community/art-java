package io.art.rsocket.module;

import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import io.art.rsocket.configuration.communicator.http.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import java.util.function.*;

@RequiredArgsConstructor
public class RsocketHttpConnectorConfigurator {
    private final String connector;
    private RsocketHttpClientGroupConfiguration group;
    private RsocketHttpClientConfiguration single;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    public RsocketHttpConnectorConfigurator roundRobin(UnaryOperator<RsocketHttpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketHttpClientGroupConfigurator(connector, ROUND_ROBIN)).configure();
        return this;
    }

    public RsocketHttpConnectorConfigurator weighted(UnaryOperator<RsocketHttpClientGroupConfigurator> configurator) {
        group = configurator.apply(new RsocketHttpClientGroupConfigurator(connector, WEIGHTED)).configure();
        return this;
    }

    public RsocketHttpConnectorConfigurator single(UnaryOperator<RsocketHttpClientConfigurator> configurator) {
        RsocketHttpClientConfigurator clientConfigurator = configurator.apply(new RsocketHttpClientConfigurator());
        single = clientConfigurator.http().apply(RsocketHttpClientConfiguration.defaults(connector).toBuilder())
                .commonConfiguration(clientConfigurator.common().apply(RsocketCommonClientConfiguration.defaults(connector).toBuilder()).build())
                .build();
        return this;
    }

    public RsocketHttpConnectorConfigurator configure(UnaryOperator<RsocketCommonConnectorConfigurationBuilder> configurator) {
        this.commonConfigurator = configurator;
        return this;
    }

    RsocketHttpConnectorConfiguration configure() {
        return RsocketHttpConnectorConfiguration.builder()
                .commonConfiguration(commonConfigurator.apply(RsocketCommonConnectorConfiguration.defaults(connector).toBuilder()).build())
                .groupConfiguration(orElse(group, RsocketHttpClientGroupConfiguration.defaults(connector)))
                .singleConfiguration(orElse(single, RsocketHttpClientConfiguration.defaults(connector)))
                .build();
    }
}
