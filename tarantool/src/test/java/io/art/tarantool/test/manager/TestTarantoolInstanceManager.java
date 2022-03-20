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
import static java.util.Objects.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class TestTarantoolInstanceManager {
    public static void initializeStorage() {
        initialize(STORAGE_PORT, STORAGE_DIRECTORY, STORAGE_SCRIPT);
    }

    public static void initializeRouter() {
        initialize(SHARD_1_MASTER_PORT, SHARD_1_MASTER_DIRECTORY, SHARD_1_MASTER_SCRIPT);
        initialize(SHARD_1_REPLICA_PORT, SHARD_1_REPLICA_DIRECTORY, SHARD_1_REPLICA_SCRIPT);
        initialize(SHARD_2_MASTER_PORT, SHARD_2_MASTER_DIRECTORY, SHARD_2_MASTER_SCRIPT);
        initialize(SHARD_2_REPLICA_PORT, SHARD_2_REPLICA_DIRECTORY, SHARD_2_REPLICA_SCRIPT);
        initialize(ROUTER_PORT, ROUTER_DIRECTORY, ROUTER_SCRIPT);
    }

    public static void shutdownStorage() {
        shutdown(STORAGE_PORT, STORAGE_DIRECTORY, STORAGE_PID);
    }

    public static void shutdownRouter() {
        shutdown(ROUTER_PORT, ROUTER_DIRECTORY, ROUTER_PID);
        initialize(SHARD_1_REPLICA_PORT, SHARD_1_REPLICA_DIRECTORY, SHARD_1_REPLICA_SCRIPT);
        initialize(SHARD_1_MASTER_PORT, SHARD_1_MASTER_DIRECTORY, SHARD_1_MASTER_SCRIPT);
        initialize(SHARD_2_REPLICA_PORT, SHARD_2_REPLICA_DIRECTORY, SHARD_2_REPLICA_SCRIPT);
        initialize(SHARD_2_MASTER_PORT, SHARD_2_MASTER_DIRECTORY, SHARD_2_MASTER_SCRIPT);
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
        recursiveDelete(get(directory));
    }

    private static void initialize(int port, String directory, String scriptFile) {
        if (!TCP.isPortAvailable(port)) return;
        Path working = touchDirectory(get(directory));
        InputStream script = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(scriptFile);
        if (isNull(script)) throw new ImpossibleSituationException();
        InputStream module = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(MODULE_SCRIPT);
        if (isNull(module)) throw new ImpossibleSituationException();
        Path scriptPath = working.resolve(scriptFile).toAbsolutePath();
        writeFile(scriptPath, toByteArray(script));
        writeFile(working.resolve(get(MODULE_SCRIPT)), toByteArray(module));
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
    }
}
