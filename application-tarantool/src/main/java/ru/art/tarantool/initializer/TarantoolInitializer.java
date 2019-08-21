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

import org.apache.logging.log4j.Logger;
import org.jtwig.JtwigModel;
import org.tarantool.TarantoolClient;
import org.zeroturnaround.exec.ProcessExecutor;
import ru.art.tarantool.configuration.TarantoolConfiguration;
import ru.art.tarantool.configuration.TarantoolConnectionConfiguration;
import ru.art.tarantool.configuration.TarantoolLocalConfiguration;
import ru.art.tarantool.exception.TarantoolConnectionException;
import ru.art.tarantool.exception.TarantoolInitializationException;
import static java.io.File.separator;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Paths.get;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.converter.WslPathConverter.convertToWslPath;
import static ru.art.core.extension.FileExtensions.writeFile;
import static ru.art.core.extension.InputOutputStreamExtensions.transferBytes;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.core.jar.JarExtensions.extractCurrentJarEntry;
import static ru.art.core.jar.JarExtensions.insideJar;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.connector.TarantoolConnector.connectToTarantool;
import static ru.art.tarantool.connector.TarantoolConnector.tryConnectToTarantool;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Directories.LUA;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Scripts.INITIALIZATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.LOCAL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.PASSWORD;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.USERNAME;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.CONFIGURATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.USER;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;

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
            try {
                TarantoolClient tarantoolClient = tryConnectToTarantool(instanceId);
                if (tarantoolClient.isAlive()) {
                    connectToTarantool(instanceId);
                    return;
                }
            } catch (Throwable newTryException) {
                logger.warn(INSTALL_TARANTOOL_MESSAGE, newTryException);
                throw newTryException;
            }
            logger.warn(INSTALL_TARANTOOL_MESSAGE, e);
            throw e;
        }
        if (instanceMode == LOCAL) {
            logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
            startTarantool(instanceId);
            try {
                TarantoolClient tarantoolClient = tryConnectToTarantool(instanceId);
                if (tarantoolClient.isAlive()) {
                    connectToTarantool(instanceId);
                    return;
                }
            } catch (Throwable newTryException) {
                logger.warn(INSTALL_TARANTOOL_MESSAGE, newTryException);
                throw newTryException;
            }
            logger.warn(INSTALL_TARANTOOL_MESSAGE);
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
            String luaConfiguration = configuration.getInitialConfiguration().toLua(connectionConfiguration.getPort());
            Path luaConfigurationPath = get(getLuaScriptPath(localConfiguration, CONFIGURATION));
            createDirectories(get(localConfiguration.getWorkingDirectory() + separator + LUA));
            writeFile(luaConfigurationPath, luaConfiguration);
            String address = connectionConfiguration.getHost() + COLON + connectionConfiguration.getPort();
            loggingModule()
                    .getLogger()
                    .info(format(WRITING_TARANTOOL_CONFIGURATION, instanceId,
                            address,
                            luaConfiguration,
                            luaConfigurationPath.toAbsolutePath()));

            String luaUserConfiguration = classpathTemplate(USER + JTW_EXTENSION)
                    .render(new JtwigModel()
                            .with(USERNAME, connectionConfiguration.getUsername())
                            .with(PASSWORD, connectionConfiguration.getPassword()));
            Path userConfigurationPath = get(getLuaScriptPath(localConfiguration, USER));
            writeFile(userConfigurationPath, luaUserConfiguration);
            loggingModule()
                    .getLogger()
                    .info(format(WRITING_TARANTOOL_USER_CONFIGURATION,
                            instanceId,
                            address,
                            userConfigurationPath.toAbsolutePath()));
            List<String> executableCommand = localConfiguration.getExecutableCommand();
            if (insideJar()) {
                extractCurrentJarEntry(LUA_REGEX, getLuaScriptPath(localConfiguration, EMPTY_STRING));
                loggingModule()
                        .getLogger()
                        .info(format(EXTRACT_TARANTOOL_LUA_SCRIPTS,
                                instanceId,
                                address,
                                localConfiguration.getWorkingDirectory()) + separator + LUA);

                executableCommand = dynamicArrayOf(executableCommand);
                executableCommand.add(convertToWslPath(getLuaScriptPath(localConfiguration, INITIALIZATION)));
                new ProcessExecutor()
                        .command(executableCommand)
                        .directory(new File(localConfiguration.getWorkingDirectory()))
                        .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                        .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                        .start();
                loggingModule()
                        .getLogger()
                        .info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
                return;
            }

            URL initializationResource = TarantoolInitializer.class
                    .getClassLoader()
                    .getResource(INITIALIZATION);
            if (isNull(initializationResource)) {
                throw new TarantoolInitializationException(format(TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS, instanceId));
            }
            FileOutputStream outputStream = new FileOutputStream(new File(getLuaScriptPath(localConfiguration, INITIALIZATION)));
            transferBytes(initializationResource.openStream(), outputStream);
            outputStream.flush();
            outputStream.close();
            executableCommand = dynamicArrayOf(executableCommand);
            executableCommand.add(convertToWslPath(getLuaScriptPath(localConfiguration, INITIALIZATION)));
            new ProcessExecutor()
                    .command(executableCommand)
                    .directory(new File(localConfiguration.getWorkingDirectory()))
                    .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                    .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                    .start();
            loggingModule()
                    .getLogger(TarantoolInitializer.class)
                    .info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
        } catch (Throwable e) {
            throw new TarantoolInitializationException(e);
        }
    }

    private static String getLuaScriptPath(TarantoolLocalConfiguration localConfiguration, String scriptName) {
        return localConfiguration.getWorkingDirectory() + separator + LUA + separator + scriptName;
    }
}
