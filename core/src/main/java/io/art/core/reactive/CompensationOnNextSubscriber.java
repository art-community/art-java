package io.art.core.reactive;

import lombok.*;
import org.reactivestreams.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static java.lang.Long.*;
import static reactor.core.publisher.Sinks.EmitFailureHandler.*;
import static reactor.core.publisher.Sinks.*;
import javax.annotation.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class CompensationOnNextSubscriber<T> implements CoreSubscriber<T> {
    private final Predicate<T> predicate;
    private final Function<T, Flux<T>> compensation;
    private final AtomicBoolean compensating = new AtomicBoolean(false);
    private final AtomicBoolean ownerCompleted = new AtomicBoolean(false);
    private final AtomicBoolean compensationCompleted = new AtomicBoolean(false);
    private final AtomicBoolean done = new AtomicBoolean();
    private final Sinks.Many<T> splitter = many().unicast().onBackpressureBuffer();

    @Override
    public void onSubscribe(@Nonnull Subscription subscription) {
        if (!done.get()) subscription.request(MAX_VALUE);
    }

    @Override
    public void onNext(T element) {
        if (done.get()) return;
        if (predicate.test(element)) {
            if (compensating.compareAndSet(false, true)) {
                compensation.apply(element)
                        .doOnNext(compensated -> splitter.emitNext(compensated, FAIL_FAST))
                        .doOnError(this::completeCompensationWithError)
                        .doOnComplete(this::completeCompensation)
                        .subscribe();
            }
            return;
        }
        splitter.emitNext(element, FAIL_FAST);
    }

    @Override
    public void onError(Throwable error) {
        if (done.get()) return;
        if (!compensating.get()) splitter.emitError(error, FAIL_FAST);
        onComplete();
    }

    @Override
    public void onComplete() {
        if (done.get()) return;
        ownerCompleted.compareAndSet(false, true);
        if (compensating.get()) {
            if (compensationCompleted.get()) {
                if (done.compareAndSet(false, true)) {
                    splitter.emitComplete(FAIL_FAST);
                }
            }
            return;
        }
        if (done.compareAndSet(false, true)) {
            splitter.emitComplete(FAIL_FAST);
        }
    }

    public Flux<T> asFlux() {
        return splitter.asFlux();
    }

    private void completeCompensationWithError(Throwable error) {
        if (done.get()) return;
        splitter.emitError(error, FAIL_FAST);
        completeCompensation();
    }

    private void completeCompensation() {
        if (done.get()) return;
        compensationCompleted.compareAndSet(false, true);
        if (ownerCompleted.get()) {
            if (done.compareAndSet(false, true)) {
                splitter.emitComplete(FAIL_FAST);
            }
        }
    }
}
