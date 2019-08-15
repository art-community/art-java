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

package ru.art.config.extensions.activator;

import lombok.experimental.UtilityClass;
import ru.art.config.extensions.provider.AgileConfigurationProvider;
import ru.art.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import ru.art.core.context.Context;
import static ru.art.config.remote.provider.RemoteConfigProvider.useRemoteConfigurations;
import static ru.art.core.constants.ContextConstants.DEFAULT_MAIN_MODULE_ID;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.core.context.Context.initContext;

@UtilityClass
public class AgileConfigurationsActivator {
    public static Context useAgileConfigurations(String mainModuleId) {
        return initContext(new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider()));
    }

    public static Context useAgileConfigurations() {
        return useAgileConfigurations(DEFAULT_MAIN_MODULE_ID);
    }

    public static Context useRemoteAgileConfigurations(String mainModuleId) {
        Context context = initContext(new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider()));
        useRemoteConfigurations(contextConfiguration());
        return context;
    }

    public static Context useRemoteAgileConfigurations() {
        return useRemoteAgileConfigurations(DEFAULT_MAIN_MODULE_ID);
    }
}
