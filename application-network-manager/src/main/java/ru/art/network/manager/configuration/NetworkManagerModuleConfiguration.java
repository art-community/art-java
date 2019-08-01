package ru.art.network.manager.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.network.manager.constants.NetworkManagerModuleConstants.BalancerMode;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.BalancerMode.ROUND_ROBIN;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.DEFAULT_STATE_PORT;

public interface NetworkManagerModuleConfiguration extends ModuleConfiguration {
    String getIpAddress();

    BalancerMode getBalancerMode();

    long getRefreshRateSeconds();

    long getConnectionPingRateSeconds();

    String getStateHost();

    String getStatePath();

    int getStatePort();

    @Getter
    class NetworkManagerModuleDefaultConfiguration implements NetworkManagerModuleConfiguration {
        private final BalancerMode balancerMode = ROUND_ROBIN;
        private final long refreshRateSeconds = 5;
        private final long connectionPingRateSeconds = 5;
        private final String ipAddress = contextConfiguration().getIpAddress();
        private final String stateHost = ipAddress;
        private final int statePort = DEFAULT_STATE_PORT;
        private final String statePath = SLASH;
    }
}
