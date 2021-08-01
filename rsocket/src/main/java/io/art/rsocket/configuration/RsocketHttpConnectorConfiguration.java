package io.art.rsocket.configuration;

import io.art.core.source.*;
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
        configuration.groupConfiguration = orElse(
                source.getNested(GROUP_KEY, nested -> RsocketHttpClientGroupConfiguration.from(refresher, current.groupConfiguration, source)),
                current.groupConfiguration
        );
        configuration.singleConfiguration = orElse(
                source.getNested(SINGLE_KEY, nested -> RsocketHttpClientConfiguration.from(refresher, current.singleConfiguration, source)),
                current.singleConfiguration
        );
        return configuration;
    }

    public static RsocketHttpConnectorConfiguration from(RsocketModuleRefresher refresher, ConfigurationSource source) {
        RsocketHttpConnectorConfiguration configuration = RsocketHttpConnectorConfiguration.builder().build();
        String connector = configuration.commonConfiguration.getConnector();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, source);
        configuration.groupConfiguration = orElse(
                source.getNested(GROUP_KEY, nested -> RsocketHttpClientGroupConfiguration.from(refresher, RsocketHttpClientGroupConfiguration.defaults(connector), source)),
                RsocketHttpClientGroupConfiguration.defaults(connector)
        );
        configuration.singleConfiguration = orElse(
                source.getNested(SINGLE_KEY, nested -> RsocketHttpClientConfiguration.from(refresher, RsocketHttpClientConfiguration.defaults(connector), source)),
                RsocketHttpClientConfiguration.defaults(connector)
        );
        return configuration;
    }
}
