package io.art.http.test.registry;

import static io.art.core.factory.MapFactory.*;
import static io.art.core.waiter.Waiter.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class HttpTestExecutionsRegistry {
    private final static Map<String, Object> executions = concurrentMap();

    public static void register(String method, Object input) {
        executions.put(method, input);
    }

    public static void clear() {
        executions.clear();
    }

    public static Map<String, Object> executions() {
        assertTrue(waitCondition(() -> executions.size() == 16));
        return executions;
    }
}
