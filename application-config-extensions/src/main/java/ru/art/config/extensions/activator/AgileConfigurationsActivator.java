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

import lombok.experimental.*;
import ru.art.config.extensions.provider.*;
import ru.art.config.remote.provider.*;
import ru.art.core.annotation.*;
import ru.art.core.configuration.ContextInitialConfiguration.*;
import ru.art.core.context.*;
import static java.text.MessageFormat.*;
import static ru.art.config.module.ConfigModule.*;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.*;
import static ru.art.core.constants.ContextConstants.*;
import static ru.art.core.context.Context.*;
import static ru.art.core.wrapper.ExceptionWrapper.*;
import static ru.art.logging.LoggingModule.*;

@PublicApi
@UtilityClass
public class AgileConfigurationsActivator {
    public static Context useAgileConfigurations(String mainModuleId) {
        ApplicationContextConfiguration configuration = new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider());
        Context context = initContext(configuration);
        ignoreException(RemoteConfigProvider::useRemoteConfigurations);
        loggingModule()
                .getLogger(AgileConfigurationsActivator.class)
                .info(format(CONFIGURATION_MODE, configModuleState().configurationMode()));
        return context;
    }

    public static Context useAgileConfigurations() {
        return useAgileConfigurations(DEFAULT_MAIN_MODULE_ID);
    }
}
