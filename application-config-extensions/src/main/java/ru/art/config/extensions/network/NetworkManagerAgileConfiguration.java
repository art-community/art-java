package ru.art.config.extensions.network;

import lombok.Getter;
import ru.art.network.manager.configuration.NetworkManagerModuleConfiguration.NetworkManagerModuleDefaultConfiguration;
import ru.art.network.manager.constants.NetworkManagerModuleConstants.BalancerMode;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.network.NetworkManagerConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.ifException;

@Getter
public class NetworkManagerAgileConfiguration extends NetworkManagerModuleDefaultConfiguration {
    private String ipAddress;
    private BalancerMode balancerMode;
    private long refreshRateSeconds;
    private long aliveConnectionUpdateSeconds;
    private String stateHost;
    private int statePort;
    private String statePath;

    public NetworkManagerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        ipAddress = configString(NETWORK_SECTION_ID, IP_ADDRESS, super.getIpAddress());
        balancerMode = ifException(() -> BalancerMode.valueOf(configString(NETWORK_SECTION_ID, BALANCER_MODE)), super.getBalancerMode());
        refreshRateSeconds = configLong(NETWORK_SECTION_ID, REFRESH_RATE_SECONDS, super.getRefreshRateSeconds());
        aliveConnectionUpdateSeconds = configLong(NETWORK_SECTION_ID, ALIVE_CONNECTION_UPDATE_SECONDS, super.getConnectionPingRateSeconds());
        stateHost = configString(NETWORK_SECTION_ID, STATE_HOST, super.getStateHost());
        statePath = configString(NETWORK_SECTION_ID, STATE_PATH, super.getStatePath());
        statePort = configInt(NETWORK_SECTION_ID, STATE_PORT, super.getStatePort());
    }
}
