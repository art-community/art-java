package io.art.core.extensions;

import io.art.core.reactive.BlockingFirstSubscriber;
import io.art.core.reactive.BlockingLastSubscriber;
import io.art.core.reactive.BlockingMonoSubscriber;
import lombok.experimental.*;
import org.reactivestreams.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.constants.ReactiveConstants.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import static reactor.core.publisher.Sinks.*;
import javax.annotation.*;
import java.util.concurrent.atomic.*;
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
        Many<T> splitter = many().unicast().onBackpressureBuffer();
        AtomicBoolean condition = new AtomicBoolean(false);

        current.subscribe(new CoreSubscriber<>() {
            @Override
            public void onSubscribe(@Nonnull Subscription subscription) {
            }

            @Override
            public void onNext(T element) {
                if (predicate.test(element)) {
                    if (condition.compareAndSet(false, true)) {
                        compensation.apply(element)
                                .doOnNext(compensated -> splitter.emitNext(compensated, FAIL_FAST))
                                .doOnError(error -> splitter.emitError(error, FAIL_FAST))
                                .doOnComplete(() -> splitter.emitComplete(FAIL_FAST))
                                .subscribe();
                    }
                    return;
                }
                splitter.emitNext(element, FAIL_FAST);
            }

            @Override
            public void onError(Throwable error) {
                if (!condition.get()) splitter.emitError(error, FAIL_FAST);
            }

            @Override
            public void onComplete() {
                if (!condition.get()) splitter.emitComplete(FAIL_FAST);
            }
        });

        return splitter.asFlux();
    }

    public <T> Flux<T> compensateOnError(Flux<T> current, Predicate<Throwable> predicate, Function<Throwable, Flux<T>> compensation) {
        Many<T> splitter = many().unicast().onBackpressureBuffer();
        AtomicBoolean condition = new AtomicBoolean(false);

        current.subscribe(new CoreSubscriber<>() {
            @Override
            public void onSubscribe(@Nonnull Subscription subscription) {
            }

            @Override
            public void onNext(T element) {
                if (!condition.get()) splitter.emitNext(element, FAIL_FAST);
            }

            @Override
            public void onError(Throwable error) {
                if (predicate.test(error)) {
                    if (condition.compareAndSet(false, true)) {
                        compensation.apply(error)
                                .doOnNext(compensated -> splitter.emitNext(compensated, FAIL_FAST))
                                .doOnError(compensationError -> splitter.emitError(compensationError, FAIL_FAST))
                                .doOnComplete(() -> splitter.emitComplete(FAIL_FAST))
                                .subscribe();
                    }
                    return;
                }
                splitter.emitError(error, FAIL_FAST);
            }

            @Override
            public void onComplete() {
                if (!condition.get()) splitter.emitComplete(FAIL_FAST);
            }
        });

        return splitter.asFlux();
    }
}
