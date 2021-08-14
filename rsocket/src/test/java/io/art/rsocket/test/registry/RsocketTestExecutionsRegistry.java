package io.art.rsocket.test.registry;

import static io.art.core.factory.MapFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.util.concurrent.TimeUnit.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;

public class RsocketTestExecutionsRegistry {
    private final static Map<String, Object> executions = map();
    private final static CountDownLatch waiter = new CountDownLatch(16);

    public static void register(String method, Object input) {
        executions.put(method, input);
        waiter.countDown();
    }

    public static Map<String, Object> executions() {
        ignoreException(() -> waiter.await(1, MINUTES), (Consumer<Throwable>) System.err::println);
        return executions;
    }
}
