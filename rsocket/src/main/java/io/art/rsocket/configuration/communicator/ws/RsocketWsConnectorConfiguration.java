package io.art.rsocket.configuration.communicator.ws;

import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.ws.RsocketWsClientGroupConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketWsConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private RsocketWsClientGroupConfiguration groupConfiguration;
    private RsocketWsClientConfiguration singleConfiguration;

    public static RsocketWsConnectorConfiguration wsConnectorConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, ConfigurationSource source) {
        RsocketWsConnectorConfiguration currentConfiguration = orElse(current, () -> wsConnectorConfiguration(source.getParent()));

        RsocketWsConnectorConfiguration configuration = RsocketWsConnectorConfiguration.builder().build();
        configuration.commonConfiguration = commonConnectorConfiguration(refresher, currentConfiguration.commonConfiguration, source);

        RsocketWsClientGroupConfiguration groupConfiguration = source.getNested(GROUP_SECTION, nested -> groupConfiguration(refresher, currentConfiguration, nested));
        RsocketWsClientGroupConfiguration defaultGroup = wsClientGroupConfiguration(currentConfiguration.commonConfiguration.getConnector());
        configuration.groupConfiguration = orElse(groupConfiguration, orElse(currentConfiguration.groupConfiguration, defaultGroup));

        RsocketWsClientConfiguration clientConfiguration = source.getNested(SINGLE_SECTION, nested -> singleConfiguration(refresher, currentConfiguration, nested));
        RsocketWsClientConfiguration defaultSingle = wsClientConfiguration(currentConfiguration.commonConfiguration.getConnector());
        configuration.singleConfiguration = orElse(clientConfiguration, orElse(currentConfiguration.singleConfiguration, defaultSingle));

        return configuration;
    }

    public static RsocketWsConnectorConfiguration wsConnectorConfiguration(String connector) {
        return RsocketWsConnectorConfiguration.builder()
                .commonConfiguration(commonConnectorConfiguration(connector))
                .build();
    }

    private static RsocketWsClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, NestedConfiguration nested) {
        RsocketWsClientConfiguration wsClientConfiguration = wsClientConfiguration(current.commonConfiguration.getConnector());
        RsocketWsClientConfiguration clientConfiguration = orElse(current.singleConfiguration, wsClientConfiguration);
        return wsClientConfiguration(refresher, clientConfiguration, nested);
    }

    private static RsocketWsClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, NestedConfiguration nested) {
        RsocketWsClientGroupConfiguration wsClientGroupConfiguration = wsClientGroupConfiguration(current.commonConfiguration.getConnector());
        RsocketWsClientGroupConfiguration groupConfiguration = orElse(current.groupConfiguration, wsClientGroupConfiguration);
        return wsClientGroupConfiguration(refresher, groupConfiguration, nested);
    }
}
