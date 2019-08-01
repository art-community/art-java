package ru.art.logging;

import lombok.Getter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.art.core.module.ModuleConfiguration;
import ru.art.logging.LoggingModuleConstants.LoggingMode;
import static org.apache.logging.log4j.LogManager.getRootLogger;
import static org.apache.logging.log4j.core.config.Configurator.setAllLevels;
import static ru.art.logging.LoggerConfigurationService.*;
import static ru.art.logging.LoggingModuleConstants.LoggingMode.SOCKET;

public interface LoggingModuleConfiguration extends ModuleConfiguration {
    Logger getLogger();

    Logger getLogger(String topic);

    Logger getLogger(Class<?> topicClass);

    Level getLevel();

    LoggingMode getLoggingMode();

    SocketAppenderConfiguration getSocketAppenderConfiguration();

    @Getter
    class LoggingModuleDefaultConfiguration implements LoggingModuleConfiguration {
        private final Logger logger = LogManager.getLogger(LoggingModuleConfiguration.class);
        private final Level level = loadLoggingLevel();
        private final SocketAppenderConfiguration socketAppenderConfiguration = loadSocketAppenderCurrentConfiguration();
        private final LoggingMode loggingMode = loadLoggingMode();

        @Override
        public Logger getLogger(String topic) {
            return LogManager.getLogger(topic);
        }

        @Override
        public Logger getLogger(Class<?> topicClass) {
            return LogManager.getLogger(topicClass);
        }

        @Override
        public void refresh() {
            setLoggingMode(getLoggingMode());
            if (getLoggingMode() == SOCKET) {
                updateSocketAppender(getSocketAppenderConfiguration());
            }
            setAllLevels(getRootLogger().getName(), getLevel());
        }
    }
}