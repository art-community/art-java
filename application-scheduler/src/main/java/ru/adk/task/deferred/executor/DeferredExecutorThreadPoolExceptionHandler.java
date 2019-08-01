package ru.adk.task.deferred.executor;

/**
 * Дефолтный обработчик ошибок работы пула отложенных событей
 */
class DeferredExecutorThreadPoolExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        throw new DeferredExecutionThreadPoolException(t, e);
    }
}
