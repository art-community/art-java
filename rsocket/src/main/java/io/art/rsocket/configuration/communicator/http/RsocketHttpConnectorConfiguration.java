package io.art.rsocket.configuration.communicator.http;

import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketHttpConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private RsocketHttpClientGroupConfiguration groupConfiguration;
    private RsocketHttpClientConfiguration singleConfiguration;

    public static RsocketHttpConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketHttpConnectorConfiguration current, ConfigurationSource source) {
        RsocketHttpConnectorConfiguration currentConfiguration = orElse(current, () -> defaults(source.getSection()));

        RsocketHttpConnectorConfiguration configuration = RsocketHttpConnectorConfiguration.builder().build();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, currentConfiguration.commonConfiguration, source);

        RsocketHttpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_KEY, nested -> groupConfiguration(refresher, currentConfiguration, nested));
        configuration.groupConfiguration = orElse(groupConfiguration, currentConfiguration.groupConfiguration);

        RsocketHttpClientConfiguration clientConfiguration = source.getNested(SINGLE_KEY, nested -> singleConfiguration(refresher, currentConfiguration, nested));
        configuration.singleConfiguration = orElse(clientConfiguration, currentConfiguration.singleConfiguration);

        return configuration;
    }

    public static RsocketHttpConnectorConfiguration defaults(String connector) {
        return RsocketHttpConnectorConfiguration.builder()
                .commonConfiguration(RsocketCommonConnectorConfiguration.defaults(connector))
                .build();
    }

    private static RsocketHttpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketHttpConnectorConfiguration current, NestedConfiguration nested) {
        RsocketHttpClientConfiguration clientConfiguration = orElse(current.singleConfiguration, RsocketHttpClientConfiguration.defaults(current.commonConfiguration.getConnector()));
        return RsocketHttpClientConfiguration.from(refresher, clientConfiguration, nested);
    }

    private static RsocketHttpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketHttpConnectorConfiguration current, NestedConfiguration nested) {
        RsocketHttpClientGroupConfiguration groupConfiguration = orElse(current.groupConfiguration, RsocketHttpClientGroupConfiguration.defaults(current.commonConfiguration.getConnector()));
        return RsocketHttpClientGroupConfiguration.from(refresher, groupConfiguration, nested);
    }
}
