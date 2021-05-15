/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.logging.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.logging.constants.LoggingLevel.*;
import static io.art.logging.constants.LoggingModuleConstants.*;
import static io.art.logging.constants.LoggingModuleConstants.ConfigurationKeys.*;
import static io.art.logging.constants.LoggingModuleConstants.Defaults.*;
import static io.art.logging.constants.LoggingWriterType.*;
import static java.util.concurrent.Executors.*;
import java.util.concurrent.*;

@Getter
public class LoggingModuleConfiguration implements ModuleConfiguration {
    private ImmutableMap<String, LoggerConfiguration> loggers = emptyImmutableMap();

    private DefaultLoggerConfiguration defaultLogger = DefaultLoggerConfiguration.builder()
            .level(INFO)
            .writer(LoggerWriterConfiguration.builder()
                    .type(CONSOLE)
                    .console(ConsoleWriterConfiguration.builder().build())
                    .dateTimeFormatter(DEFAULT_LOG_DATE_TIME_FORMAT)
                    .build())
            .build();

    private final ExecutorService consumingExecutor = newSingleThreadExecutor(runnable -> newDaemon(CONSUMER_THREAD, runnable));

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<LoggingModuleConfiguration, Configurator> {
        private final LoggingModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            configuration.defaultLogger = orElse(source.getNested(LOGGING_DEFAULT_SECTION, DefaultLoggerConfiguration::from), configuration.defaultLogger);
            configuration.loggers = source.getNestedMap(LOGGING_LOGGERS_SECTION, LoggerConfiguration::from);
            return this;
        }

        @Override
        public Configurator initialize(LoggingModuleConfiguration configuration) {
            return this;
        }
    }
}
