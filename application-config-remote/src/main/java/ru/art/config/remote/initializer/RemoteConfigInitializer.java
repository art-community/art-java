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

package ru.art.config.remote.initializer;

import ru.art.config.Config;
import ru.art.config.module.ConfigModule;
import ru.art.config.remote.specification.RemoteConfigServiceSpecification;
import ru.art.configurator.api.specification.ConfiguratorCommunicationSpecification;
import ru.art.core.context.Context;
import ru.art.grpc.client.module.GrpcClientModule;
import ru.art.logging.LoggingModule;
import ru.art.service.ServiceModule;
import static ru.art.config.ConfigProvider.config;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.CONFIGURATOR_CONNECTION_PROPERTIES_NOT_EXISTS;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.LocalConfigKeys.CONFIGURATOR_HOST;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.LocalConfigKeys.CONFIGURATOR_PORT;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.service.ServiceModule.serviceModule;

public interface RemoteConfigInitializer {
    static Context withRemoteConfig(Context context) {
        context.loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new ConfigModule())
                .loadModule(new GrpcClientModule());
        Config applicationConfig = config(EMPTY_STRING);
        if (!applicationConfig.hasPath(CONFIGURATOR_HOST) || !applicationConfig.hasPath(CONFIGURATOR_PORT)) {
            loggingModule().getLogger(RemoteConfigInitializer.class).warn(CONFIGURATOR_CONNECTION_PROPERTIES_NOT_EXISTS);
            return context;
        }
        serviceModule()
                .getServiceRegistry()
                .registerService(new ConfiguratorCommunicationSpecification(applicationConfig.getString(CONFIGURATOR_HOST), applicationConfig.getInt(CONFIGURATOR_PORT)))
                .registerService(new RemoteConfigServiceSpecification());
        return context;
    }
}
