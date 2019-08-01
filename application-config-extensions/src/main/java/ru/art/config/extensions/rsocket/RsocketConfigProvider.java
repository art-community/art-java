package ru.art.config.extensions.rsocket;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.rsocket.RsocketConfigKeys.RSOCKET_SECTION_ID;

public interface RsocketConfigProvider {
    static Config rsocketConfig() {
        return configInner(RSOCKET_SECTION_ID);
    }
}
