package ru.art.tarantool.module.initializer;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.StartedProcess;
import ru.art.tarantool.configuration.TarantoolConfiguration;
import ru.art.tarantool.configuration.TarantoolConnectionConfiguration;
import ru.art.tarantool.configuration.TarantoolLocalInstanceConfiguration;
import ru.art.tarantool.exception.TarantoolExecutionException;
import ru.art.tarantool.exception.TarantoolInitializationException;
import ru.art.tarantool.module.TarantoolModule;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.io.File.separator;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.sleep;
import static java.nio.file.Files.createDirectories;
import static java.nio.file.Paths.get;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toSet;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.logging.log4j.io.IoBuilder.forLogger;
import static ru.art.core.caster.Caster.cast;
import static ru.art.core.checker.CheckerForEmptiness.isEmpty;
import static ru.art.core.checker.CheckerForEmptiness.isNotEmpty;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.constants.SystemConstants.WSL;
import static ru.art.core.converter.WslPathConverter.convertToWslPath;
import static ru.art.core.determinant.SystemDeterminant.isMac;
import static ru.art.core.determinant.SystemDeterminant.isWindows;
import static ru.art.core.extension.FileExtensions.writeFile;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.core.factory.CollectionsFactory.dynamicArrayOf;
import static ru.art.core.jar.JarExtensions.extractCurrentJarEntry;
import static ru.art.core.jar.JarExtensions.insideJar;
import static ru.art.core.wrapper.ExceptionWrapper.ignoreException;
import static ru.art.core.wrapper.ExceptionWrapper.wrapException;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Directories.BIN;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Directories.LUA;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LUA_REGEX;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static ru.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.TARANTOOL_PROCESS_SUCCESSFULLY_STARTED;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Scripts.INITIALIZATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TWIG_TEMPLATE;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.PASSWORD;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.USERNAME;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.CONFIGURATION;
import static ru.art.tarantool.constants.TarantoolModuleConstants.Templates.USER;
import static ru.art.tarantool.module.TarantoolModule.getTarantoolConfiguration;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;

public class TarantoolInstanceLauncher {
    private final static OutputStream loggerOutputStream = forLogger(loggingModule().getLogger(TarantoolInstanceLauncher.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(TarantoolInstanceLauncher.class);

    protected static void launchTarantoolInstance(String instanceId) {
        try {
            Map<String, TarantoolConfiguration> configurations = tarantoolModule().getTarantoolConfigurations();
            TarantoolConfiguration configuration = getTarantoolConfiguration(instanceId, configurations);

            TarantoolConnectionConfiguration connectionConfiguration = configuration.getConnectionConfiguration();
            TarantoolLocalInstanceConfiguration localConfiguration = tarantoolModule().getLocalConfiguration();
            String address = configuration.getConnectionAddress();

            String workingDirectory = localConfiguration.getWorkingDirectory() + separator + instanceId;
            String luaDirectory = workingDirectory + separator + LUA;
            createDirectories(get(luaDirectory));

            Set<String> replicas = setOf(configuration.getReplicas());
            if (isNotEmpty(replicas)) {
                replicas.add(instanceId);
            }
            replicas = replicas.stream()
                    .map(replica -> getTarantoolConfiguration(replica, configurations))
                    .map(TarantoolConfiguration::getConnectionConfiguration)
                    .map(replicaConfiguration ->
                            replicaConfiguration.getUsername() + COLON + replicaConfiguration.getPassword() + AT_SIGN +
                                    replicaConfiguration.getHost() + COLON + replicaConfiguration.getPort())
                    .collect(toSet());
            String luaConfiguration = configuration.getInitialConfiguration().toLua(connectionConfiguration.getPort(), replicas);
            Path luaConfigurationPath = get(getLuaScriptPath(instanceId, localConfiguration, CONFIGURATION));
            writeFile(luaConfigurationPath, luaConfiguration);
            getLogger().info(format(WRITING_TARANTOOL_CONFIGURATION, instanceId,
                    address,
                    luaConfigurationPath.toAbsolutePath()));
            StringWriter templateWriter = new StringWriter();
            Map<String, Object> templateContext = cast(mapOf()
                    .add(USERNAME, connectionConfiguration.getUsername())
                    .add(PASSWORD, connectionConfiguration.getPassword()));
            new PebbleEngine.Builder()
                    .loader(new ClasspathLoader())
                    .autoEscaping(false)
                    .cacheActive(false)
                    .build()
                    .getTemplate(USER + TWIG_TEMPLATE)
                    .evaluate(templateWriter, templateContext);
            String luaUserConfiguration = templateWriter.toString();
            Path userConfigurationPath = get(getLuaScriptPath(instanceId, localConfiguration, USER));
            writeFile(userConfigurationPath, luaUserConfiguration);
            getLogger().info(format(WRITING_TARANTOOL_USER_CONFIGURATION,
                    instanceId,
                    address,
                    userConfigurationPath.toAbsolutePath()));

            if (isEmpty(localConfiguration.getExecutableFilePath()) && isMac()) {
                throw new TarantoolInitializationException(TARANTOOL_ON_MAC_EXCEPTION);
            }

            if (insideJar(TarantoolModule.class)) {
                launchInstanceFromJar(instanceId, localConfiguration, address);
                return;
            }
            launchInstanceOutOfJar(instanceId, localConfiguration, address);

        } catch (Throwable throwable) {
            getLogger().error(format(STARTUP_ERROR, instanceId), throwable);
            throw new TarantoolInitializationException(throwable);
        }
    }

    private static void launchInstanceFromJar(String instanceId, TarantoolLocalInstanceConfiguration localConfiguration, String address) throws IOException {
        String workingDirectory = localConfiguration.getWorkingDirectory() + separator + instanceId;
        String executableDirectory = workingDirectory + separator + BIN;
        String luaDirectory = workingDirectory + separator + LUA;
        String executableFilePath = localConfiguration.getExecutableFilePath();
        extractCurrentJarEntry(TarantoolModule.class, LUA_REGEX, getLuaScriptPath(instanceId, localConfiguration, EMPTY_STRING));
        if (isEmpty(executableFilePath) || !isMac()) {
            createDirectories(get(executableDirectory));
            extractCurrentJarEntry(TarantoolModule.class, localConfiguration.getExecutable(), executableDirectory);
        }
        getLogger().info(format(EXTRACT_TARANTOOL_LUA_SCRIPTS, instanceId, address, luaDirectory));
        String executableLogMessage = isEmpty(executableFilePath) || !isMac() ? EXTRACT_TARANTOOL_BINARY : USING_TARANTOOL_BINARY;
        getLogger().info(format(executableLogMessage, instanceId, address, executableDirectory));
        String executableFile = isEmpty(executableFilePath) || !isMac() ? executableDirectory + separator + localConfiguration.getExecutable() : executableFilePath;
        if (!new File(executableFile).setExecutable(true)) {
            getLogger().warn(format(FAILED_SET_EXECUTABLE, executableFile));
        }
        List<String> executableCommand = isWindows()
                ? dynamicArrayOf(WSL, convertToWslPath(executableFile))
                : dynamicArrayOf(executableFile);
        executableCommand.add(convertToWslPath(getLuaScriptPath(instanceId, localConfiguration, INITIALIZATION)));
        StartedProcess process = new ProcessExecutor()
                .command(executableCommand)
                .directory(new File(workingDirectory))
                .redirectOutputAlsoTo(loggerOutputStream)
                .redirectErrorAlsoTo(loggerOutputStream)
                .start();
        waitForInstanceStartup(instanceId, localConfiguration, address, process);
    }

    private static void launchInstanceOutOfJar(String instanceId, TarantoolLocalInstanceConfiguration localConfiguration, String address) throws IOException {
        String workingDirectory = localConfiguration.getWorkingDirectory() + separator + instanceId;
        URL executableUrl = ofNullable(localConfiguration.getExecutableFilePath())
                .filter(file -> isMac())
                .map(File::new)
                .filter(File::exists)
                .map(File::toURI)
                .map(uri -> wrapException(uri::toURL, TarantoolExecutionException::new))
                .orElse(TarantoolInitializer.class.getClassLoader().getResource(localConfiguration.getExecutable()));
        if (isNull(executableUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_EXECUTABLE_NOT_EXISTS, address, localConfiguration.getExecutable()));
        }
        if (!new File(executableUrl.getPath()).setExecutable(true)) {
            getLogger().warn(format(FAILED_SET_EXECUTABLE, executableUrl.getPath()));
        }
        List<String> executableCommand;
        executableCommand = isWindows()
                ? dynamicArrayOf(WSL, convertToWslPath(executableUrl.getPath()))
                : dynamicArrayOf(executableUrl.getPath());
        URL initializationScriptUrl = TarantoolInitializer.class.getClassLoader().getResource(INITIALIZATION);
        if (isNull(initializationScriptUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS, address));
        }
        executableCommand.add(isWindows() ? convertToWslPath(initializationScriptUrl.getPath()) : initializationScriptUrl.getPath());
        StartedProcess process = new ProcessExecutor()
                .command(executableCommand)
                .directory(new File(workingDirectory))
                .redirectOutputAlsoTo(loggerOutputStream)
                .redirectErrorAlsoTo(loggerOutputStream)
                .start();
        waitForInstanceStartup(instanceId, localConfiguration, address, process);
    }

    private static String getLuaScriptPath(String instanceId, TarantoolLocalInstanceConfiguration localConfiguration, String scriptName) {
        String workingDirectory = localConfiguration.getWorkingDirectory() + separator + instanceId;
        String luaDirectory = workingDirectory + separator + LUA;
        return luaDirectory + separator + scriptName;
    }

    private static void waitForInstanceStartup(String instanceId, TarantoolLocalInstanceConfiguration localConfiguration, String address, StartedProcess process) {
        long current = currentTimeMillis();
        int checkIntervalMillis = localConfiguration.getProcessStartupCheckIntervalMillis();
        int startupTimeoutMillis = localConfiguration.getProcessStartupTimeoutMillis();
        while (!process.getProcess().isAlive()) {
            getLogger().info(format(WAITING_FOR_INITIALIZATION, instanceId, address, startupTimeoutMillis, checkIntervalMillis));
            ignoreException(() -> sleep(checkIntervalMillis));
            if (currentTimeMillis() - current > startupTimeoutMillis) {
                throw new TarantoolInitializationException(format(TARANTOOL_PROCESS_FAILED, instanceId));
            }
        }
        ignoreException(() -> sleep(checkIntervalMillis));
        getLogger().info(format(TARANTOOL_PROCESS_SUCCESSFULLY_STARTED, instanceId, address));
    }
}
