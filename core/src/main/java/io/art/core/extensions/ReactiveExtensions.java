package io.art.core.extensions;

import io.art.core.reactive.BlockingFirstSubscriber;
import io.art.core.reactive.BlockingLastSubscriber;
import io.art.core.reactive.BlockingMonoSubscriber;
import io.art.core.reactive.*;
import lombok.experimental.*;
import org.reactivestreams.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.ReactiveConstants.*;
import static java.util.Objects.*;
import java.util.function.*;

@UtilityClass
public class ReactiveExtensions {
    public Mono<Void> mono(Runnable action) {
        return Mono.create(emitter -> {
            action.run();
            emitter.success(null);
        });
    }

    public Flux<Void> flux(Runnable action) {
        return Flux.create(emitter -> {
            action.run();
            emitter.complete();
        });
    }

    public <T> Publisher<T> asPublisher(Object object) {
        return cast(object);
    }

    public <T> Mono<T> asMono(Object object) {
        return cast(object);
    }

    public <T> Flux<T> asFlux(Object object) {
        return cast(object);
    }

    public <T> T blockFirst(Flux<T> flux) {
        BlockingFirstSubscriber<T> subscriber = new BlockingFirstSubscriber<>();
        flux.subscribe(subscriber);
        return subscriber.blockingGet();
    }

    public <T> T blockLast(Flux<T> flux) {
        BlockingLastSubscriber<T> subscriber = new BlockingLastSubscriber<>();
        flux.subscribe(subscriber);
        return subscriber.blockingGet();
    }

    public <T> T block(Mono<T> mono) {
        BlockingMonoSubscriber<T> subscriber = new BlockingMonoSubscriber<>();
        mono.subscribe(subscriber);
        return subscriber.blockingGet();
    }

    public <T> T blockNullable(Mono<T> mono) {
        BlockingMonoSubscriber<T> subscriber = new BlockingMonoSubscriber<>();
        mono.subscribe(subscriber);
        T result = subscriber.blockingGet();
        return result == NULL_OBJECT ? null : result;
    }

    public <T> Flux<T> compensate(Flux<T> current, Predicate<T> predicate, Function<T, Flux<T>> compensation) {
        CompensationOnNextSubscriber<T> compensationSubscriber = new CompensationOnNextSubscriber<>(predicate, compensation);
        current.subscribe(compensationSubscriber);
        return compensationSubscriber.asFlux();
    }

    public <T> Flux<T> compensateOnError(Flux<T> current, Predicate<Throwable> predicate, Function<Throwable, Flux<T>> compensation) {
        CompensationOnErrorSubscriber<T> compensationSubscriber = new CompensationOnErrorSubscriber<>(predicate, compensation);
        current.subscribe(compensationSubscriber);
        return compensationSubscriber.asFlux();
    }

    public <T> Flux<T> compensateOnError(Flux<T> current, Function<Throwable, Flux<T>> compensation) {
        return compensateOnError(current, ignore -> true, compensation);
    }

    public static <T> Flux<T> justOrEmpty(T data) {
        return nonNull(data) ? Flux.just(data) : Flux.empty();
    }
}
