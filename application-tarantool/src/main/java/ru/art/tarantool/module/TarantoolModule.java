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

package ru.art.tarantool.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.tarantool.configuration.TarantoolConfiguration;
import ru.art.tarantool.configuration.TarantoolModuleConfiguration;
import ru.art.tarantool.exception.TarantoolConnectionException;
import ru.art.tarantool.state.TarantoolModuleState;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.core.context.Context.contextIsNotReady;
import static ru.art.tarantool.configuration.TarantoolModuleConfiguration.DEFAULT_CONFIGURATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TARANTOOL_MODULE_ID;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInitializationMode.ON_MODULE_LOAD;
import static ru.art.tarantool.initializer.TarantoolInitializer.initializeTarantools;
import java.util.Map;

@Getter
public class TarantoolModule implements Module<TarantoolModuleConfiguration, TarantoolModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static TarantoolModuleConfiguration tarantoolModule = context().getModule(TARANTOOL_MODULE_ID, TarantoolModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private final static TarantoolModuleState tarantoolModuleState = context().getModuleState(TARANTOOL_MODULE_ID, TarantoolModule::new);
    private final String id = TARANTOOL_MODULE_ID;
    private final TarantoolModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;
    private final TarantoolModuleState state = new TarantoolModuleState();

    @Override
    public void onLoad() {
        if (tarantoolModule().getInitializationMode() != ON_MODULE_LOAD) {
            return;
        }
        initializeTarantools();
    }

    public static TarantoolModuleConfiguration tarantoolModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getTarantoolModule();
    }

    public static TarantoolModuleState tarantoolModuleState() {
        return getTarantoolModuleState();
    }


    public static TarantoolConfiguration getTarantoolConfiguration(String instanceId, Map<String, TarantoolConfiguration> configurations) {
        TarantoolConfiguration configuration = configurations.get(instanceId);
        if (isNull(configuration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        return configuration;
    }
}
