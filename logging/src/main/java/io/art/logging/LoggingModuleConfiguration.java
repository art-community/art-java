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

package io.art.logging;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.logging.LoggerWriterConfiguration.*;
import io.art.scheduler.executor.deferred.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.constants.DateTimeConstants.*;
import static io.art.logging.LoggingLevel.*;
import static io.art.logging.LoggingModuleConstants.ConfigurationKeys.*;
import static io.art.logging.LoggingWriterType.*;

@Getter
public class LoggingModuleConfiguration implements ModuleConfiguration {
    private Boolean asynchronous = true;
    private LoggerWriterConfiguration defaultWriter = LoggerWriterConfiguration.builder()
            .level(INFO)
            .type(CONSOLE)
            .console(ConsoleWriterConfiguration.builder().colored(true).build())
            .dateTimeFormatter(DD_MM_YYYY_HH_MM_SS_24H_SSS_DASH_FORMAT)
            .build();
    private ImmutableArray<LoggerWriterConfiguration> writers = emptyImmutableArray();
    private final DeferredExecutor executor = new DeferredExecutorBuilder()
            .awaitAllTasksTerminationOnShutdown(true)
            .threadPoolCoreSize(1)
            .shutdownOnExit(true)
            .build();

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<LoggingModuleConfiguration, Configurator> {
        private final LoggingModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            configuration.defaultWriter = orElse(source.getNested(LOGGING_DEFAULT_SECTION, LoggerWriterConfiguration::from), configuration.defaultWriter);
            configuration.writers = source.getNestedArray(LOGGING_WRITERS_SECTION, LoggerWriterConfiguration::from);
            configuration.asynchronous = orElse(source.getNested(LOGGING_SECTION, logging -> logging.getBool(ASYNCHRONOUS_KEY)), configuration.asynchronous);
            return this;
        }

        @Override
        public Configurator configure(LoggingModuleConfiguration configuration) {
            apply(configuration.getAsynchronous(), asynchronous -> this.configuration.asynchronous = asynchronous);
            return this;
        }
    }
}
