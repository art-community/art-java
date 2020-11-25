package io.art.tarantool.module.initializer;

import com.mitchellbosecke.pebble.*;
import com.mitchellbosecke.pebble.loader.*;
import lombok.*;
import org.apache.logging.log4j.*;
import org.zeroturnaround.exec.*;
import io.art.tarantool.configuration.*;
import io.art.tarantool.exception.*;
import io.art.tarantool.module.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.constants.SystemConstants.*;
import static io.art.core.converter.WslPathConverter.*;
import static io.art.core.determinant.SystemDeterminant.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.JarExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.logging.LoggingModule.*;
import static java.io.File.*;
import static java.lang.System.*;
import static java.lang.Thread.*;
import static java.nio.file.Files.*;
import static java.nio.file.Paths.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static java.util.Optional.*;
import static java.util.stream.Collectors.*;
import static lombok.AccessLevel.*;
import static org.apache.logging.log4j.io.IoBuilder.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Directories.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.LoggingMessages.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Scripts.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.TemplateParameterKeys.*;
import static io.art.tarantool.constants.TarantoolModuleConstants.Templates.USER;
import static io.art.tarantool.constants.TarantoolModuleConstants.Templates.*;
import static io.art.tarantool.module.TarantoolModule.*;
import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class TarantoolInstanceLauncher {
    private final static OutputStream loggerOutputStream = forLogger(logger(TarantoolInstanceLauncher.class))
            .buildOutputStream();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(TarantoolInstanceLauncher.class);

    protected static void launchTarantoolInstance(String instanceId) {
        try {
            Map<String, TarantoolConfiguration> configurations = tarantoolModule().configuration().getTarantoolConfigurations();
            TarantoolConfiguration configuration = getTarantoolConfiguration(instanceId, configurations);

            TarantoolConnectionConfiguration connectionConfiguration = configuration.getConnectionConfiguration();
            TarantoolLocalInstanceConfiguration localConfiguration = tarantoolModule().configuration().getLocalConfiguration();
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
        extractCurrentJarEntry(TarantoolModule.class, VSHARD_REGEX, getLuaScriptPath(instanceId, localConfiguration, VSHARD));
        extractCurrentJarEntry(TarantoolModule.class, ROUTER_REGEX, getLuaScriptPath(instanceId, localConfiguration, ROUTER));
        extractCurrentJarEntry(TarantoolModule.class, STORAGE_REGEX, getLuaScriptPath(instanceId, localConfiguration, STORAGE));

        getLogger().info(format(EXTRACT_TARANTOOL_VSHARD_SCRIPTS,
                instanceId,
                address,
                getLuaScriptPath(instanceId, localConfiguration, VSHARD)));

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
        StartedProcess process = runTarantoolProcess(executableCommand, workingDirectory);
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
        URL vshardUrl = TarantoolInitializer.class.getClassLoader().getResource(VSHARD);
        if (isNull(vshardUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_VSHARD_NOT_EXISTS, address));
        }
        copyRecursive(get(vshardUrl.getFile()), get(getLuaScriptPath(instanceId, localConfiguration, VSHARD)));

        getLogger().info(format(EXTRACT_TARANTOOL_VSHARD_SCRIPTS,
                instanceId,
                address,
                getLuaScriptPath(instanceId, localConfiguration, VSHARD)));

        if (isNull(executableUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_EXECUTABLE_NOT_EXISTS, address, localConfiguration.getExecutable()));
        }

        if (!new File(executableUrl.getPath()).setExecutable(true)) {
            getLogger().warn(format(FAILED_SET_EXECUTABLE, executableUrl.getPath()));
        }
        List<String> executableCommand = isWindows()
                ? dynamicArrayOf(WSL, convertToWslPath(executableUrl.getPath()))
                : dynamicArrayOf(executableUrl.getPath());
        URL initializationScriptUrl = TarantoolInitializer.class.getClassLoader().getResource(INITIALIZATION);
        if (isNull(initializationScriptUrl)) {
            throw new TarantoolInitializationException(format(TARANTOOL_INITIALIZATION_SCRIP_NOT_EXISTS, address));
        }
        executableCommand.add(isWindows() ? convertToWslPath(initializationScriptUrl.getPath()) : initializationScriptUrl.getPath());

        StartedProcess process = runTarantoolProcess(executableCommand, workingDirectory);
        waitForInstanceStartup(instanceId, localConfiguration, address, process);
    }

    private static StartedProcess runTarantoolProcess(List<String> executableCommand, String workingDirectory) throws IOException{
        return new ProcessExecutor()
                .command(executableCommand)
                .directory(new File(workingDirectory))
                .redirectOutputAlsoTo(loggerOutputStream)
                .redirectErrorAlsoTo(loggerOutputStream)
                .start();

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
