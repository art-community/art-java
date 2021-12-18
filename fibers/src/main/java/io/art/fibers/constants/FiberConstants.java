package io.art.fibers.constants;

public interface FiberConstants {
    interface GraalConstants {
        String COROUTINE_LIBRARY_NAME = "coroutine";
        String FIBER_INVOKE_METHOD_NAME = "invoke";
    }

    interface Defaults {
        Integer DEFAULT_FIBER_STACK_SIZE = 1024 * 1024;
    }
}
