package io.art.server.test.service;

import reactor.core.publisher.*;

public class BenchmarkService {
    public static void m1() {
    }

    public static String m2() {
        return "test";
    }

    public static Mono<String> m3() {
        return Mono.just("test");
    }

    public static Flux<String> m4() {
        return Flux.just("test");
    }


    public static void m5(String input) {
    }

    public static String m6(String input) {
        return "test";
    }

    public static Mono<String> m7(String input) {
        return Mono.just("test");
    }

    public static Flux<String> m8(String input) {
        return Flux.just("test");
    }


    public static void m9(Mono<String> input) {
    }

    public static String m10(Mono<String> input) {
        return "test";
    }


    public static Mono<String> m11(Mono<String> input) {
        return Mono.just("test");
    }

    public static Flux<String> m12(Mono<String> input) {
        return Flux.just("test");
    }


    public static void m13(Flux<String> input) {
    }

    public static String m14(Flux<String> input) {
        return "test";
    }

    public static Mono<String> m15(Flux<String> input) {
        return Mono.just("test");
    }

    public static Flux<String> m16(Flux<String> input) {
        return Flux.just("test");
    }
}
