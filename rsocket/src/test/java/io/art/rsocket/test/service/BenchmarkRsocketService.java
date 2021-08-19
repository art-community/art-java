package io.art.rsocket.test.service;

import io.art.rsocket.test.communicator.*;
import reactor.core.publisher.*;
import static io.art.rsocket.test.registry.RsocketTestExecutionsRegistry.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;

public class BenchmarkRsocketService implements TestRsocket {
    public void m1() {
    }

    public String m2() {
        return "test";
    }

    public Mono<String> m3() {
        return Mono.just("test");
    }

    public Flux<String> m4() {
        return Flux.just("test");
    }


    public void m5(String input) {
    }

    public String m6(String input) {
        return "test";
    }

    public Mono<String> m7(String input) {
        return Mono.just("test");
    }

    public Flux<String> m8(String input) {
        return Flux.just("test");
    }

    public void m9(Mono<String> input) {
    }

    public String m10(Mono<String> input) {
        return "test";
    }

    public Mono<String> m11(Mono<String> input) {
        return Mono.just("test");
    }

    public Flux<String> m12(Mono<String> input) {
        return Flux.just("test");
    }


    public void m13(Flux<String> input) {
    }

    public String m14(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        return "test";
    }

    public Mono<String> m15(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        return Mono.just("test");
    }

    public Flux<String> m16(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        return Flux.just("test");
    }
}
