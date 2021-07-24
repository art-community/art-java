package io.art.server.test.service;

import reactor.core.publisher.*;
import static io.art.server.test.registry.TestServiceExecutionRegistry.*;

public class TestService {
    public static void m1() {
        register("m1", new Object());
    }

    public static String m2() {
        register("m2", new Object());
        return "test";
    }

    public static Mono<String> m3() {
        register("m3", new Object());
        return Mono.just("test");
    }

    public static Flux<String> m4() {
        register("m4", new Object());
        return Flux.just("test");
    }


    public static void m5(String input) {
        register("m5", input);
    }

    public static String m6(String input) {
        register("m6", input);
        return "test";
    }

    public static Mono<String> m7(String input) {
        register("m7", input);
        return Mono.just("test");
    }

    public static Flux<String> m8(String input) {
        register("m8", input);
        return Flux.just("test");
    }


    public static void m9(Mono<String> input) {
        register("m9", input);
    }

    public static String m10(Mono<String> input) {
        register("m10", input);
        return "test";
    }


    public static Mono<String> m11(Mono<String> input) {
        register("m11", input);
        return Mono.just("test");
    }

    public static Flux<String> m12(Mono<String> input) {
        register("m12", input);
        return Flux.just("test");
    }


    public static void m13(Flux<String> input) {
        register("m13", input);
    }

    public static String m14(Flux<String> input) {
        register("m14", input);
        return "test";
    }

    public static Mono<String> m15(Flux<String> input) {
        register("m15", input);
        return Mono.just("test");
    }

    public static Flux<String> m16(Flux<String> input) {
        register("m16", input);
        return Flux.just("test");
    }
}
