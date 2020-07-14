/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.json.module;

import lombok.*;
import io.art.core.module.Module;
import io.art.core.module.*;
import io.art.json.configuration.*;
import static lombok.AccessLevel.*;
import static io.art.core.context.Context.*;
import static io.art.json.configuration.JsonModuleConfiguration.*;
import static io.art.json.constants.JsonModuleConstants.*;

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
