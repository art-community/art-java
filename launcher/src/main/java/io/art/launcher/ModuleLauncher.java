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

package io.art.launcher;

import io.art.configuration.module.*;
import io.art.core.module.*;
import io.art.json.module.*;
import io.art.model.module.*;
import static io.art.configuration.module.ConfiguratorModule.*;
import static io.art.core.context.Context.*;
import java.util.*;
import java.util.concurrent.atomic.*;

public class ModuleLauncher {
    private final static AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (launched.compareAndSet(false, true)) {
            context().loadModule(new ConfiguratorModule());
            List<ModuleConfigurationSource> sources = configuratorModule().configuration().orderedSources();
            context()
                    .loadModule(new JsonModule().configure(configurator -> configurator.from(sources)));
        }
    }
}
