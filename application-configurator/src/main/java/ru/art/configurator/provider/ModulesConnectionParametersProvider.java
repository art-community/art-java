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

package ru.art.configurator.provider;

import lombok.*;
import ru.art.configurator.api.model.ModuleKey;
import ru.art.entity.Value;
import ru.art.entity.*;
import static java.util.Optional.*;
import static ru.art.configurator.constants.ConfiguratorDbConstants.*;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.art.configurator.dao.ConfiguratorDao.*;
import java.util.*;

public interface ModulesConnectionParametersProvider {
    static Optional<ModuleConnectionParameters> getModulesConnectionParameters(ModuleKey moduleKey) {
        Optional<Entity> applicationConfiguration = getConfig(APPLICATION).filter(Value::isEntity).map(Value::asEntity);
        Optional<Entity> moduleConfiguration = getConfig(moduleKey.formatKey()).filter(Value::isEntity).map(Value::asEntity);
        if (applicationConfiguration.isPresent()) {
            Optional<String> host = applicationConfiguration.map(configuration -> configuration.findString(BALANCER_HOST_KEY));
            Optional<Integer> port = applicationConfiguration.map(configuration -> configuration.findInt(BALANCER_POT_KEY));
            if (host.isPresent() && port.isPresent()) {
                return moduleConfiguration
                        .map(configuration -> configuration.findString(GRPC_SERVER_PATH_KEY))
                        .map(modulePath -> new ModuleConnectionParameters(host.get(), port.get(), modulePath));
            }
            return empty();
        }
        Optional<String> host = moduleConfiguration.map(configuration -> configuration.findString(GRPC_SERVER_HOST_KEY));
        Optional<Integer> port = moduleConfiguration.map(configuration -> configuration.findInt(GRPC_SERVER_PORT_KEY));
        if (host.isPresent() && port.isPresent()) {
            return moduleConfiguration
                    .map(configuration -> configuration.getString(GRPC_SERVER_PATH_KEY))
                    .map(modulePath -> new ModuleConnectionParameters(host.get(), port.get(), modulePath));
        }
        return empty();
    }

    @Getter
    @AllArgsConstructor
    class ModuleConnectionParameters {
        private final String grpcHost;
        private final int grpcPort;
        private final String grpcPath;
    }
}