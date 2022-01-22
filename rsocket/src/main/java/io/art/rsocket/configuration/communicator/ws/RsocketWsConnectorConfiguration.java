package io.art.rsocket.configuration.communicator.ws;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.rsocket.transport.netty.client.*;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class RsocketWsConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private ImmutableSet<RsocketWsClientConfiguration> clientConfigurations;
    private BalancerMethod balancer;
    private UnaryOperator<HttpClient> clientDecorator;
    private UnaryOperator<WebsocketClientTransport> transportDecorator;

    public static RsocketWsConnectorConfiguration wsConnectorConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, ConfigurationSource source) {
        RsocketWsConnectorConfiguration currentConfiguration = orElse(current, wsConnectorConfiguration(source.getParent()));

        RsocketWsConnectorConfiguration configuration = RsocketWsConnectorConfiguration.builder().build();
        configuration.commonConfiguration = commonConnectorConfiguration(refresher, currentConfiguration.commonConfiguration, source);
        configuration.clientDecorator = current.clientDecorator;
        configuration.transportDecorator = current.transportDecorator;
        configuration.balancer = rsocketBalancer(source.getString(BALANCER_KEY), current.balancer);

        ImmutableSet<RsocketWsClientConfiguration> clientConfigurations = immutableSetOf(source.getNestedArray(
                CLIENTS_SECTION,
                nested -> clientConfiguration(refresher, current, nested)
        ));

        configuration.clientConfigurations = merge(current.clientConfigurations, clientConfigurations);
        return configuration;
    }

    public static RsocketWsConnectorConfiguration wsConnectorConfiguration(String connector) {
        return builder()
                .commonConfiguration(commonConnectorConfiguration(connector))
                .balancer(ROUND_ROBIN)
                .clientConfigurations(immutableSetOf(wsClientConfiguration(connector)))
                .clientDecorator(identity())
                .transportDecorator(identity())
                .build();
    }


    private static RsocketWsClientConfiguration clientConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, NestedConfiguration nested) {
        RsocketWsClientConfiguration defaults = wsClientConfiguration(current.commonConfiguration.getConnector());
        return wsClientConfiguration(refresher, defaults, nested);
    }
}
