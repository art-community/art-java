package io.art.core.reactive;

public class BlockingLastSubscriber<T> extends BlockingSingleSubscriber<T> {
    @Override
    public void onNext(T element) {
        value = element;
    }

    @Override
    public void onError(Throwable throwable) {
        value = null;
        error = throwable;
        countDown();
    }
}
