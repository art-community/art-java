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
import ru.art.configurator.api.entity.*;
import ru.art.entity.Value;
import ru.art.entity.*;
import java.util.*;

import static java.util.Objects.*;
import static java.util.Optional.*;
import static ru.art.configurator.constants.ConfiguratorDbConstants.*;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.art.configurator.dao.ConfiguratorDao.*;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.entity.Value.*;

public interface ApplicationModulesParametersProvider {
    static Optional<ApplicationModuleParameters> getApplicationModuleParameters(ModuleKey moduleKey) {
        Optional<Entity> entityOptional = getConfig(APPLICATION).map(Value::asEntity);
        if (!entityOptional.isPresent()) {
            return empty();
        }
        Entity configuration = entityOptional.get();
        String balancerHost = configuration.getString(APPLICATION_BALANCER_HOST_CONFIG_KEY);
        if (isEmpty(balancerHost)) {
            return empty();
        }
        Integer balancerPort = asPrimitive(configuration.getValue(APPLICATION_BALANCER_PORT_CONFIG_KEY)).parseInt();
        if (isNull(balancerPort)) {
            return empty();
        }
        return getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .map(moduleEntity -> moduleEntity.findString(APPLICATION_MODULE_GRPC_PATH_CONFIG_KEY))
                .map(path -> new ApplicationModuleParameters(balancerHost, balancerPort, path));
    }

    @Getter
    @AllArgsConstructor
    class ApplicationModuleParameters {
        private final String balancerHost;
        private final int balancerPort;
        private final String path;
    }
}