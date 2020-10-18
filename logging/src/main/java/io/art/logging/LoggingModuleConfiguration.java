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
import io.art.core.source.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.logging.LoggingModuleConstants.ConfigurationKeys.*;

@Getter
public class LoggingModuleConfiguration implements ModuleConfiguration {
    private boolean colored = false;
    private boolean asynchronous = false;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<LoggingModuleConfiguration, Configurator> {
        private final LoggingModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            configuration.colored = orElse(let(source.getNested(LOGGING_SECTION), logging -> logging.getBool(COLORED_KEY)), false);
            configuration.asynchronous = orElse(let(source.getNested(LOGGING_SECTION), logging -> logging.getBool(ASYNCHRONOUS_KEY)), false);
            return this;
        }

        @Override
        public Configurator override(LoggingModuleConfiguration configuration) {
            this.configuration.asynchronous = configuration.asynchronous;
            this.configuration.colored = configuration.colored;
            return this;
        }
    }
}
