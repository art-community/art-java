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

        RsocketTcpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_KEY, nested -> groupConfiguration(refresher, current, nested));
        configuration.groupConfiguration = orElse(groupConfiguration, current.groupConfiguration);

        RsocketTcpClientConfiguration singleConfiguration = source.getNested(SINGLE_KEY, nested -> singleConfiguration(refresher, current, nested));
        configuration.singleConfiguration = orElse(singleConfiguration, current.singleConfiguration);

        return configuration;
    }

    public static RsocketTcpConnectorConfiguration from(RsocketModuleRefresher refresher, ConfigurationSource source) {
        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        String connector = configuration.commonConfiguration.getConnector();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, source);

        RsocketTcpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_KEY, nested -> groupConfiguration(refresher, source, connector));
        configuration.groupConfiguration = orElse(groupConfiguration, RsocketTcpClientGroupConfiguration.defaults(connector));

        RsocketTcpClientConfiguration singleConfiguration = source.getNested(SINGLE_KEY, nested -> singleConfiguration(refresher, source, connector));
        configuration.singleConfiguration = orElse(singleConfiguration, RsocketTcpClientConfiguration.defaults(connector));

        return configuration;
    }


    private static RsocketTcpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        return RsocketTcpClientConfiguration.from(refresher, current.singleConfiguration, source);
    }

    private static RsocketTcpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        return RsocketTcpClientGroupConfiguration.from(refresher, current.groupConfiguration, source);
    }

    private static RsocketTcpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, ConfigurationSource source, String connector) {
        RsocketTcpClientConfiguration defaults = RsocketTcpClientConfiguration.defaults(connector);
        return RsocketTcpClientConfiguration.from(refresher, defaults, source);
    }

    private static RsocketTcpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, ConfigurationSource source, String connector) {
        RsocketTcpClientGroupConfiguration defaults = RsocketTcpClientGroupConfiguration.defaults(connector);
        return RsocketTcpClientGroupConfiguration.from(refresher, defaults, source);
    }
}
