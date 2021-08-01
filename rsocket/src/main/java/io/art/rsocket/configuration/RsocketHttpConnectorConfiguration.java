package io.art.rsocket.configuration;

import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketHttpConnectorConfiguration {
    private String connector;
    private RsocketHttpClientGroupConfiguration groupConfiguration;
    private RsocketHttpClientConfiguration singleConfiguration;

    public static RsocketHttpConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketHttpConnectorConfiguration current, ConfigurationSource source) {
        RsocketHttpConnectorConfiguration configuration = RsocketHttpConnectorConfiguration.builder().build();
        configuration.connector = current.connector;
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
        configuration.connector = source.getSection();
        configuration.groupConfiguration = orElse(
                source.getNested(GROUP_KEY, nested -> RsocketHttpClientGroupConfiguration.from(refresher, RsocketHttpClientGroupConfiguration.defaults(configuration.connector), source)),
                RsocketHttpClientGroupConfiguration.defaults(configuration.connector)
        );
        configuration.singleConfiguration = orElse(
                source.getNested(SINGLE_KEY, nested -> RsocketHttpClientConfiguration.from(refresher, RsocketHttpClientConfiguration.defaults(configuration.connector), source)),
                RsocketHttpClientConfiguration.defaults(configuration.connector)
        );
        return configuration;
    }
}
