package io.art.rsocket.test.registry;

import io.art.logging.*;
import static io.art.core.factory.MapFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.concurrent.*;

public class RsocketTestExecutionsRegistry {
    private final static Map<String, Object> executions = map();
    private final static CountDownLatch waiter = new CountDownLatch(16);

    public static void register(String method, Object input) {
        executions.put(method, input);
        waiter.countDown();
    }

    public static Map<String, Object> executions() {
        try {
            assertTrue(waiter.await(1, MINUTES));
        } catch (InterruptedException interruptedException) {
            Logging.logger().error(interruptedException);
        }
        return executions;
    }
}
