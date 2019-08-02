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

package ru.art.json.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.json.configuration.JsonModuleConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.json.configuration.JsonModuleConfiguration.JsonModuleDefaultConfiguration;
import static ru.art.json.constants.JsonModuleConstants.JSON_MODULE_ID;

@Getter
public class JsonModule implements Module<JsonModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static JsonModuleConfiguration jsonModule = context().getModule(JSON_MODULE_ID, new JsonModule());
    @Getter
    private final JsonModuleConfiguration defaultConfiguration = new JsonModuleDefaultConfiguration();
    private final String id = JSON_MODULE_ID;

    public static JsonModuleConfiguration jsonModule() {
        return getJsonModule();
    }
}
