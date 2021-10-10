package io.art.http.test.service;

import io.art.http.test.communicator.*;
import io.art.http.test.meta.MetaHttpTest.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaTestPackage.MetaServicePackage.*;
import reactor.core.publisher.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.test.registry.HttpTestExecutionsRegistry.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;

public class TestWsService implements TestWs {
    public void ws1() {
        register("ws1", new Object());
        httpModule()
                .state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws1Method)
                .close();
    }

    public String ws2() {
        register("ws2", new Object());
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws2Method)
                .close();
        return "test";
    }

    public Mono<String> ws3() {
        register("ws3", new Object());
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws3Method)
                .close();
        return Mono.just("test");
    }

    public Flux<String> ws4() {
        register("ws4", new Object());
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws4Method)
                .close();
        return Flux.just("test");
    }


    public void ws5(String input) {
        register("ws5", input);
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws5Method)
                .close();
    }

    public String ws6(String input) {
        register("ws6", input);
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws6Method)
                .close();
        return "test";
    }

    public Mono<String> ws7(String input) {
        register("ws7", input);
        return Mono.just("test").doOnSuccess(ignore -> httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws7Method)
                .close());
    }

    public Flux<String> ws8(String input) {
        register("ws8", input);
        return Flux.just("test").doOnComplete(() -> httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws8Method)
                .close());
    }

    public void ws9(Mono<String> input) {
        register("ws9", input);
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws9Method)
                .close();
    }

    public String ws10(Mono<String> input) {
        input.subscribe();
        register("ws10", input);
        httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws10Method)
                .close();
        return "test";
    }

    public Mono<String> ws11(Mono<String> input) {
        input.subscribe();
        register("ws11", input);
        return Mono.just("test").doOnSuccess(ignore -> httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws11Method)
                .close());
    }

    public Flux<String> ws12(Mono<String> input) {
        input.subscribe();
        register("ws12", input);
        return Flux.just("test").doOnComplete(() -> httpModule().state()
                .wsState(TestWsService.class, MetaTestWsServiceClass::ws12Method)
                .close());
    }


    public void ws13(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> httpModule().state()
                        .wsState(TestWsService.class, MetaTestWsServiceClass::ws13Method)
                        .close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws13", many.asFlux());
    }

    public String ws14(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> httpModule().state()
                        .wsState(TestWsService.class, MetaTestWsServiceClass::ws14Method)
                        .close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws14", many.asFlux());
        return "test";
    }

    public Mono<String> ws15(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> httpModule().state()
                        .wsState(TestWsService.class, MetaTestWsServiceClass::ws15Method)
                        .close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws15", many.asFlux());
        return Mono.just("test");
    }

    public Flux<String> ws16(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> httpModule().state()
                        .wsState(TestWsService.class, MetaTestWsServiceClass::ws16Method)
                        .close())
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
        httpModule().state().wsState(TestWsService.class, MetaTestWsServiceClass::ws17Method).close();
        register("ws17", many.asFlux());
    }
}
