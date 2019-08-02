/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.SocketAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import static java.lang.Integer.parseInt;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.Level.INFO;
import static org.apache.logging.log4j.LogManager.getContext;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.extension.NullCheckingExtensions.getOrElse;
import static ru.art.logging.LoggingModuleConstants.*;
import static ru.art.logging.LoggingModuleConstants.LoggingMode.CONSOLE;
import static ru.art.logging.LoggingModuleConstants.LoggingMode.SOCKET;
import java.util.List;
import java.util.Map;

public interface LoggerConfigurationService {
    static void updateSocketAppender(SocketAppenderConfiguration socketAppenderConfiguration) {
        LoggerContext context = (LoggerContext) getContext(false);
        Configuration configuration = context.getConfiguration();
        if (isNull(configuration)) return;
        SocketAppender socketAppender = configuration.getAppender(SocketAppender.class.getSimpleName());
        context.getRootLogger().removeAppender(socketAppender);
        SocketAppender appender = SocketAppender
                .newBuilder()
                .setName(SocketAppender.class.getSimpleName())
                .withHost(socketAppenderConfiguration.getHost())
                .withPort(socketAppenderConfiguration.getPort())
                .setLayout(socketAppenderConfiguration.getLayout())
                .build();
        appender.start();
        context.getRootLogger().addAppender(appender);
        context.updateLoggers();
    }

    static SocketAppenderConfiguration loadSocketAppenderCurrentConfiguration() {
        LoggerContext context = (LoggerContext) getContext(false);
        Configuration configuration = context.getConfiguration();
        if (isNull(configuration)) return SocketAppenderConfiguration.builder().build();
        SocketAppender socketAppender = configuration.getAppender(SocketAppender.class.getSimpleName());
        if (isNull(socketAppender)) return SocketAppenderConfiguration.builder().build();
        Map<String, String> contentFormat = socketAppender.getManager().getContentFormat();
        String host = contentFormat.get(ADDRESS);
        int port = parseInt(contentFormat.get(PORT));
        return SocketAppenderConfiguration.builder()
                .host(host)
                .port(port)
                .layout(socketAppender.getLayout())
                .build();
    }

    static ConsoleAppenderConfiguration loadConsoleAppenderConfiguration() {
        LoggerContext context = (LoggerContext) getContext(false);
        Configuration configuration = context.getConfiguration();
        if (isNull(configuration)) return ConsoleAppenderConfiguration.builder().build();
        ConsoleAppender consoleAppender = configuration.getAppender(ConsoleAppender.class.getSimpleName());
        if (isNull(consoleAppender)) return ConsoleAppenderConfiguration.builder().build();
        Layout<?> layout = consoleAppender.getLayout();
        return ConsoleAppenderConfiguration.builder().patternLayout(layout).build();
    }

    static LoggingMode loadLoggingMode() {
        LoggerConfig rootLogger;
        if (isNull(rootLogger = getRootLogger())) return CONSOLE;
        List<AppenderRef> appenderRefs = rootLogger.getAppenderRefs();
        if (isEmpty(appenderRefs)) return CONSOLE;
        AppenderRef appenderRef = appenderRefs.get(0);
        if (isNull(appenderRef)) return CONSOLE;
        String ref = appenderRef.getRef();
        if (isEmpty(ref)) return CONSOLE;
        return ref.equalsIgnoreCase(SocketAppender.class.getSimpleName()) ? SOCKET : CONSOLE;
    }

    static Level loadLoggingLevel() {
        LoggerConfig rootLogger;
        if (isNull(rootLogger = getRootLogger()) || isEmpty(rootLogger.getName())) return INFO;
        return getOrElse(rootLogger.getLevel(), INFO);
    }

    static SocketAppender createLoadedSocketAppender() {
        SocketAppenderConfiguration socketAppenderConfiguration = loadSocketAppenderCurrentConfiguration();
        String host = socketAppenderConfiguration.getHost();
        if (isEmpty(host)) return SocketAppender.newBuilder().setName(SocketAppender.class.getSimpleName()).build();
        return SocketAppender
                .newBuilder()
                .setName(SocketAppender.class.getSimpleName())
                .withHost(host)
                .withPort(socketAppenderConfiguration.getPort())
                .setLayout(socketAppenderConfiguration.getLayout())
                .build();
    }

    static ConsoleAppender createLoadedConsoleAppender() {
        Layout<?> patternLayout = loadConsoleAppenderConfiguration().getPatternLayout();
        if (isNull(patternLayout)) ConsoleAppender.newBuilder().setName(ConsoleAppender.class.getSimpleName()).build();
        return ConsoleAppender
                .newBuilder()
                .setName(ConsoleAppender.class.getSimpleName())
                .setLayout(patternLayout)
                .build();
    }

    static void setLoggingMode(LoggingMode mode) {
        LoggerContext context = (LoggerContext) getContext(false);
        Logger rootLogger = context.getRootLogger();
        switch (mode) {
            case CONSOLE:
                ConsoleAppender loadedConsoleAppender = createLoadedConsoleAppender();
                loadedConsoleAppender.start();
                rootLogger.getAppenders().values().forEach(rootLogger::removeAppender);
                rootLogger.addAppender(loadedConsoleAppender);
                context.updateLoggers();
                return;
            case SOCKET:
                SocketAppender loadedSocketAppender = createLoadedSocketAppender();
                loadedSocketAppender.start();
                rootLogger.getAppenders().values().forEach(rootLogger::removeAppender);
                rootLogger.addAppender(loadedSocketAppender);
                context.updateLoggers();
        }
    }

    static LoggerConfig getRootLogger() {
        LoggerContext context = (LoggerContext) getContext(false);
        Configuration configuration = context.getConfiguration();
        if (isNull(configuration)) return null;
        return configuration.getRootLogger();
    }
}
