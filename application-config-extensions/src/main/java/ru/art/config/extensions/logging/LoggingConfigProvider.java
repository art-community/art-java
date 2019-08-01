package ru.art.config.extensions.logging;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.logging.LoggingConfigKeys.LOGGING_SECTION_ID;

public interface LoggingConfigProvider {
    static Config loggingConfig() {
        return configInner(LOGGING_SECTION_ID);
    }
}
