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

package ru.art.state.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import static ru.art.state.constants.StateModuleConstants.DEFAULT_MODULE_ENDPOINT_CHECK_RATE_SECONDS;
import static ru.art.state.constants.StateModuleConstants.DEFAULT_MODULE_ENDPOINT_LIFE_TIME_MINUTES;

public interface ApplicationStateModuleConfiguration extends ModuleConfiguration {
    long getModuleEndpointCheckRateSeconds();

    long getModuleEndpointLifeTimeMinutes();

    ApplicationStateModuleDefaultConfiguration DEFAULT_CONFIGURATION = new ApplicationStateModuleDefaultConfiguration();

    @Getter
    class ApplicationStateModuleDefaultConfiguration implements ApplicationStateModuleConfiguration {
        private final long moduleEndpointCheckRateSeconds = DEFAULT_MODULE_ENDPOINT_CHECK_RATE_SECONDS;
        private final long moduleEndpointLifeTimeMinutes = DEFAULT_MODULE_ENDPOINT_LIFE_TIME_MINUTES;
    }
}
