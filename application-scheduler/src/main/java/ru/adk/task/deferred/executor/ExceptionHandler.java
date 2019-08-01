package ru.adk.task.deferred.executor;

import ru.adk.task.deferred.executor.SchedulerModuleExceptions.ExceptionEvent;

/**
 * Функциональный интерфейс {@link FunctionalInterface} для обработки ошибок
 */
@FunctionalInterface
interface ExceptionHandler {
    void onException(ExceptionEvent event, Throwable e);
}