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
        RsocketTcpConnectorConfiguration currentConfiguration = orElse(current, RsocketTcpConnectorConfiguration.defaults(source.getParent()));

        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        configuration.commonConfiguration = RsocketCommonConnectorConfiguration.from(refresher, currentConfiguration.commonConfiguration, source);

        RsocketTcpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_SECTION, nested -> groupConfiguration(refresher, currentConfiguration, nested));
        RsocketTcpClientGroupConfiguration defaultGroup = RsocketTcpClientGroupConfiguration.defaults(currentConfiguration.commonConfiguration.getConnector());
        configuration.groupConfiguration = orElse(groupConfiguration, orElse(currentConfiguration.groupConfiguration, defaultGroup));

        RsocketTcpClientConfiguration singleConfiguration = source.getNested(SINGLE_SECTION, nested -> singleConfiguration(refresher, currentConfiguration, nested));
        RsocketTcpClientConfiguration defaultSingle = RsocketTcpClientConfiguration.defaults(currentConfiguration.commonConfiguration.getConnector());
        configuration.singleConfiguration = orElse(singleConfiguration, orElse(currentConfiguration.singleConfiguration, defaultSingle));

        return configuration;
    }

    public static RsocketTcpConnectorConfiguration defaults(String connector) {
        return RsocketTcpConnectorConfiguration.builder()
                .commonConfiguration(RsocketCommonConnectorConfiguration.defaults(connector))
                .build();
    }

    private static RsocketTcpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpClientConfiguration clientConfiguration = orElse(current.singleConfiguration, RsocketTcpClientConfiguration.defaults(current.commonConfiguration.getConnector()));
        return RsocketTcpClientConfiguration.from(refresher, clientConfiguration, source);
    }

    private static RsocketTcpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpClientGroupConfiguration groupConfiguration = orElse(current.groupConfiguration, RsocketTcpClientGroupConfiguration.defaults(current.commonConfiguration.getConnector()));
        return RsocketTcpClientGroupConfiguration.from(refresher, groupConfiguration, source);
    }
}
