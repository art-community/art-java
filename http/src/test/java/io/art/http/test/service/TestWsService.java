package io.art.http.test.service;

import io.art.http.state.*;
import io.art.http.test.communicator.*;
import io.art.http.test.meta.MetaHttpTest.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaTestPackage.MetaServicePackage.*;
import reactor.core.publisher.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.test.registry.HttpTestExecutionsRegistry.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;

public class TestWsService implements TestWs {
    public void ws1() {
        register("ws1", new Object());
    }

    public String ws2() {
        register("ws2", new Object());
        return "test";
    }

    public Mono<String> ws3() {
        register("ws3", new Object());
        return Mono.just("test");
    }

    public Flux<String> ws4() {
        register("ws4", new Object());
        return Flux.just("test");
    }


    public void ws5(String input) {
        register("ws5", input);
    }

    public String ws6(String input) {
        register("ws6", input);
        return "test";
    }

    public Mono<String> ws7(String input) {
        register("ws7", input);
        return Mono.just("test");
    }

    public Flux<String> ws8(String input) {
        register("ws8", input);
        return Flux.just("test");
    }

    public void ws9(Mono<String> input) {
        register("ws9", input);
    }

    public String ws10(Mono<String> input) {
        input.subscribe();
        register("ws10", input);
        return "test";
    }

    public Mono<String> ws11(Mono<String> input) {
        input.subscribe();
        register("ws11", input);
        return Mono.just("test");
    }

    public Flux<String> ws12(Mono<String> input) {
        input.subscribe();
        register("ws12", input);
        return Flux.just("test");
    }


    public void ws13(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws13Method)
                .disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws13", many.asFlux());
    }

    public String ws14(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws14Method)
                .disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws14", many.asFlux());
        return "test";
    }

    public Mono<String> ws15(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws15Method)
                .disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws15", many.asFlux());
        return Mono.just("test");
    }

    public Flux<String> ws16(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule().state().wsState(TestWsService.class, MetaTestWsServiceClass::ws16Method).disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws16", many.asFlux());
        return Flux.just("test");
    }

    @Override
    public void ws17(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnComplete(() -> {
            many.emitNext("test", FAIL_FAST);
            many.emitComplete(FAIL_FAST);
        }).subscribe();
        register("ws17", many.asFlux());
    }
}
