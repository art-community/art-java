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

package io.art.logging;

import io.art.core.module.*;
import lombok.*;
import org.apache.logging.log4j.Logger;
import reactor.core.publisher.*;
import reactor.util.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModuleConstants.*;
import static io.art.logging.LoggingModuleConstants.LoggingMessages.*;
import static java.lang.System.*;
import static java.nio.file.Paths.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static java.util.logging.LogManager.*;
import static lombok.AccessLevel.*;
import static reactor.util.Loggers.*;
import java.net.*;

@Getter
public class LoggingModule implements StatelessModule<LoggingModuleConfiguration, LoggingModuleConfiguration.Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<LoggingModuleConfiguration> loggingModule = context().getStatelessModule(LoggingModule.class.getSimpleName());
    private final String id = LoggingModule.class.getSimpleName();
    private final LoggingModuleConfiguration configuration = new LoggingModuleConfiguration();
    private final LoggingModuleConfiguration.Configurator configurator = new LoggingModuleConfiguration.Configurator(configuration);

    public static StatelessModuleProxy<LoggingModuleConfiguration> loggingModule() {
        return getLoggingModule();
    }

    @Override
    public void onLoad() {
        getLogManager().reset();

        boolean fromFile = ofNullable(getProperty(LOG42_CONFIGURATION_FILE_PROPERTY))
                .map(property -> get(property))
                .map(path -> path.toFile().exists())
                .orElse(false);
        if (fromFile) {
            logger(LoggingModule.class).info(format(CONFIGURE_FROM_FILE, getProperty(LOG42_CONFIGURATION_FILE_PROPERTY)));
            return;
        }

        ClassLoader loader = LoggingModule.class.getClassLoader();
        URL source;
        boolean fromClasspath = nonNull(source = loader.getResource(LOG4J2_YML_FILE)) || nonNull(source = loader.getResource(LOG4J2_YAML_FILE));
        if (fromClasspath) {
            logger(LoggingModule.class).info(format(CONFIGURE_FROM_CLASSPATH, source.getFile()));
        }

        useCustomLoggers(name -> new ReactorLogger(logger(name)));
    }

    public static Logger logger() {
        return loggingModule().configuration().getLogger();
    }

    public static Logger logger(String topic) {
        return loggingModule().configuration().getLogger(topic);
    }

    public static Logger logger(Class<?> topicClass) {
        return loggingModule().configuration().getLogger(topicClass);
    }
}
