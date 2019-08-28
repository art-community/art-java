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

package ru.art.config.remote.loader;

import lombok.experimental.UtilityClass;
import ru.art.configurator.api.entity.Configuration;
import ru.art.entity.Entity;
import ru.art.service.ServiceController;

import java.util.Optional;

import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.CONFIGURATION_IS_EMPTY;
import static ru.art.configurator.api.constants.ConfiguratorCommunicationConstants.CONFIGURATOR_COMMUNICATION_SERVICE_ID;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.Methods.GET_PROTOBUF_CONFIG;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.logging.LoggingModule.loggingModule;

@UtilityClass
public class RemoteConfigLoader {
    public static Entity loadRemoteConfig() {
        Optional<Entity> configuration = ServiceController.<Configuration>executeServiceMethod(CONFIGURATOR_COMMUNICATION_SERVICE_ID, GET_PROTOBUF_CONFIG)
                .map(Configuration::getConfiguration);
        if (!configuration.isPresent()) {
            loggingModule().getLogger(RemoteConfigLoader.class).warn(CONFIGURATION_IS_EMPTY);
        }
        return configuration.orElse(entityBuilder().build());
    }
}