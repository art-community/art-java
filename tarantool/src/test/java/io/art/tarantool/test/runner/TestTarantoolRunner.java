package io.art.tarantool.test.runner;

import io.art.core.exception.*;
import io.art.logging.*;
import io.art.tarantool.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.converter.WslPathConverter.*;
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
import java.util.*;

@UtilityClass
public class TestTarantoolRunner {
    public static void runStorage() {
        if (!TCP.isPortAvailable(STORAGE_PORT)) return;
        Path working = touchDirectory(get(STORAGE_DIRECTORY));
        InputStream script = TestTarantoolRunner.class.getClassLoader().getResourceAsStream(STORAGE_SCRIPT);
        if (isNull(script)) throw new ImpossibleSituationException();
        InputStream module = TestTarantoolRunner.class.getClassLoader().getResourceAsStream(MODULE_SCRIPT);
        if (isNull(module)) throw new ImpossibleSituationException();
        Path scriptPath = working.resolve(STORAGE_SCRIPT).toAbsolutePath();
        writeFile(scriptPath, toByteArray(script));
        writeFile(working.resolve(get(MODULE_SCRIPT)), toByteArray(module));
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                DOUBLE_QUOTES + STORAGE_COMMAND + SPACE + convertToWslPath(scriptPath.toString()) + DOUBLE_QUOTES
        };
        System.out.println("Executing: " + Arrays.toString(command));
        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        waitCondition(() -> !TCP.isPortAvailable(STORAGE_PORT));
    }

    public static void shutdownStorage() {
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                DOUBLE_QUOTES + KILL_COMMAND + readFile(get(STORAGE_DIRECTORY).resolve(STORAGE_PID)) + DOUBLE_QUOTES
        };
        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
        waitCondition(() -> TCP.isPortAvailable(STORAGE_PORT));
        recursiveDelete(get(STORAGE_DIRECTORY));
    }
}
