package ru.art.task.deferred.executor;


/**
 * Исключение, выбрасываемое дефолтным обработчиком ошибок выполнения отложенных событий
 */
class DeferredExecutionException extends RuntimeException {
    DeferredExecutionException(String message) {
        super(message);
    }
}
