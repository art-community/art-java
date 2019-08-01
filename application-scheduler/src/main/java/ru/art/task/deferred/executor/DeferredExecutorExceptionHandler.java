package ru.art.task.deferred.executor;

import static java.text.MessageFormat.format;
import static ru.art.core.checker.CheckerForEmptiness.ifEmpty;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.task.deferred.executor.SchedulerModuleExceptions.EXCEPTION_OCCURRED_DURING;
import static ru.art.task.deferred.executor.SchedulerModuleExceptions.ExceptionEvent;

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
