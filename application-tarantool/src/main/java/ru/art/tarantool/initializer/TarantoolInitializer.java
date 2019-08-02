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
import ru.art.tarantool.module.TarantoolModule;
import static java.io.File.separator;
import static java.lang.Thread.sleep;
import static java.nio.file.Paths.get;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static org.jtwig.JtwigTemplate.classpathTemplate;
import static ru.art.core.constants.StringConstants.COLON;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.extension.FileExtensions.writeFile;
import static ru.art.core.jar.JarExtensions.extractCurrentJarEntry;
import static ru.art.core.jar.JarExtensions.insideJar;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.connector.TarantoolConnector.connectToTarantool;
import static ru.art.tarantool.connector.TarantoolConnector.tryConnectToTarantool;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Directories.BIN;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Directories.LUA;
import static ru.art.tarantool.constants.TarantoolModuleConstants.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Scripts.INITIALIZATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInstanceMode.LOCAL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.PASSWORD;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.USERNAME;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.CONFIGURATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.USER;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;

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
        } catch (Exception e) {
            if (instanceMode == LOCAL) {
                logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
                startTarantool(instanceId);
            }
            connectToTarantool(instanceId);
            return;
        }
        if (instanceMode == LOCAL) {
            logger.warn(format(UNABLE_TO_CONNECT_TO_TARANTOOL_ON_STARTUP, instanceId, address));
            startTarantool(instanceId);
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
            String luaConfiguration = configuration.getInitialConfiguration().toLua();
            Path luaConfigurationPath = get(getLuaScriptPath(localConfiguration, CONFIGURATION));
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
            URL tarantoolExecutableResource = TarantoolModule.class
                    .getClassLoader()
                    .getResource(localConfiguration.getExecutableApplicationName());
            if (isNull(tarantoolExecutableResource)) {
                throw new TarantoolInitializationException(format(TARANTOOL_EXECUTABLE_NOT_EXISTS, instanceId));
            }

            if (insideJar()) {
                extractCurrentJarEntry(localConfiguration.getExecutableApplicationName(), getBinaryPath(localConfiguration, EMPTY_STRING));
                loggingModule()
                        .getLogger()
                        .info(format(EXTRACT_TARANTOOL_EXECUTABLE,
                                instanceId,
                                address,
                                localConfiguration.getExecutableApplicationName(),
                                getBinaryPath(localConfiguration, EMPTY_STRING)));

                extractCurrentJarEntry(LUA_REGEX, getLuaScriptPath(localConfiguration, EMPTY_STRING));
                loggingModule()
                        .getLogger()
                        .info(format(EXTRACT_TARANTOOL_LUA_SCRIPTS,
                                instanceId,
                                address,
                                localConfiguration.getWorkingDirectory() + separator + LUA));

                String binaryPath = getBinaryPath(localConfiguration, localConfiguration.getExecutableApplicationName());
                String initializationScriptPath = getLuaScriptPath(localConfiguration, INITIALIZATION);
                new ProcessExecutor()
                        .command(binaryPath, initializationScriptPath)
                        .directory(new File(localConfiguration.getWorkingDirectory()))
                        .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                        .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                        .start();
                sleep(localConfiguration.getStartupTimeoutSeconds());
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

            new ProcessExecutor()
                    .command(tarantoolExecutableResource.getPath(), initializationResource.getFile())
                    .directory(new File(localConfiguration.getWorkingDirectory()))
                    .redirectOutput(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                    .redirectError(TARANTOOL_INITIALIZER_LOGGER_OUTPUT_STREAM)
                    .start();
            sleep(localConfiguration.getStartupTimeoutSeconds());
            loggingModule()
                    .getLogger(TarantoolInitializer.class)
                    .info(format(TARANTOOL_SUCCESSFULLY_STARTED, instanceId, address));
        } catch (Exception e) {
            throw new TarantoolInitializationException(e);
        }
    }

    private static String getLuaScriptPath(TarantoolLocalConfiguration localConfiguration, String scriptName) {
        return localConfiguration.getWorkingDirectory() + separator + LUA + separator + scriptName;
    }

    private static String getBinaryPath(TarantoolLocalConfiguration localConfiguration, String binaryName) {
        return localConfiguration.getWorkingDirectory() + separator + BIN + separator + binaryName;
    }
}
