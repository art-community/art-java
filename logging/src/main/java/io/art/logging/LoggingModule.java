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

package io.art.logging;

import io.art.core.context.*;
import io.art.core.module.*;
import lombok.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.ThreadExtensions.block;
import static java.util.logging.LogManager.*;
import static lombok.AccessLevel.*;
import static reactor.util.Loggers.*;

@Getter
public class LoggingModule implements StatelessModule<LoggingModuleConfiguration, LoggingModuleConfiguration.Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<LoggingModuleConfiguration> loggingModule = context().getStatelessModule(LoggingModule.class.getSimpleName());
    private final String id = LoggingModule.class.getSimpleName();
    private final LoggingModuleConfiguration configuration = new LoggingModuleConfiguration();
    private final LoggingModuleConfiguration.Configurator configurator = new LoggingModuleConfiguration.Configurator(configuration);

    static {
        registerDefault(LoggingModule.class.getSimpleName(), LoggingModule::new);
    }

    public static StatelessModuleProxy<LoggingModuleConfiguration> loggingModule() {
        return getLoggingModule();
    }

    @Override
    public void onLoad(Context.Service contextService) {
        getLogManager().reset();
        useCustomLoggers(name -> new ReactorLogger(currentLogger(name)));
    }

    @Override
    public void afterReload(Context.Service contextService) {
        getLogManager().reset();
    }

    @Override
    public void onUnload(Context.Service contextService) {
        configuration.getExecutor().shutdown();
    }

    public Logger currentLogger() {
        return currentLogger(LoggingModule.class.getName());
    }

    public Logger currentLogger(String topic) {
        return new ConfiguredLogger(topic, configuration);
    }

    public static Logger logger() {
        return logger(LoggingModule.class);
    }

    public static Logger logger(Class<?> topicClass) {
        return logger(topicClass.getName());
    }

    public static Logger logger(String topic) {
        return new ConfiguredLogger(topic, loggingModule().configuration());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            logger(LoggingModule.class).info("test:  " + i);
        }
        block();
    }
}
