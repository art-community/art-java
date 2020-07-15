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

package io.art.config.extensions.activator;

import lombok.experimental.*;
import org.apache.logging.log4j.*;
import io.art.config.extensions.provider.*;
import io.art.core.context.*;
import static java.text.MessageFormat.*;
import static io.art.config.ConfigProvider.*;
import static io.art.config.module.ConfigModule.*;
import static io.art.config.remote.constants.RemoteConfigLoaderConstants.*;
import static io.art.config.remote.provider.RemoteConfigProvider.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.ContextConstants.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModule.*;

@UtilityClass
public class AgileConfigurationsActivator {
    public static Context useAgileConfigurations(String mainModuleId) {
        ApplicationContextConfiguration configuration = new ApplicationContextConfiguration(mainModuleId, new AgileConfigurationProvider());
        Context context = initContext(configuration);
        useRemoteConfigurations();
        Logger logger = loggingModule().getLogger(AgileConfigurationsActivator.class);
        logger.info(format(CONFIGURATION_MODE, configModuleState().configurationMode()));
        switch (configModuleState().configurationMode()) {
            case FILE:
                String configUrl = configUrl();
                if (isEmpty(configUrl)) {
                    logger.warn(CONFIGURATION_FILE_NOT_EXISTS);
                    return context;
                }
                logger.info(format(CONFIGURATION_FILE_URL, configUrl));
                return context;
            case REMOTE:
                logger.info(format(REMOTE_CONFIGURATION_PROPERTIES, configModuleState().remoteConfigProperties()));
                return context;
        }
        return context;
    }

    public static Context useAgileConfigurations() {
        return useAgileConfigurations(DEFAULT_MAIN_MODULE_ID);
    }
}
