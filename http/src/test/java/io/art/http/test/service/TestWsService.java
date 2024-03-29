package io.art.http.test.service;

import io.art.http.state.*;
import io.art.http.test.communicator.*;
import io.art.http.test.meta.*;
import io.art.http.test.meta.MetaHttpTest.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaTestPackage.MetaServicePackage.*;
import reactor.core.publisher.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.http.module.HttpModule.*;
import static io.art.http.test.meta.MetaHttpTest.MetaIoPackage.MetaArtPackage.MetaHttpPackage.MetaTestPackage.MetaCommunicatorPackage.MetaTestWsClass.testWs;
import static io.art.http.test.registry.HttpTestExecutionsRegistry.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;

@SuppressWarnings(CALLING_SUBSCRIBE_IN_NON_BLOCKING_SCOPE)
public class TestWsService implements TestWs {
    @Override
    public void ws1() {
        register("ws1", new Object());
    }

    @Override
    public String ws2() {
        register("ws2", new Object());
        return "test";
    }

    @Override
    public Mono<String> ws3() {
        register("ws3", new Object());
        return Mono.just("test");
    }

    @Override
    public Flux<String> ws4() {
        register("ws4", new Object());
        return Flux.just("test");
    }


    @Override
    public void ws5(String input) {
        register("ws5", input);
    }

    @Override
    public String ws6(String input) {
        register("ws6", input);
        return "test";
    }

    @Override
    public Mono<String> ws7(String input) {
        register("ws7", input);
        return Mono.just("test");
    }

    @Override
    public Flux<String> ws8(String input) {
        register("ws8", input);
        return Flux.just("test");
    }

    @Override
    public void ws9(Mono<String> input) {
        register("ws9", input);
    }

    @Override
    public String ws10(Mono<String> input) {
        input.subscribe();
        register("ws10", input);
        return "test";
    }

    @Override
    public Mono<String> ws11(Mono<String> input) {
        input.subscribe();
        register("ws11", input);
        return Mono.just("test");
    }

    @Override
    public Flux<String> ws12(Mono<String> input) {
        input.subscribe();
        register("ws12", input);
        return Flux.just("test");
    }


    @Override
    public void ws13(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule()
                .state()
                .wsState(testWs().ws13Method())
                .disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws13", many.asFlux());
    }

    @Override
    public String ws14(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule()
                .state()
                .wsState(testWs().ws14Method())
                .disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws14", many.asFlux());
        return "test";
    }

    @Override
    public Mono<String> ws15(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule()
                .state()
                .wsState(testWs().ws15Method())
                .disableAutoClosing();
        input.doOnNext(element -> many.emitNext(element, FAIL_FAST))
                .doOnNext(ignore -> state.close())
                .doOnError(element -> many.emitError(element, FAIL_FAST))
                .doOnComplete(() -> many.emitComplete(FAIL_FAST))
                .subscribe();
        register("ws15", many.asFlux());
        return Mono.just("test");
    }

    @Override
    public Flux<String> ws16(Flux<String> input) {
        Sinks.Many<Object> many = Sinks.many().unicast().onBackpressureBuffer();
        WsLocalState state = httpModule()
                .state()
                .wsState(testWs().ws16Method())
                .disableAutoClosing();
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

    @Override
    public Flux<String> wsEcho(Flux<String> input) {
        WsLocalState state = httpModule()
                .state()
                .wsState(testWs().wsEchoMethod())
                .disableAutoClosing();
        Sinks.Many<String> output = Sinks.many().unicast().onBackpressureBuffer();
        input.buffer(10)
                .doOnNext(element -> element.forEach(output::tryEmitNext))
                .doOnNext(ignore -> state.close())
                .doOnComplete(output::tryEmitComplete)
                .subscribe();
        return output.asFlux();
    }
}
