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

import io.art.configurator.module.*;
import io.art.core.module.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.model.communicator.*;
import io.art.model.configurator.*;
import io.art.model.module.*;
import io.art.server.module.*;
import io.art.xml.module.*;
import lombok.experimental.*;
import static io.art.configurator.module.ConfiguratorModule.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.context.Context.*;
import static io.art.model.module.ModuleModel.*;
import static java.util.Optional.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@UtilityClass
public class ModuleLauncher {
    private final static AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (launched.compareAndSet(false, true)) {
            context().loadModule(new ConfiguratorModule());
            List<ModuleConfigurationSource> sources = configuratorModule().configuration().orderedSources();
            ConfiguratorModel configuratorModel = model.getConfiguratorModel();
            logging(sources, configuratorModel);
            json(sources);
            xml(sources);
            server(sources);
        }
    }

    private void logging(List<ModuleConfigurationSource> sources, ConfiguratorModel configuratorModel) {
        LoggingModule logging = cast(new LoggingModule().configure(configurator -> configurator.from(sources)));
        ofNullable(configuratorModel.getLoggingConfigurator())
                .ifPresent(model -> logging.configure(configurator -> configurator.from(model.getConfiguration())));
        context().loadModule(logging);
    }

    private void json(List<ModuleConfigurationSource> sources) {
        context().loadModule(new JsonModule().configure(configurator -> configurator.from(sources)));
    }

    private void xml(List<ModuleConfigurationSource> sources) {
        context().loadModule(new XmlModule().configure(configurator -> configurator.from(sources)));
    }

    private void server(List<ModuleConfigurationSource> sources) {
        context().loadModule(new ServerModule().configure(configurator -> configurator.from(sources)));
    }

    public static void main(String[] args) {
        launch(module()
                .communicate(communicator -> communicator.grpc("test", GrpcCommunicatorModel.class))
                .configure(configurator -> configurator.logging(logging -> logging.colored().asynchronous())));
    }
}
