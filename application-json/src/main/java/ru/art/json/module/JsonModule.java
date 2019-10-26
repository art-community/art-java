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

package ru.art.json.module;

import lombok.*;
import ru.art.core.module.Module;
import ru.art.core.module.*;
import ru.art.json.configuration.*;
import static lombok.AccessLevel.*;
import static ru.art.core.context.Context.*;
import static ru.art.json.configuration.JsonModuleConfiguration.*;
import static ru.art.json.constants.JsonModuleConstants.*;

@Getter
public class JsonModule implements Module<JsonModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private final static JsonModuleConfiguration jsonModule = context().getModule(JSON_MODULE_ID, JsonModule::new);
    private final String id = JSON_MODULE_ID;
    private final JsonModuleConfiguration defaultConfiguration = DEFAULT_CONFIGURATION;

    public static JsonModuleConfiguration jsonModule() {
        if (contextIsNotReady()) {
            return DEFAULT_CONFIGURATION;
        }
        return getJsonModule();
    }
}
