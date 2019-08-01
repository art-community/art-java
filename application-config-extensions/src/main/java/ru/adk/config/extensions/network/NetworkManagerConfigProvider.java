package ru.adk.config.extensions.network;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.network.NetworkManagerConfigKeys.NETWORK_SECTION_ID;

public interface NetworkManagerConfigProvider {
    static Config networkManagerConfig() {
        return configInner(NETWORK_SECTION_ID);
    }
}
