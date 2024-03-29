package io.art.tarantool.test.manager;

import io.art.core.exception.*;
import io.art.tarantool.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.converter.WslPathConverter.*;
import static io.art.core.determiner.SystemDeterminer.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.core.waiter.Waiter.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static java.lang.Runtime.*;
import static java.nio.charset.Charset.*;
import static java.nio.file.Paths.*;
import static java.text.MessageFormat.*;
import static java.time.Duration.*;
import static java.util.Objects.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class TestTarantoolInstanceManager {
    public static void initializeStorage() {
        initialize(STORAGE_DIRECTORY, STORAGE_SCRIPT);
        waitTime(ofSeconds(5));
    }

    public static void initializeRouter() {
        initialize(SHARD_1_MASTER_DIRECTORY, SHARD_1_MASTER_SCRIPT);
        initialize(SHARD_2_MASTER_DIRECTORY, SHARD_2_MASTER_SCRIPT);
        initialize(SHARD_1_REPLICA_DIRECTORY, SHARD_1_REPLICA_SCRIPT);
        initialize(SHARD_2_REPLICA_DIRECTORY, SHARD_2_REPLICA_SCRIPT);
        initialize(ROUTER_DIRECTORY, ROUTER_SCRIPT);
        waitTime(ofSeconds(5));
    }

    public static void shutdownStorage() {
        shutdown(STORAGE_DIRECTORY, STORAGE_PID);
        waitTime(ofSeconds(5));
    }

    public static void shutdownRouter() {
        shutdown(ROUTER_DIRECTORY, ROUTER_PID);
        shutdown(SHARD_1_MASTER_DIRECTORY, SHARD_1_MASTER_PID);
        shutdown(SHARD_2_MASTER_DIRECTORY, SHARD_2_MASTER_PID);
        shutdown(SHARD_1_REPLICA_DIRECTORY, SHARD_1_REPLICA_PID);
        shutdown(SHARD_2_REPLICA_DIRECTORY, SHARD_2_REPLICA_PID);
        waitTime(ofSeconds(5));
    }

    private static void shutdown(String directory, String pidPath) {
        Path pid = get(directory).resolve(pidPath);
        if (!pid.toFile().exists()) return;
        Path logFile = get(directory).resolve(directory + LOG_EXTENSION);
        if (logFile.toFile().exists()) {
            System.out.println(format(SHUTDOWN_LOG_OUTPUT, directory, readFile(logFile, defaultCharset())));
        }
        String executable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                KILL_COMMAND + readFile(pid, defaultCharset()) +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                executable
        };

        assertEquals(0, wrapExceptionCall(() -> getRuntime().exec(command).waitFor(), TarantoolException::new));

        String deleteExecutable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                DELETE_COMMAND + TEMP_DIRECTORY + SLASH + directory +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] deleteCommand = {
                BASH,
                BASH_ARGUMENT,
                deleteExecutable
        };
        assertEquals(0, wrapExceptionCall(() -> getRuntime().exec(deleteCommand).waitFor(), TarantoolException::new));
        recursiveDelete(get(directory));
    }

    private static void initialize(String directory, String scriptFile) {
        String directoryExecutable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                MKDIR_COMMAND + TEMP_DIRECTORY + SLASH + directory +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] directoryCommand = {
                BASH,
                BASH_ARGUMENT,
                directoryExecutable
        };
        assertEquals(0, wrapExceptionCall(() -> getRuntime().exec(directoryCommand).waitFor(), TarantoolException::new));

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

        assertEquals(0, wrapExceptionCall(() -> getRuntime().exec(command).waitFor(), TarantoolException::new));

        Path logFile = get(directory).resolve(directory + LOG_EXTENSION);
        if (logFile.toFile().exists())
            System.out.println(format(INITIALIZATION_LOG_OUTPUT, directory, readFile(logFile, defaultCharset())));
    }
}
