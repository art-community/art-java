package io.art.rsocket.configuration.communicator.tcp;

import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketTcpConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private RsocketTcpClientGroupConfiguration groupConfiguration;
    private RsocketTcpClientConfiguration singleConfiguration;

    public static RsocketTcpConnectorConfiguration from(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, current.commonConfiguration, source);
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
        String connector = configuration.commonConfiguration.getConnector();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, source);
        configuration.groupConfiguration = orElse(
                source.getNested(GROUP_KEY, nested -> RsocketTcpClientGroupConfiguration.from(refresher, RsocketTcpClientGroupConfiguration.defaults(connector), source)),
                RsocketTcpClientGroupConfiguration.defaults(connector)
        );
        configuration.singleConfiguration = orElse(
                source.getNested(SINGLE_KEY, nested -> RsocketTcpClientConfiguration.from(refresher, RsocketTcpClientConfiguration.defaults(connector), source)),
                RsocketTcpClientConfiguration.defaults(connector)
        );
        return configuration;
    }
}
