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

package ru.art.configurator.factory;

import lombok.experimental.*;
import ru.art.config.remote.api.specification.*;
import ru.art.configurator.api.entity.*;
import ru.art.configurator.provider.ApplicationModulesParametersProvider.*;
import ru.art.entity.*;
import static java.util.Objects.*;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.art.configurator.dao.ConfiguratorDao.*;
import static ru.art.core.checker.CheckerForEmptiness.*;
import java.util.*;

@UtilityClass
public class RemoteConfigCommunicationSpecificationsFactory {
    public static Optional<RemoteConfigCommunicationSpecification> createRemoteConfigCommunicationSpecification(ApplicationModuleParameters parameters, ModuleKey moduleKey) {
        return getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .filter(config -> isNotEmpty(config.findString(GRPC_SERVER_HOST)))
                .filter(config -> nonNull(config.findString(GRPC_SERVER_PORT)))
                .filter(config -> isNotEmpty(config.findString(GRPC_SERVER_PATH)))
                .map(config -> new RemoteConfigCommunicationSpecification(config.findString(GRPC_SERVER_HOST),
                        config.findInt(GRPC_SERVER_PORT),
                        config.findString(GRPC_SERVER_PATH)));
    }
}
