package ru.adk.task.deferred.executor;

import static java.text.MessageFormat.format;
import static ru.adk.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.task.deferred.executor.SchedulerModuleExceptions.EXCEPTION_OCCURRED_DURING;
import static ru.adk.task.deferred.executor.SchedulerModuleExceptions.ExceptionEvent;

/**
 * Дефолтный обработчик ошибок выполнения отложенных событей
 */
public class DeferredExecutorExceptionHandler implements ExceptionHandler {
    @Override
    public void onException(ExceptionEvent event, Throwable e) {
        loggingModule()
                .getLogger(DeferredExecutorExceptionHandler.class)
                .error(format(EXCEPTION_OCCURRED_DURING, event.getMessage(), ifEmpty(e.getMessage(), e.getClass()), e));
    }
}
