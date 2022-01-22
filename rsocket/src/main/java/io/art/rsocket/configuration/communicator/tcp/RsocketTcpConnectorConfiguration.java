package io.art.rsocket.configuration.communicator.tcp;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import reactor.netty.tcp.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class RsocketTcpConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private ImmutableSet<RsocketTcpClientConfiguration> clientConfigurations;
    private BalancerMethod balancer;
    private UnaryOperator<TcpClient> clientDecorator;
    private UnaryOperator<TcpClientTransport> transportDecorator;

    public static RsocketTcpConnectorConfiguration tcpConnectorConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpConnectorConfiguration currentConfiguration = orElse(current, tcpConnectorConfiguration(source.getParent()));

        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        configuration.commonConfiguration = commonConnectorConfiguration(refresher, currentConfiguration.commonConfiguration, source);
        configuration.clientDecorator = current.clientDecorator;
        configuration.transportDecorator = current.transportDecorator;
        configuration.balancer = rsocketBalancer(source.getString(BALANCER_KEY), current.balancer);

        ImmutableSet<RsocketTcpClientConfiguration> clientConfigurations = immutableSetOf(source.getNestedArray(
                CLIENTS_SECTION,
                nested -> clientConfiguration(refresher, current, nested)
        ));

        configuration.clientConfigurations = merge(current.clientConfigurations, clientConfigurations);
        return configuration;
    }

    public static RsocketTcpConnectorConfiguration tcpConnectorConfiguration(String connector) {
        return builder()
                .commonConfiguration(commonConnectorConfiguration(connector))
                .balancer(ROUND_ROBIN)
                .clientConfigurations(immutableSetOf(tcpClientConfiguration(connector)))
                .clientDecorator(identity())
                .transportDecorator(identity())
                .build();
    }


    private static RsocketTcpClientConfiguration clientConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, NestedConfiguration nested) {
        RsocketTcpClientConfiguration defaults = tcpClientConfiguration(current.commonConfiguration.getConnector());
        return tcpClientConfiguration(refresher, defaults, nested);
    }
}
