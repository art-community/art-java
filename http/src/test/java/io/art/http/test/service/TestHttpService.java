package io.art.http.test.service;

import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.http.test.registry.HttpTestExecutionsRegistry.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;

@SuppressWarnings(CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE)
public class TestHttpService {
    public void post1() {
        register("post1", new Object());
    }

    public String post2() {
        register("post2", new Object());
        return "test";
    }

    public Mono<String> post3() {
        register("post3", new Object());
        return Mono.just("test");
    }

    public Flux<String> post4() {
        register("post4", new Object());
        return Flux.just("test");
    }


    public void post5(String input) {
        register("post5", input);
    }

    public String post6(String input) {
        register("post6", input);
        return "test";
    }

    public Mono<String> post7(String input) {
        register("post7", input);
        return Mono.just("test");
    }

    public Flux<String> post8(String input) {
        register("post8", input);
        return Flux.just("test");
    }

    public void post9(Mono<String> input) {
        register("post9", input);
    }

    public String post10(Mono<String> input) {
        input.subscribe();
        register("post10", input);
        return "test";
    }

    public Mono<String> post11(Mono<String> input) {
        input.subscribe();
        register("post11", input);
        return Mono.just("test");
    }

    public Flux<String> post12(Mono<String> input) {
        input.subscribe();
        register("post12", input);
        return Flux.just("test");
    }


    public void post13(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("post13", many.asFlux());
    }

    public String post14(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("post14", many.asFlux());
        return "test";
    }

    public Mono<String> post15(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("post15", many.asFlux());
        return Mono.just("test");
    }

    public Flux<String> post16(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("post16", many.asFlux());
        return Flux.just("test");
    }

    public void post17(Flux<String> empty) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        empty.doOnComplete(() -> many.emitComplete(FAIL_FAST)).subscribe();
        register("post17", many.asFlux());
    }

    public String post18(Flux<String> empty) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        empty.doOnComplete(() -> many.emitComplete(FAIL_FAST)).subscribe();
        register("post18", many.asFlux());
        return "test";
    }

    public Mono<String> post19(Flux<String> empty) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        empty.doOnComplete(() -> many.emitComplete(FAIL_FAST)).subscribe();
        register("post19", many.asFlux());
        return Mono.just("test");
    }

    public Flux<String> post20(Flux<String> empty) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        empty.doOnComplete(() -> many.emitComplete(FAIL_FAST)).subscribe();
        register("post20", many.asFlux());
        return Flux.just("test");
    }
}
