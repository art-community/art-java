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
        if (!TCP.isPortAvailable(STORAGE_PORT)) return;
        Path working = touchDirectory(get(STORAGE_DIRECTORY));
        InputStream script = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(STORAGE_SCRIPT);
        if (isNull(script)) throw new ImpossibleSituationException();
        InputStream module = TestTarantoolInstanceManager.class.getClassLoader().getResourceAsStream(MODULE_SCRIPT);
        if (isNull(module)) throw new ImpossibleSituationException();
        Path scriptPath = working.resolve(STORAGE_SCRIPT).toAbsolutePath();
        writeFile(scriptPath, toByteArray(script));
        writeFile(working.resolve(get(MODULE_SCRIPT)), toByteArray(module));
        String executable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                STORAGE_COMMAND + SPACE + convertToWslPath(scriptPath.toString()) +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                executable
        };
        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        waitCondition(() -> !TCP.isPortAvailable(STORAGE_PORT));
    }

    public static void shutdownStorage() {
        String executable = (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING) +
                KILL_COMMAND + readFile(get(STORAGE_DIRECTORY).resolve(STORAGE_PID)) +
                (isWindows() ? DOUBLE_QUOTES : EMPTY_STRING);
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                executable
        };
        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        waitCondition(() -> TCP.isPortAvailable(STORAGE_PORT));
        recursiveDelete(get(STORAGE_DIRECTORY));
    }
}
