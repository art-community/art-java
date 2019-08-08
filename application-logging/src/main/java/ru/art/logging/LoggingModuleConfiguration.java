/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
import java.util.Set;

public interface LoggingModuleConfiguration extends ModuleConfiguration {
    Logger getLogger();

    Logger getLogger(String topic);

    Logger getLogger(Class<?> topicClass);

    Level getLevel();

    Set<LoggingMode> getLoggingModes();

    SocketAppenderConfiguration getSocketAppenderConfiguration();

    @Getter
    class LoggingModuleDefaultConfiguration implements LoggingModuleConfiguration {
        private final Logger logger = LogManager.getLogger(LoggingModuleConfiguration.class);
        private final Level level = loadLoggingLevel();
        private final SocketAppenderConfiguration socketAppenderConfiguration = loadSocketAppenderCurrentConfiguration();
        private final Set<LoggingMode> loggingModes = loadLoggingModes();

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
            setLoggingModes(loggingModes);
            if (getLoggingModes().contains(SOCKET)) {
                updateSocketAppender(getSocketAppenderConfiguration());
            }
            setAllLevels(getRootLogger().getName(), getLevel());
        }
    }
}