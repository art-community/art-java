package ru.adk.config.extensions.rsocket;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.rsocket.RsocketConfigKeys.RSOCKET_SECTION_ID;

public interface RsocketConfigProvider {
    static Config rsocketConfig() {
        return configInner(RSOCKET_SECTION_ID);
    }
}
