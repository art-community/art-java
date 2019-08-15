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

package ru.art.config.module;

import lombok.Getter;
import ru.art.config.configuration.ConfigModuleConfiguration;
import ru.art.config.configuration.ConfigModuleConfiguration.ConfigModuleDefaultConfiguration;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import static ru.art.config.constants.ConfigModuleConstants.CONFIG_MODULE_ID;
import static ru.art.core.context.Context.context;

@Getter
public class ConfigModule implements Module<ConfigModuleConfiguration, ModuleState> {
    @Getter(lazy = true)
    private static final ConfigModuleConfiguration configModule = context().getModule(CONFIG_MODULE_ID, ConfigModule::new);
    private final ConfigModuleConfiguration defaultConfiguration = new ConfigModuleDefaultConfiguration();
    private final String id = CONFIG_MODULE_ID;

    public static ConfigModuleConfiguration configModule() {
        return context().getModule(CONFIG_MODULE_ID, ConfigModule::new);
    }
}
