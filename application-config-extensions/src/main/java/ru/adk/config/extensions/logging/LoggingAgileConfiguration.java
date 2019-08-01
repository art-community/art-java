package ru.adk.config.extensions.logging;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import ru.adk.logging.LoggingModuleConfiguration.LoggingModuleDefaultConfiguration;
import static ru.adk.config.extensions.ConfigExtensions.configString;
import static ru.adk.config.extensions.logging.LoggingConfigKeys.LEVEL;
import static ru.adk.config.extensions.logging.LoggingConfigKeys.LOGGING_SECTION_ID;
import static ru.adk.core.extension.ExceptionExtensions.ifException;

@Getter
public class LoggingAgileConfiguration extends LoggingModuleDefaultConfiguration {
    private Level level;

    public LoggingAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        level = ifException(() -> Level.getLevel(configString(LOGGING_SECTION_ID, LEVEL).toUpperCase()), super.getLevel());
        super.refresh();
    }
}
