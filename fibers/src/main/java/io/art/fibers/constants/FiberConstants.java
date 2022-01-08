package io.art.fibers.constants;

import static io.art.core.determiner.SystemDeterminer.*;

public interface FiberConstants {
    interface GraalConstants {
        static String coroutineLibraryName() {
            if (isWindows()) {
                return "coroutine-windows";
            }
            if (isMac()) {
                return "coroutine-osx";
            }
            return "coroutine-linux";
        }

        String FIBER_INVOKE_METHOD_NAME = "invoke";
    }

    interface Defaults {
        Integer DEFAULT_FIBER_STACK_SIZE = 1024 * 1024;
    }
}
