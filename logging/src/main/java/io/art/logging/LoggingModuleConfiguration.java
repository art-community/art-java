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

import io.art.core.module.*;
import lombok.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.*;
import org.apache.logging.log4j.core.async.*;
import static io.art.core.caster.Caster.*;
import static io.art.logging.LoggingModuleConstants.ConfigurationKeys.*;
import static java.lang.System.*;
import static org.apache.logging.log4j.core.util.Constants.*;

@Getter
public class LoggingModuleConfiguration implements ModuleConfiguration {
    private boolean colored = true;
    private boolean asynchronous = false;

    public LoggingModuleConfiguration() {
        if (isAsynchronous()) {
            setProperty(LOG4J_CONTEXT_SELECTOR, AsyncLoggerContextSelector.class.getName());
        }
    }

    public Logger getLogger() {
        return isColored() ? new ColoredLogger(cast(LogManager.getLogger())) : LogManager.getLogger();
    }

    public Logger getLogger(String topic) {
        return isColored() ? new ColoredLogger(cast(LogManager.getLogger(topic))) : LogManager.getLogger(topic);
    }

    public Logger getLogger(Class<?> topicClass) {
        return isColored() ? new ColoredLogger(cast(LogManager.getLogger(topicClass))) : LogManager.getLogger(topicClass);
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<Configurator> {
        private final LoggingModuleConfiguration configuration;

        @Override
        public Configurator from(ModuleConfigurationSource source) {
            configuration.colored = source.getBool(COLORED_KEY);
            configuration.asynchronous = source.getBool(ASYNCHRONOUS_KEY);
            return null;
        }
    }
}
