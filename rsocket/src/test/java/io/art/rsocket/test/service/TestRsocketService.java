package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;
import reactor.core.publisher.*;
import static io.art.rsocket.test.registry.RsocketTestExecutionsRegistry.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;

public class TestRsocketService implements TestRsocket {
    public void m1() {
        register("m1", new Object());
    }

    public String m2() {
        register("m2", new Object());
        return "test";
    }

    public Mono<String> m3() {
        register("m3", new Object());
        return Mono.just("test");
    }

    public Flux<String> m4() {
        register("m4", new Object());
        return Flux.just("test");
    }


    public void m5(String input) {
        register("m5", input);
    }

    public String m6(String input) {
        register("m6", input);
        return "test";
    }

    public Mono<String> m7(String input) {
        register("m7", input);
        return Mono.just("test");
    }

    public Flux<String> m8(String input) {
        register("m8", input);
        return Flux.just("test");
    }

    public void m9(Mono<String> input) {
        register("m9", input);
    }

    public String m10(Mono<String> input) {
        register("m10", input);
        return "test";
    }

    public Mono<String> m11(Mono<String> input) {
        register("m11", input);
        return Mono.just("test");
    }

    public Flux<String> m12(Mono<String> input) {
        register("m12", input);
        return Flux.just("test");
    }


    public void m13(Flux<String> input) {
        register("m13", input);
    }

    public String m14(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("m14", many.asFlux());
        return "test";
    }

    public Mono<String> m15(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("m15", many.asFlux());
        return Mono.just("test");
    }

    public Flux<String> m16(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("m16", many.asFlux());
        return Flux.just("test");
    }
}
