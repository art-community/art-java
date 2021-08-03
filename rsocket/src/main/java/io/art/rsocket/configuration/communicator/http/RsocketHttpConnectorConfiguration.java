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
        RsocketHttpConnectorConfiguration configuration = RsocketHttpConnectorConfiguration.builder().build();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, current.commonConfiguration, source);

        RsocketHttpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_KEY, nested -> groupConfiguration(refresher, current, nested));
        configuration.groupConfiguration = orElse(groupConfiguration, current.groupConfiguration);

        RsocketHttpClientConfiguration clientConfiguration = source.getNested(SINGLE_KEY, nested -> singleConfiguration(refresher, current, nested));
        configuration.singleConfiguration = orElse(clientConfiguration, current.singleConfiguration);
        return configuration;
    }

    public static RsocketHttpConnectorConfiguration from(RsocketModuleRefresher refresher, ConfigurationSource source) {
        RsocketHttpConnectorConfiguration configuration = RsocketHttpConnectorConfiguration.builder().build();
        String connector = configuration.commonConfiguration.getConnector();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, source);

        RsocketHttpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_KEY, nested -> groupConfiguration(refresher, connector, nested));
        configuration.groupConfiguration = orElse(groupConfiguration, RsocketHttpClientGroupConfiguration.defaults(connector));

        RsocketHttpClientConfiguration clientConfiguration = source.getNested(SINGLE_KEY, nested -> singleConfiguration(refresher, source, connector));
        configuration.singleConfiguration = orElse(clientConfiguration, RsocketHttpClientConfiguration.defaults(connector));
        return configuration;
    }


    private static RsocketHttpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, ConfigurationSource source, String connector) {
        RsocketHttpClientConfiguration defaults = RsocketHttpClientConfiguration.defaults(connector);
        return RsocketHttpClientConfiguration.from(refresher, defaults, source);
    }

    private static RsocketHttpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, String connector, NestedConfiguration nested) {
        RsocketHttpClientGroupConfiguration defaults = RsocketHttpClientGroupConfiguration.defaults(connector);
        return RsocketHttpClientGroupConfiguration.from(refresher, defaults, nested);
    }

    private static RsocketHttpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketHttpConnectorConfiguration current, NestedConfiguration nested) {
        return RsocketHttpClientConfiguration.from(refresher, current.singleConfiguration, nested);
    }

    private static RsocketHttpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketHttpConnectorConfiguration current, NestedConfiguration nested) {
        return RsocketHttpClientGroupConfiguration.from(refresher, current.groupConfiguration, nested);
    }
}
