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

import com.google.common.collect.*;
import io.art.communicator.module.*;
import io.art.configurator.module.*;
import io.art.core.configuration.ContextConfiguration.*;
import io.art.core.context.*;
import io.art.core.lazy.*;
import io.art.core.module.Module;
import io.art.core.source.*;
import io.art.json.module.*;
import io.art.logging.*;
import io.art.model.configurator.*;
import io.art.model.module.*;
import io.art.rsocket.module.*;
import io.art.server.module.*;
import io.art.xml.module.*;
import lombok.experimental.*;
import org.apache.logging.log4j.*;
import static io.art.core.colorizer.AnsiColorizer.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.launcher.ModelImplementor.*;
import static io.art.launcher.ModuleLauncherConstants.LAUNCHED_MESSAGES;
import static io.art.logging.LoggingModule.*;
import static io.art.model.module.ModuleModel.*;
import static java.util.Optional.*;
import java.util.concurrent.atomic.*;

@UtilityClass
public class ModuleLauncher {
    private final static AtomicBoolean launched = new AtomicBoolean(false);

    public static void launch(ModuleModel model) {
        if (launched.compareAndSet(false, true)) {
            ConfiguratorModule configurator = new ConfiguratorModule();
            ImmutableList<ConfigurationSource> sources = configurator
                    .loadConfigurations()
                    .configuration()
                    .orderedSources();
            ConfiguratorModel configuratorModel = model.getConfiguratorModel();
            ImmutableList.Builder<Module> modules = ImmutableList.builder();
            modules.add(
                    configurator,
                    logging(sources, configuratorModel),
                    json(sources),
                    xml(sources),
                    server(sources),
                    communicator(sources),
                    rsocket(sources)
            );
            LazyValue<Logger> logger = lazy(() -> logger(Context.class));
            initialize(new DefaultContextConfiguration(), modules.build(), message -> logger.get().info(message));
            implement(model);
            LAUNCHED_MESSAGES.forEach(message -> logger.get().info(success(message)));
            block();
        }
    }

    private LoggingModule logging(ImmutableList<ConfigurationSource> sources, ConfiguratorModel configuratorModel) {
        LoggingModule logging = new LoggingModule();
        logging.configure(configurator -> configurator.from(sources));
        ofNullable(configuratorModel.getLoggingConfigurator())
                .ifPresent(model -> logging.configure(configurator -> configurator.override(model.getConfiguration())));
        return logging;
    }

    private JsonModule json(ImmutableList<ConfigurationSource> sources) {
        JsonModule json = new JsonModule();
        json.configure(configurator -> configurator.from(sources));
        return json;
    }

    private XmlModule xml(ImmutableList<ConfigurationSource> sources) {
        XmlModule xml = new XmlModule();
        xml.configure(configurator -> configurator.from(sources));
        return xml;
    }

    private ServerModule server(ImmutableList<ConfigurationSource> sources) {
        ServerModule server = new ServerModule();
        server.configure(configurator -> configurator.from(sources));
        return server;
    }

    private CommunicatorModule communicator(ImmutableList<ConfigurationSource> sources) {
        CommunicatorModule communicator = new CommunicatorModule();
        communicator.configure(configurator -> configurator.from(sources));
        return communicator;
    }

    private RsocketModule rsocket(ImmutableList<ConfigurationSource> sources) {
        RsocketModule rsocket = new RsocketModule();
        rsocket.configure(configurator -> configurator.from(sources));
        return rsocket;
    }

    public static void main(String[] args) {
        launch(module()
                .serve(server -> server
                        .rsocket("example", "method")
                )
        );
    }
}
