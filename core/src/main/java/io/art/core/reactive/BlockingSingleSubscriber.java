package io.art.core.reactive;

import org.jetbrains.annotations.*;
import org.reactivestreams.*;
import reactor.core.*;
import reactor.util.context.*;
import static java.lang.Long.*;
import static java.util.Objects.*;
import static reactor.core.Exceptions.*;
import static reactor.core.Scannable.Attr.*;
import static reactor.core.Scannable.Attr.TERMINATED;
import static reactor.core.Scannable.Attr.RunStyle.*;
import static reactor.util.context.Context.*;
import javax.annotation.Nullable;
import java.util.concurrent.*;

public abstract class BlockingSingleSubscriber<T> extends CountDownLatch implements Disposable, CoreSubscriber<T>, Scannable {
    protected T value;
    protected Throwable error;
    protected Subscription subscription;
    private volatile boolean cancelled;

    protected BlockingSingleSubscriber() {
        super(1);
    }

    @Override
    public final void onSubscribe(@NotNull Subscription subscription) {
        this.subscription = subscription;
        if (!cancelled) {
            subscription.request(MAX_VALUE);
        }
    }

    @Override
    public final void onComplete() {
        countDown();
    }

    @Override
    @NotNull
    public Context currentContext() {
        return empty();
    }

    @Override
    public final void dispose() {
        cancelled = true;
        Subscription subscription = this.subscription;
        if (nonNull(subscription)) {
            this.subscription = null;
            subscription.cancel();
        }
    }

    @Nullable
    public T blockingGet() {
        if (getCount() != 0) {
            try {
                await();
            } catch (InterruptedException interruptedException) {
                dispose();
                throw propagate(interruptedException);
            }
        }

        Throwable throwable = error;
        if (nonNull(throwable)) {
            RuntimeException runtimeException = propagate(throwable);
            runtimeException.addSuppressed(new Exception("#block terminated with an error"));
            throw runtimeException;
        }
        return value;
    }

    @Nullable
    public T blockingGet(long timeout, TimeUnit unit) {
        if (getCount() != 0) {
            try {
                if (!await(timeout, unit)) {
                    dispose();
                    throw new IllegalStateException("Timeout on blocking read for " + timeout + " " + unit);
                }
            } catch (InterruptedException interruptedException) {
                dispose();
                RuntimeException runtimeException = propagate(interruptedException);
                runtimeException.addSuppressed(new Exception("#block has been interrupted"));
                throw runtimeException;
            }
        }

        Throwable throwable = error;
        if (nonNull(throwable)) {
            RuntimeException runtimeException = propagate(throwable);
            runtimeException.addSuppressed(new Exception("#block terminated with an error"));
            throw runtimeException;
        }
        return value;
    }

    @Override
    @Nullable
    public Object scanUnsafe(@NotNull Attr key) {
        if (key == TERMINATED) return getCount() == 0;
        if (key == PARENT) return subscription;
        if (key == CANCELLED) return cancelled;
        if (key == ERROR) return error;
        if (key == PREFETCH) return Integer.MAX_VALUE;
        if (key == RUN_STYLE) return SYNC;

        return null;
    }

    @Override
    public boolean isDisposed() {
        return cancelled || getCount() == 0;
    }
}
