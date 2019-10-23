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
import ru.art.configurator.api.model.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.art.configurator.dao.ConfiguratorDao.*;
import java.util.*;

public interface ModulesConnectionParametersProvider {
    static List<ModuleConnectionParameters> getModulesConnectionParameters(ModuleKey moduleKey) {
        Optional<Entity> profileConfiguration = getConfig(moduleKey.getProfileId()).filter(Value::isEntity).map(Value::asEntity);
        Optional<Entity> moduleConfiguration = getConfig(moduleKey.formatKey()).filter(Value::isEntity).map(Value::asEntity);
        if (!profileConfiguration.isPresent() || !moduleConfiguration.isPresent()) {
            return emptyList();
        }
        Optional<Set<String>> hosts = profileConfiguration.map(configuration -> configuration.getStringSet(moduleKey.getModuleId()));
        Optional<Integer> port = moduleConfiguration.map(config -> config.getInt(GRPC_SERVER_PORT_KEY));
        Optional<String> path = moduleConfiguration.map(config -> config.getString(GRPC_SERVER_PATH_KEY));
        if (!hosts.isPresent() || !port.isPresent() || !path.isPresent()) {
            return emptyList();
        }
        return hosts.map(instanceConfig -> instanceConfig
                .stream()
                .map(host -> new ModuleConnectionParameters(host, port.get(), path.get()))
                .collect(toList()))
                .orElse(emptyList());
    }

    @Getter
    @AllArgsConstructor
    class ModuleConnectionParameters {
        private final String grpcHost;
        private final int grpcPort;
        private final String grpcPath;
    }
}