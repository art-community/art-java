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
import org.apache.logging.log4j.core.config.*;
import org.apache.logging.log4j.core.config.yaml.*;
import static io.art.core.context.Context.*;
import static io.art.logging.LoggingModuleConstants.*;
import static java.lang.System.*;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static java.util.logging.LogManager.*;
import static lombok.AccessLevel.*;
import static org.apache.logging.log4j.core.LoggerContext.*;
import static org.apache.logging.log4j.core.config.Configurator.*;
import java.io.*;
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

    static {
        getLogManager().reset();
        boolean fromClasspath =
                nonNull(LoggingModule.class.getClassLoader().getResourceAsStream(LOG4J2_YML_FILE)) ||
                        nonNull(LoggingModule.class.getClassLoader().getResourceAsStream(LOG4J2_YAML_FILE));
        boolean fromFile = ofNullable(getProperty(LOG42_CONFIGURATION_FILE_PROPERTY)).map(property -> exists(get((String) property))).orElse(false);
        URL defaultConfiguration;
        boolean fromDefault = nonNull(defaultConfiguration = LoggingModule.class.getClassLoader().getResource(LOG4J2_DEFAULT_YML_FILE));
        if (!fromClasspath && !fromFile && fromDefault) {
            setProperty(LOG42_CONFIGURATION_FILE_PROPERTY, defaultConfiguration.getFile());
        }
    }

    @Override
    public void onLoad() {
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

    public static void main(String[] args) {
        context().loadModule(new LoggingModule());
        logger().info("Test");
    }
}
