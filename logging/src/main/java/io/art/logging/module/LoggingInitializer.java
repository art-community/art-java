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

package io.art.logging.module;

import io.art.core.annotation.*;
import io.art.core.module.*;
import io.art.logging.configuration.*;
import io.art.logging.configuration.DefaultLoggerConfiguration.*;
import io.art.logging.configuration.LoggerWriterConfiguration.*;
import lombok.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@UsedByGenerator
public class LoggingInitializer implements ModuleInitializer<LoggingModuleConfiguration, LoggingModuleConfiguration.Configurator, LoggingModule> {
    private final Initial configuration = new Initial();

    public LoggingInitializer forDefault(UnaryOperator<DefaultLoggerConfigurationBuilder> operator) {
        configuration.defaultOperator = operator;
        return this;
    }

    public LoggingInitializer forDefaultWriter(UnaryOperator<LoggerWriterConfigurationBuilder> operator) {
        configuration.defaultWriterOperator = operator;
        return this;
    }

    public LoggingInitializer colored() {
        configuration.defaultWriterOperator = writer -> writer.console(ConsoleWriterConfiguration.builder().colored(true).build());
        return this;
    }

    @Override
    public LoggingModuleConfiguration initialize(LoggingModule module) {
        return configuration;
    }

    @Getter
    private static class Initial extends LoggingModuleConfiguration {
        private UnaryOperator<DefaultLoggerConfigurationBuilder> defaultOperator = identity();
        private UnaryOperator<LoggerWriterConfigurationBuilder> defaultWriterOperator = identity();

        @Override
        public DefaultLoggerConfiguration getDefaultLogger() {
            DefaultLoggerConfiguration loggerConfiguration = defaultOperator.apply(super.getDefaultLogger().toBuilder()).build();
            return loggerConfiguration.toBuilder().writer(defaultWriterOperator.apply(loggerConfiguration.getWriter().toBuilder()).build()).build();
        }
    }
}
