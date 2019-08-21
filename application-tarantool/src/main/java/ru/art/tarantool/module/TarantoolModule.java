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

import lombok.*;
import ru.art.core.module.*;
import ru.art.tarantool.configuration.*;
import ru.art.tarantool.configuration.TarantoolModuleConfiguration.*;
import ru.art.tarantool.initializer.*;
import ru.art.tarantool.state.*;

import static java.util.Map.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInitializationMode.*;

@Getter
public class TarantoolModule implements Module<TarantoolModuleConfiguration, TarantoolModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static TarantoolModuleConfiguration tarantoolModule = context().getModule(TARANTOOL_MODULE_ID, TarantoolModule::new);
    @Getter(lazy = true, value = PRIVATE)
    private final static TarantoolModuleState tarantoolModuleState = context().getModuleState(TARANTOOL_MODULE_ID, TarantoolModule::new);
    private final String id = TARANTOOL_MODULE_ID;
    private final TarantoolModuleConfiguration defaultConfiguration = TarantoolModuleDefaultConfiguration.DEFAULT_CONFIGURATION;
    private final TarantoolModuleState state = new TarantoolModuleState();

    @Override
    public void onLoad() {
        if (tarantoolModule().getInitializationMode() != ON_MODULE_LOAD) {
            return;
        }
        tarantoolModule().getTarantoolConfigurations()
                .entrySet()
                .stream()
                .filter(entry -> nonNull(entry) && nonNull(entry.getKey()) && nonNull(entry.getValue()))
                .map(Entry::getKey)
                .forEach(TarantoolInitializer::initializeTarantool);
    }

    public static TarantoolModuleConfiguration tarantoolModule() {
        if (insideDefaultContext()) {
            return TarantoolModuleConfiguration.DEFAULT_CONFIGURATION;
        }
        return getTarantoolModule();
    }

    public static TarantoolModuleState tarantoolModuleState() {
        return getTarantoolModuleState();
    }
}
