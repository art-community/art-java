package ru.adk.task.deferred.executor;

import lombok.Getter;

/**
 * Исключение, выбрасываемое дефолтным обработчиком ошибок пула потоков отложенных событий
 */
@Getter
class DeferredExecutionThreadPoolException extends RuntimeException {
    private final Thread thread;

    DeferredExecutionThreadPoolException(Thread thread, Throwable cause) {
        super(cause);
        this.thread = thread;
    }
}
