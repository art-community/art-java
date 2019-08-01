package ru.art.config.extensions.network;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.network.NetworkManagerConfigKeys.NETWORK_SECTION_ID;

public interface NetworkManagerConfigProvider {
    static Config networkManagerConfig() {
        return configInner(NETWORK_SECTION_ID);
    }
}
