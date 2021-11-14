package io.art.core.reactive;

import static java.util.Objects.*;

public class BlockingMonoSubscriber<T> extends BlockingSingleSubscriber<T> {
    @Override
    public void onNext(T element) {
        if (isNull(value)) {
            value = element;
            countDown();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (isNull(value)) {
            error = throwable;
        }
        countDown();
    }
}
