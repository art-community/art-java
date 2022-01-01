/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.logging.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.logging.*;
import io.art.logging.configuration.*;
import io.art.logging.manager.*;
import io.art.logging.reactor.*;
import io.art.logging.state.*;
import lombok.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.logging.netty.NettyLoggerFactory.*;
import static io.netty.util.internal.logging.InternalLoggerFactory.*;
import static java.util.logging.LogManager.*;
import static reactor.util.Loggers.*;

@Getter
public class LoggingModule implements StatefulModule<LoggingModuleConfiguration, LoggingModuleConfiguration.Configurator, LoggingModuleState> {
    private static final LazyProperty<StatefulModuleProxy<LoggingModuleConfiguration, LoggingModuleState>> loggingModule = lazy(() -> context().getStatefulModule(LOGGING_MODULE_ID));
    private final String id = LOGGING_MODULE_ID;
    private final LoggingModuleConfiguration configuration = new LoggingModuleConfiguration();
    private final LoggingModuleConfiguration.Configurator configurator = new LoggingModuleConfiguration.Configurator(configuration);
    private final LoggingManager manager = new LoggingManager(configuration);
    private final LoggingModuleState state = new LoggingModuleState(manager);

    @Override
    public void launch(Context.Service contextService) {
        getLogManager().reset();
        setDefaultFactory(defaultNettyLoggerFactory());
        graalNettyLoggerFactory().dispose();
        useCustomLoggers(name -> new ReactorLogger(Logging.logger(name)));
        manager.activate();
    }

    @Override
    public void afterReload(Context.Service contextService) {
        getLogManager().reset();
    }

    @Override
    public void unload(Context.Service contextService) {
        manager.deactivate();
    }

    public static StatefulModuleProxy<LoggingModuleConfiguration, LoggingModuleState> loggingModule() {
        return loggingModule.get();
    }
}
