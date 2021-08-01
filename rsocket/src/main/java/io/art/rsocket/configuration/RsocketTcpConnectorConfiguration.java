package io.art.rsocket.configuration;

import io.art.core.source.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketTcpConnectorConfiguration {
    private String connector;
    private RsocketTcpClientGroupConfiguration groupConfiguration;
    private RsocketTcpClientConfiguration singleConfiguration;

    public static RsocketTcpConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        configuration.connector = current.connector;
        configuration.groupConfiguration = orElse(
                source.getNested(GROUP_KEY, nested -> RsocketTcpClientGroupConfiguration.from(refresher, current.groupConfiguration, source)),
                current.groupConfiguration
        );
        configuration.singleConfiguration = orElse(
                source.getNested(SINGLE_KEY, nested -> RsocketTcpClientConfiguration.from(refresher, current.singleConfiguration, source)),
                current.singleConfiguration
        );
        return configuration;
    }

    public static RsocketTcpConnectorConfiguration from(RsocketModuleRefresher refresher, ConfigurationSource source) {
        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        configuration.connector = source.getSection();
        configuration.groupConfiguration = orElse(
                source.getNested(GROUP_KEY, nested -> RsocketTcpClientGroupConfiguration.from(refresher, RsocketTcpClientGroupConfiguration.defaults(configuration.connector), source)),
                RsocketTcpClientGroupConfiguration.defaults(configuration.connector)
        );
        configuration.singleConfiguration = orElse(
                source.getNested(SINGLE_KEY, nested -> RsocketTcpClientConfiguration.from(refresher, RsocketTcpClientConfiguration.defaults(configuration.connector), source)),
                RsocketTcpClientConfiguration.defaults(configuration.connector)
        );
        return configuration;
    }
}
