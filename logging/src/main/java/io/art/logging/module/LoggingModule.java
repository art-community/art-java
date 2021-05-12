/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import io.art.logging.configuration.*;
import io.art.logging.logger.*;
import io.art.logging.manager.*;
import io.art.logging.reactor.*;
import io.art.logging.state.*;
import io.art.scheduler.manager.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.*;
import static java.util.logging.LogManager.*;
import static lombok.AccessLevel.*;
import static reactor.util.Loggers.*;
import java.time.*;

@Getter
public class LoggingModule implements StatefulModule<LoggingModuleConfiguration, LoggingModuleConfiguration.Configurator, LoggingModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<LoggingModuleConfiguration, LoggingModuleState> loggingModule = context().getStatefulModule(LoggingModule.class.getSimpleName());
    private final String id = LoggingModule.class.getSimpleName();
    private final LoggingModuleConfiguration configuration = new LoggingModuleConfiguration();
    private final LoggingModuleConfiguration.Configurator configurator = new LoggingModuleConfiguration.Configurator(configuration);
    private final LoggingModuleState state = new LoggingModuleState();
    private final LoggingManager manager = new LoggingManager(configuration, state);
    private static final LoggingManager DEFAULT_MANAGER;

    static {
        registerDefault(LoggingModule.class.getSimpleName(), LoggingModule::new);
        DEFAULT_MANAGER = new LoggingManager(getLoggingModule().configuration(), getLoggingModule().state());
        DEFAULT_MANAGER.activate();
    }

    public static StatefulModuleProxy<LoggingModuleConfiguration, LoggingModuleState> loggingModule() {
        return getLoggingModule();
    }

    @Override
    public void onLoad(Context.Service contextService) {
        getLogManager().reset();
        useCustomLoggers(name -> new ReactorLogger(logger(name)));
        DEFAULT_MANAGER.deactivate();
        manager.activate();
    }

    @Override
    public void afterReload(Context.Service contextService) {
        getLogManager().reset();
    }

    @Override
    public void onUnload(Context.Service contextService) {
        manager.deactivate();
    }

    public static Logger logger() {
        return logger(LoggingModule.class);
    }

    public static Logger logger(Class<?> topicClass) {
        return logger(topicClass.getName());
    }

    public static Logger logger(String topic) {
        LoggingModuleConfiguration configuration = loggingModule().configuration();
        LoggingModuleState state = loggingModule().state();
        LoggerConfiguration loggerConfiguration = configuration
                .getLoggers()
                .getOrDefault(topic, configuration.getDefaultLogger().toLoggerConfiguration());
        return new LoggerImplementation(topic, loggerConfiguration, state);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            logger(LoggingModule.class).info("test:  " + i);
        }
        SchedulersManager.scheduleDelayed(() -> logger(LoggingModule.class).info("test:  "), Duration.ofSeconds(1));
        block();
    }
}
