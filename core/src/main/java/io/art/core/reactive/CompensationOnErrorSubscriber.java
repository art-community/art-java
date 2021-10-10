package io.art.core.reactive;

import lombok.*;
import org.reactivestreams.*;
import reactor.core.*;
import reactor.core.publisher.*;
import static java.lang.Long.*;
import static reactor.core.publisher.Sinks.*;
import javax.annotation.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;

@RequiredArgsConstructor
public class CompensationOnErrorSubscriber<T> implements CoreSubscriber<T> {
    private final Predicate<Throwable> predicate;
    private final Function<Throwable, Flux<T>> compensation;
    private final AtomicBoolean compensating = new AtomicBoolean(false);
    private final AtomicBoolean ownerCompleted = new AtomicBoolean(false);
    private final AtomicBoolean compensationCompleted = new AtomicBoolean(false);
    private final Many<T> splitter = many().unicast().onBackpressureBuffer();
    private final AtomicBoolean done = new AtomicBoolean(false);

    @Override
    public void onSubscribe(@Nonnull Subscription subscription) {
        if (!done.get()) subscription.request(MAX_VALUE);
    }

    @Override
    public void onNext(T element) {
        if (done.get()) return;
        if (!compensating.get()) splitter.tryEmitNext(element);
    }

    @Override
    public void onError(Throwable error) {
        if (done.get()) return;
        if (predicate.test(error)) {
            if (compensating.compareAndSet(false, true)) {
                compensation
                        .apply(error)
                        .subscribe(splitter::tryEmitNext, this::completeCompensationWithError, this::completeCompensation);
            }
            onComplete();
            return;
        }
        splitter.tryEmitError(error);
        onComplete();
    }

    @Override
    public void onComplete() {
        if (done.get()) return;
        if (compensating.get()) {
            if (compensationCompleted.get()) {
                if (done.compareAndSet(false, true)) {
                    splitter.tryEmitComplete();
                }
            }
            return;
        }
        if (done.compareAndSet(false, true)) {
            splitter.tryEmitComplete();
        }
    }

    public Flux<T> asFlux() {
        return splitter.asFlux();
    }

    private void completeCompensation() {
        if (done.get()) return;
        compensationCompleted.compareAndSet(false, true);
        if (ownerCompleted.get()) {
            if (done.compareAndSet(false, true)) {
                splitter.tryEmitComplete();
            }
        }
    }

    private void completeCompensationWithError(Throwable compensationError) {
        if (done.get()) return;
        splitter.tryEmitError(compensationError);
        completeCompensation();
    }
}
