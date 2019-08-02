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

package ru.art.config.extensions.activator;

import ru.art.config.extensions.provider.AgileConfigurationProvider;
import ru.art.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import ru.art.core.context.Context;
import static ru.art.core.constants.ContextConstants.DEFAULT_MAIN_MODULE_ID;
import static ru.art.core.context.Context.initContext;

public interface AgileConfigurationsActivator {
    static Context useAgileConfigurations(String mainModuleId) {
        return initContext(new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider()));
    }

    static Context useAgileConfigurations() {
        return initContext(new ApplicationContextConfiguration(DEFAULT_MAIN_MODULE_ID, new AgileConfigurationProvider()));
    }
}
