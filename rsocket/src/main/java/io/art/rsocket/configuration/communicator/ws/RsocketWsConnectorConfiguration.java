package io.art.rsocket.configuration.communicator.ws;

import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketWsConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private RsocketWsClientGroupConfiguration groupConfiguration;
    private RsocketWsClientConfiguration singleConfiguration;

    public static RsocketWsConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, ConfigurationSource source) {
        RsocketWsConnectorConfiguration currentConfiguration = orElse(current, () -> defaults(source.getSection()));

        RsocketWsConnectorConfiguration configuration = RsocketWsConnectorConfiguration.builder().build();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, currentConfiguration.commonConfiguration, source);

        RsocketWsClientGroupConfiguration groupConfiguration = source.getNested(GROUP_SECTION, nested -> groupConfiguration(refresher, currentConfiguration, nested));
        RsocketWsClientGroupConfiguration defaultGroup = RsocketWsClientGroupConfiguration.defaults(currentConfiguration.commonConfiguration.getConnector());
        configuration.groupConfiguration = orElse(groupConfiguration, orElse(currentConfiguration.groupConfiguration, defaultGroup));

        RsocketWsClientConfiguration clientConfiguration = source.getNested(SINGLE_SECTION, nested -> singleConfiguration(refresher, currentConfiguration, nested));
        RsocketWsClientConfiguration defaultSingle = RsocketWsClientConfiguration.defaults(currentConfiguration.commonConfiguration.getConnector());
        configuration.singleConfiguration = orElse(clientConfiguration, orElse(currentConfiguration.singleConfiguration, defaultSingle));

        return configuration;
    }

    public static RsocketWsConnectorConfiguration defaults(String connector) {
        return RsocketWsConnectorConfiguration.builder()
                .commonConfiguration(RsocketCommonConnectorConfiguration.defaults(connector))
                .build();
    }

    private static RsocketWsClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, NestedConfiguration nested) {
        RsocketWsClientConfiguration clientConfiguration = orElse(current.singleConfiguration, RsocketWsClientConfiguration.defaults(current.commonConfiguration.getConnector()));
        return RsocketWsClientConfiguration.from(refresher, clientConfiguration, nested);
    }

    private static RsocketWsClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketWsConnectorConfiguration current, NestedConfiguration nested) {
        RsocketWsClientGroupConfiguration groupConfiguration = orElse(current.groupConfiguration, RsocketWsClientGroupConfiguration.defaults(current.commonConfiguration.getConnector()));
        return RsocketWsClientGroupConfiguration.from(refresher, groupConfiguration, nested);
    }
}
