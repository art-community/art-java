package io.art.fibers.service;

import lombok.experimental.*;
import static io.art.fibers.graal.GraalCoroutineService.*;

@UtilityClass
public class FiberService {
    public static void createFiber(Runnable entryPoint) {
        initializeCoroutine(entryPoint);
    }

    public static void destroyFiber() {
        destroyCoroutine();
    }

    public static void suspend() {
        suspendCoroutine();
    }
}
