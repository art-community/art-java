package io.art.communicator.test.registry;

import static io.art.core.factory.MapFactory.*;
import java.util.*;

public class CommunicatorTestExecutionsRegistry {
    private final static Map<String, Object> executions = map();

    public static void register(String method, Object input) {
        executions.put(method, input);
    }

    public static Map<String, Object> executions() {
        return executions;
    }
}
