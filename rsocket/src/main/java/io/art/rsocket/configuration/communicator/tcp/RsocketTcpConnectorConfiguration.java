package io.art.rsocket.configuration.communicator.tcp;

import io.art.core.source.*;
import io.art.rsocket.configuration.communicator.common.*;
import io.art.rsocket.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.rsocket.configuration.communicator.common.RsocketCommonConnectorConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientConfiguration.*;
import static io.art.rsocket.configuration.communicator.tcp.RsocketTcpClientGroupConfiguration.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketTcpConnectorConfiguration {
    private RsocketCommonConnectorConfiguration commonConfiguration;
    private RsocketTcpClientGroupConfiguration groupConfiguration;
    private RsocketTcpClientConfiguration singleConfiguration;

    public static RsocketTcpConnectorConfiguration tcpConnectorConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpConnectorConfiguration currentConfiguration = orElse(current, tcpConnectorConfiguration(source.getParent()));

        RsocketTcpConnectorConfiguration configuration = RsocketTcpConnectorConfiguration.builder().build();
        configuration.commonConfiguration = commonConnectorConfiguration(refresher, currentConfiguration.commonConfiguration, source);

        RsocketTcpClientGroupConfiguration groupConfiguration = source.getNested(GROUP_SECTION, nested -> groupConfiguration(refresher, currentConfiguration, nested));
        RsocketTcpClientGroupConfiguration defaultGroup = tcpClientGroupConfiguration(currentConfiguration.commonConfiguration.getConnector());
        configuration.groupConfiguration = orElse(groupConfiguration, orElse(currentConfiguration.groupConfiguration, defaultGroup));

        RsocketTcpClientConfiguration singleConfiguration = source.getNested(SINGLE_SECTION, nested -> singleConfiguration(refresher, currentConfiguration, nested));
        RsocketTcpClientConfiguration defaultSingle = tcpClientConfiguration(currentConfiguration.commonConfiguration.getConnector());
        configuration.singleConfiguration = orElse(singleConfiguration, orElse(currentConfiguration.singleConfiguration, defaultSingle));

        return configuration;
    }

    public static RsocketTcpConnectorConfiguration tcpConnectorConfiguration(String connector) {
        return builder()
                .commonConfiguration(commonConnectorConfiguration(connector))
                .build();
    }

    private static RsocketTcpClientConfiguration singleConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpClientConfiguration tcpClientConfiguration = tcpClientConfiguration(current.commonConfiguration.getConnector());
        RsocketTcpClientConfiguration clientConfiguration = orElse(current.singleConfiguration, tcpClientConfiguration);
        return tcpClientConfiguration(refresher, clientConfiguration, source);
    }

    private static RsocketTcpClientGroupConfiguration groupConfiguration(RsocketModuleRefresher refresher, RsocketTcpConnectorConfiguration current, ConfigurationSource source) {
        RsocketTcpClientGroupConfiguration tcpClientGroupConfiguration = tcpClientGroupConfiguration(current.commonConfiguration.getConnector());
        RsocketTcpClientGroupConfiguration groupConfiguration = orElse(current.groupConfiguration, tcpClientGroupConfiguration);
        return tcpClientGroupConfiguration(refresher, groupConfiguration, source);
    }
}
