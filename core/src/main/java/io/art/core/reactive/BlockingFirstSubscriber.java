package io.art.core.reactive;

import static java.util.Objects.*;

public class BlockingFirstSubscriber<T> extends BlockingSingleSubscriber<T> {
    @Override
    public void onNext(T element) {
        if (isNull(value)) {
            value = element;
            dispose();
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
