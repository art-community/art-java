package io.art.server.test.service;

import reactor.core.publisher.*;
import static io.art.server.test.registry.TestServiceExecutionRegistry.*;

public class TestService {
    public static void m1() {
        register("m1", null);
    }

    public static void m2(String input) {
        register("m2", input);
    }

    public static void m3(Mono<String> input) {
        register("m3", input);
    }

    public static void m4(Flux<String> input) {
        register("m4", input);
    }

    public static String m5() {
        register("m5", null);
        return "test";
    }
}
