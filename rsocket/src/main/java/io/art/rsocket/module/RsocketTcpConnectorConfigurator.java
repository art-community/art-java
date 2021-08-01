package io.art.rsocket.module;

import io.art.communicator.configurator.*;
import io.art.communicator.proxy.*;
import io.art.core.collection.*;
import io.art.core.property.*;
import io.art.rsocket.communicator.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import io.art.rsocket.configuration.communicator.tcp.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.module.RsocketModule.*;
import java.util.function.*;

public class RsocketTcpConnectorConfigurator extends CommunicatorConfigurator {
    private final String connector;
    private RsocketTcpClientGroupConfiguration group;
    private RsocketTcpClientConfiguration single;
    private UnaryOperator<RsocketCommonConnectorConfigurationBuilder> commonConfigurator = UnaryOperator.identity();

    RsocketTcpConnectorConfigurator(String connector) {
        super(() -> rsocketModule().configuration().getCommunicatorConfiguration(), () -> new RsocketCommunication(rsocketModule().configuration()));
        this.connector = connector;
    }

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
        single = clientConfigurator.tcp().apply(RsocketTcpClientConfiguration.defaults(connector).toBuilder())
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
                .singleConfiguration(orElse(single, RsocketTcpClientConfiguration.defaults(connector)))
                .build();
    }

    LazyProperty<ImmutableMap<Class<?>, CommunicatorProxy<?>>> communicatorProxies() {
        return get();
    }
}
