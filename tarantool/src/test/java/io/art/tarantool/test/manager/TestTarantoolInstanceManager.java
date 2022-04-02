package io.art.tarantool.test.manager;

import io.art.core.exception.*;
import io.art.tarantool.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.converter.WslPathConverter.*;
import static io.art.core.determiner.SystemDeterminer.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.core.network.selector.PortSelector.SocketType.*;
import static io.art.core.waiter.Waiter.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static java.lang.Runtime.*;
import static java.nio.file.Paths.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class TestTarantoolInstanceManager {
    public static void initializeStorage() {
        initialize(STORAGE_PORT, STORAGE_DIRECTORY, STORAGE_SCRIPT);
    }

    public static void initializeRouter() {
        initialize(ROUTER_PORT, ROUTER_DIRECTORY, ROUTER_SCRIPT);
        initialize(SHARD_1_MASTER_PORT, SHARD_1_MASTER_DIRECTORY, SHARD_1_MASTER_SCRIPT);
        initialize(SHARD_2_MASTER_PORT, SHARD_2_MASTER_DIRECTORY, SHARD_2_MASTER_SCRIPT);
        initialize(SHARD_1_REPLICA_PORT, SHARD_1_REPLICA_DIRECTORY, SHARD_1_REPLICA_SCRIPT);
        initialize(SHARD_2_REPLICA_PORT, SHARD_2_REPLICA_DIRECTORY, SHARD_2_REPLICA_SCRIPT);
    }

    public static void shutdownStorage() {
        shutdown(STORAGE_PORT, STORAGE_DIRECTORY, STORAGE_PID);
    }

    public static void shutdownRouter() {
        shutdown(ROUTER_PORT, ROUTER_DIRECTORY, ROUTER_PID);
        shutdown(SHARD_1_MASTER_PORT, SHARD_1_MASTER_DIRECTORY, SHARD_1_MASTER_PID);
        shutdown(SHARD_2_MASTER_PORT, SHARD_2_MASTER_DIRECTORY, SHARD_2_MASTER_PID);
        shutdown(SHARD_1_REPLICA_PORT, SHARD_1_REPLICA_DIRECTORY, SHARD_1_REPLICA_PID);
        shutdown(SHARD_2_REPLICA_PORT, SHARD_2_REPLICA_DIRECTORY, SHARD_2_REPLICA_PID);
    }

    private static void shutdown(int port, String directory, String pidPath) {
        System.out.println(format(LOG_OUTPUT, directory, readFile(get(directory).resolve(directory + ".log"))));
        Path pid = get(directory).resolve(pidPath);
        if (!pid.toFile().exists()) return;
        String executable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                KILL_COMMAND + readFile(pid) +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                executable
        };

        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        if (!waitCondition(() -> TCP.isPortAvailable(port))) {
            fail(format(SHUTDOWN_ERROR, directory, port));
        }

        String deleteExecutable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                DELETE_COMMAND + TEMP_DIRECTORY + SLASH + directory +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] deleteCommand = {
                BASH,
                BASH_ARGUMENT,
                deleteExecutable
        };
        wrapExceptionCall(() -> getRuntime().exec(deleteCommand), TarantoolException::new);
        recursiveDelete(get(directory));
    }

    private static void initialize(int port, String directory, String scriptFile) {
        if (!TCP.isPortAvailable(port)) fail(format(PORT_IS_BUSY_ERROR, port));

        String directoryExecutable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                MKDIR_COMMAND + TEMP_DIRECTORY + SLASH + directory +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] directoryCommand = {
                BASH,
                BASH_ARGUMENT,
                directoryExecutable
        };
        wrapExceptionCall(() -> getRuntime().exec(directoryCommand), TarantoolException::new);

        Path working = touchDirectory(get(directory));

        InputStream script = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(scriptFile);
        Path scriptPath = working.resolve(scriptFile).toAbsolutePath();
        writeFile(scriptPath, toByteArray(script));

        for (String additionalScriptPath : ADDITIONAL_SCRIPTS) {
            InputStream additionalScript = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(additionalScriptPath);
            if (isNull(script)) throw new ImpossibleSituationException();
            writeFile(working.resolve(get(additionalScriptPath)), toByteArray(additionalScript));
        }

        String executable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                instanceCommand(directory) + SPACE + convertToWslPath(scriptPath.toString()) +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);

        String[] command = {
                BASH,
                BASH_ARGUMENT,
                executable
        };

        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        if (!waitCondition(() -> !TCP.isPortAvailable(port))) {
            Path logFile = get(directory).resolve(directory + ".log");
            if (logFile.toFile().exists()) {
                System.out.println(format(LOG_OUTPUT, directory, readFile(logFile)));
            }
            fail(format(INITIALIZATION_ERROR, directory, port));
        }
    }
}
