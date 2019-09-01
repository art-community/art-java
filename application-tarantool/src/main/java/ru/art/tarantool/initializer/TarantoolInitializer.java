/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.tarantool.initializer;

import org.apache.logging.log4j.*;
import org.jtwig.*;
import org.tarantool.*;
import org.zeroturnaround.exec.*;
import ru.art.tarantool.configuration.*;
import ru.art.tarantool.exception.*;
import ru.art.tarantool.module.*;
import static java.io.File.*;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static org.apache.logging.log4j.io.IoBuilder.*;
import static org.jtwig.JtwigTemplate.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.constants.SystemConstants.*;
import static ru.art.core.converter.WslPathConverter.*;
import static ru.art.core.determinant.SystemDeterminant.*;
import static ru.art.core.extension.FileExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.core.jar.JarExtensions.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.tarantool.connector.TarantoolConnector.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Directories.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.UNABLE_TO_CONNECT_TO_TARANTOOL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Scripts.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.USER;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.*;
import static ru.art.tarantool.module.TarantoolModule.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class TarantoolInitializer {
    private final static OutputStream TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM = forLogger(loggingModule()
            .getLogger(TarantoolInitializer.class))
            .buildOutputStream();
    private final static Logger logger = loggingModule().getLogger(TarantoolInitializer.class);

    @SuppressWarnings("Duplicates")
    public static void initializeTarantool(String instanceId) {
        TarantoolConfiguration tarantoolConfiguration = tarantoolModule()
                .getTarantoolConfigurations()
                .get(instanceId);
        if (isNull(tarantoolConfiguration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        TarantoolInstanceMode instanceMode = tarantoolConfiguration.getInstanceMode();
        TarantoolConnectionConfiguration connectionConfiguration = tarantoolConfiguration.getConnectionConfiguration();
        String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
        try {
            TarantoolClient tarantoolClient = tryConnectToTarantool(instanceId);
            if (tarantoolClient.isAlive()) {
                connectToTarantool(instanceId);
                return;
            }
        } catch (Throwable e) {
            if (instanceMode == LOCAL) {
                logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address), e);
                startTarantool(instanceId);
            }
            TarantoolClient tarantoolClient = tryConnectToTarantool(instanceId);
            if (tarantoolClient.isAlive()) {
                connectToTarantool(instanceId);
                return;
            }
            throw e;
        }
        if (instanceMode == LOCAL) {
            logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
            startTarantool(instanceId);
            TarantoolClient tarantoolClient = tryConnectToTarantool(instanceId);
            if (tarantoolClient.isAlive()) {
                connectToTarantool(instanceId);
                return;
            }
            throw new TarantoolInitializationException(UNABLE_TO_CONNECT_TO_TARANTOOL);
        }
        connectToTarantool(instanceId);
    }

    @SuppressWarnings("Duplicates")
    private static void startTarantool(String instanceId) {
        try {
            TarantoolConfiguration configuration = tarantoolModule()
                    .getTarantoolConfigurations()
                    .get(instanceId);
            if (isNull(configuration)) {
                throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
            }

            TarantoolConnectionConfiguration connectionConfiguration = configuration.getConnectionConfiguration();
            TarantoolLocalConfiguration localConfiguration = tarantoolModule().getLocalConfiguration();
            String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();

            createDirectories(get(localConfiguration.getWorkingDirectory() + separator + LUA));

            String luaConfiguration = configuration.getInitialConfiguration().toLua(connectionConfiguration.getPort());
            Path luaConfigurationPath = get(getLuaScriptPath(localConfiguration, CONFIGURATION));
            writeFile(luaConfigurationPath, luaConfiguration);
            logger.info(format(WRITING_TARANTOOL_CONFIGURATION, instanceId,
                    address,
                    luaConfiguration,
                    luaConfigurationPath.toAbsolutePath()));

            String luaUserConfiguration = classpathTemplate(USER + JTW_EXTENSION)
                    .render(new JtwigModel()
                            .with(USERNAME, connectionConfiguration.getUsername())
                            .with(PASSWORD, connectionConfiguration.getPassword()));
            Path userConfigurationPath = get(getLuaScriptPath(localConfiguration, USER));
            writeFile(userConfigurationPath, luaUserConfiguration);
            logger.info(format(WRITING_TARANTOOL_USER_CONFIGURATION,
                    instanceId,
                    address,
                    userConfigurationPath.toAbsolutePath()));

            List<String> executableCommand = dynamicArrayOf(localConfiguration.getExecutable());
            if (insideJar(TarantoolModule.class)) {
                startTarantoolFromJar(instanceId, localConfiguration, address);
                return;
            }
            startTarantoolOutOfJar(instanceId, localConfiguration, address);

        } catch (Throwable e) {
            throw new TarantoolInitializationException(e);
        }
    }

    private static void startTarantoolFromJar(String instanceId, TarantoolLocalConfiguration localConfiguration, String address) throws IOException {
        createDirectories(get(localConfiguration.getWorkingDirectory() + separator + BIN));
        extractCurrentJarEntry(TarantoolModule.class, LUA_REGEX, getLuaScriptPath(localConfiguration, EMPTY_STRING));
        extractCurrentJarEntry(TarantoolModule.class, localConfiguration.getExecutable(), localConfiguration.getWorkingDirectory() + separator + BIN);
        logger.info(format(EXTRACT_TARANTOOL_LUA_SCRIPTS,
                instanceId,
                address,
                localConfiguration.getWorkingDirectory()) + separator + LUA);
        logger.info(format(EXTRACT_TARANTOOL_BINARY,
                instanceId,
                address,
                localConfiguration.getWorkingDirectory()) + separator + BIN);
        String executableFilePath = localConfiguration.getWorkingDirectory()
                + separator
                + BIN
                + separator
                + localConfiguration.getExecutable();
        if (!new File(executableFilePath).setExecutable(true)) {
            logger.warn(format(FAILED_SET_EXECUTABLE, executableFilePath));
        }
        List<String> executableCommand = isWindows()
                ? dynamicArrayOf(WSL, convertToWslPath(executableFilePath))
                : dynamicArrayOf(executableFilePath);
        executableCommand.add(convertToWslPath(getLuaScriptPath(localConfiguration, INITIALIZATION)));
        new ProcessExecutor()
                .command(executableCommand)
                .directory(new File(localConfiguration.getWorkingDirectory()))
                .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                .start();
        logger.info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
    }

    private static void startTarantoolOutOfJar(String instanceId, TarantoolLocalConfiguration localConfiguration, String address) throws IOException {
        URL executableUrl = TarantoolInitializer.class.getClassLoader().getResource(localConfiguration.getExecutable());
        if (isNull(executableUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_EXECUTABLE_NOT_EXISTS, address, localConfiguration.getExecutable()));
        }
        if (!new File(executableUrl.getPath()).setExecutable(true)) {
            logger.warn(format(FAILED_SET_EXECUTABLE, executableUrl.getPath()));
        }
        List<String> executableCommand;
        executableCommand = isWindows()
                ? dynamicArrayOf(WSL, convertToWslPath(executableUrl.getPath()))
                : dynamicArrayOf(executableUrl.getPath());
        URL initializationScriptUrl = TarantoolInitializer.class.getClassLoader().getResource(INITIALIZATION);
        if (isNull(initializationScriptUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS, address));
        }
        executableCommand.add(convertToWslPath(initializationScriptUrl.getPath()));
        new ProcessExecutor()
                .command(executableCommand)
                .directory(new File(localConfiguration.getWorkingDirectory()))
                .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                .start();
        logger.info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
    }

    private static String getLuaScriptPath(TarantoolLocalConfiguration localConfiguration, String scriptName) {
        return localConfiguration.getWorkingDirectory() + separator + LUA + separator + scriptName;
    }
}
