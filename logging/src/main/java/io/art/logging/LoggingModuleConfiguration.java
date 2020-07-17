/*
 * ART
 *
 * Copyright 2020 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.logging;

import lombok.*;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.async.*;
import io.art.core.module.*;
import io.art.logging.LoggingModuleConstants.*;
import static java.lang.System.setProperty;
import static org.apache.logging.log4j.LogManager.getRootLogger;
import static org.apache.logging.log4j.core.config.Configurator.*;
import static org.apache.logging.log4j.core.util.Constants.LOG4J_CONTEXT_SELECTOR;
import static io.art.core.caster.Caster.*;
import static io.art.logging.LoggerConfigurationService.*;
import static io.art.logging.LoggingModuleConstants.LoggingMode.*;
import java.util.*;

@Getter
public class LoggingModuleConfiguration implements ModuleConfiguration {
    @Getter(lazy = true)
    private final Level level = loadLoggingLevel();
    @Getter(lazy = true)
    private final SocketAppenderConfiguration socketAppenderConfiguration = loadSocketAppenderCurrentConfiguration();
    @Getter(lazy = true)
    private final Set<LoggingMode> loggingModes = loadLoggingModes();
    private final boolean enabledColoredLogs = false;
    private final boolean enabledAsynchronousLogging = false;

    public LoggingModuleConfiguration() {
        if (isEnabledAsynchronousLogging()) {
            setProperty(LOG4J_CONTEXT_SELECTOR, AsyncLoggerContextSelector.class.getName());
        }
        setLoggingModes(getLoggingModes());
        if (getLoggingModes().contains(SOCKET)) {
            updateSocketAppender(getSocketAppenderConfiguration());
        }
        setAllLevels(getRootLogger().getName(), getLevel());
    }

    public Logger getLogger() {
        return isEnabledColoredLogs() ? new ColoredLogger(cast(LogManager.getLogger())) : LogManager.getLogger();
    }

    public Logger getLogger(String topic) {
        return isEnabledColoredLogs() ? new ColoredLogger(cast(LogManager.getLogger(topic))) : LogManager.getLogger(topic);
    }

    public Logger getLogger(Class<?> topicClass) {
        return isEnabledColoredLogs() ? new ColoredLogger(cast(LogManager.getLogger(topicClass))) : LogManager.getLogger(topicClass);
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<Configurator> {
        private final LoggingModuleConfiguration configuration;
    }
}
