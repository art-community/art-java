package ru.adk.config.extensions.logging;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.logging.LoggingConfigKeys.LOGGING_SECTION_ID;

public interface LoggingConfigProvider {
    static Config loggingConfig() {
        return configInner(LOGGING_SECTION_ID);
    }
}
