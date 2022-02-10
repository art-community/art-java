package io.art.tarantool.test.runner;

import io.art.core.exception.*;
import io.art.tarantool.exception.*;
import lombok.experimental.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.converter.WslPathConverter.*;
import static io.art.core.extensions.FileExtensions.*;
import static io.art.core.extensions.InputStreamExtensions.*;
import static io.art.core.network.selector.PortSelector.SocketType.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.tarantool.test.constants.TestTarantoolConstants.*;
import static java.lang.Runtime.*;
import static java.nio.file.Paths.*;
import static java.util.Objects.*;
import java.io.*;
import java.nio.file.*;

@UtilityClass
public class TestTarantoolRunner {
    public static void runStorage() {
        if (!TCP.isPortAvailable(STORAGE_PORT)) return;
        InputStream script = TestTarantoolRunner.class.getClassLoader().getResourceAsStream(STORAGE_SCRIPT);
        if (isNull(script)) throw new ImpossibleSituationException();
        InputStream module = TestTarantoolRunner.class.getClassLoader().getResourceAsStream(MODULE_SCRIPT);
        if (isNull(module)) throw new ImpossibleSituationException();
        Path scriptPath = get(STORAGE_SCRIPT).toAbsolutePath();
        writeFile(scriptPath, toByteArray(script));
        writeFile(get(MODULE_SCRIPT), toByteArray(module));
        String[] command = {
                BASH,
                BASH_ARGUMENT,
                DOUBLE_QUOTES + EXECUTABLE + SPACE + convertToWslPath(scriptPath.toString()) + DOUBLE_QUOTES
        };
        wrapExceptionCall(() -> getRuntime().exec(command), TarantoolException::new);
    }

    public static void deleteScripts() {
        recursiveDelete(get(STORAGE_SCRIPT).toAbsolutePath());
        recursiveDelete(get(MODULE_SCRIPT).toAbsolutePath());
    }
}
