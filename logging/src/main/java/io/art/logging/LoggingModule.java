/*
 * ART Java
 *
 * Copyright 2019 ART
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
import io.art.core.module.Module;
import io.art.core.module.*;
import static java.util.logging.LogManager.*;
import static lombok.AccessLevel.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModuleConfiguration.*;
import static io.art.logging.LoggingModuleConstants.*;

@Getter
public class LoggingModule implements Module<LoggingModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final LoggingModuleConfiguration loggingModule = context().getModule(LOGGING_MODULE_ID, LoggingModule::new);
    private final String id = LOGGING_MODULE_ID;
    private final LoggingModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;

    static {
        getLogManager().reset();
    }

    public static LoggingModuleConfiguration loggingModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getLoggingModule();
    }
}
