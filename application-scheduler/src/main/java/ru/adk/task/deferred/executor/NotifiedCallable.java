package ru.adk.task.deferred.executor;

import lombok.AllArgsConstructor;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

@AllArgsConstructor
class NotifiedCallable<T> implements Callable<T> {
    private Callable<T> executionCallable;
    private Consumer<T> notification;

    @Override
    public T call() throws Exception {
        T result = executionCallable.call();
        notification.accept(result);
        return result;
    }
}
