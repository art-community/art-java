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
import static java.time.Duration.*;
import static java.util.Objects.*;
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
        waitCondition(() -> TCP.isPortAvailable(port));

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
        if (!TCP.isPortAvailable(port)) return;

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
        if (isNull(script)) throw new ImpossibleSituationException();
        InputStream module = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(MODULE_SCRIPT);
        if (isNull(module)) throw new ImpossibleSituationException();
        InputStream sharding = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(SHARDING_SCRIPT);
        if (isNull(sharding)) throw new ImpossibleSituationException();
        InputStream shardInitializer = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(SHARD_INITIALIZER_SCRIPT);
        if (isNull(shardInitializer)) throw new ImpossibleSituationException();
        Path scriptPath = working.resolve(scriptFile).toAbsolutePath();
        writeFile(scriptPath, toByteArray(script));
        writeFile(working.resolve(get(MODULE_SCRIPT)), toByteArray(module));
        writeFile(working.resolve(get(SHARDING_SCRIPT)), toByteArray(sharding));
        writeFile(working.resolve(get(SHARD_INITIALIZER_SCRIPT)), toByteArray(shardInitializer));
        String executable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                instanceCommand(directory) + SPACE + convertToWslPath(scriptPath.toString()) +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);

        String[] command = {
                BASH,
                BASH_ARGUMENT,
                executable
        };

        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        waitCondition(() -> !TCP.isPortAvailable(port));

        waitTime(ofSeconds(3));
    }
}
