package ru.adk.core.wrapper;

import lombok.SneakyThrows;
import ru.adk.core.exception.InternalRuntimeException;
import ru.adk.core.factory.ExceptionFactory;
import static java.util.Objects.isNull;
import static ru.adk.core.constants.ExceptionMessages.EXCEPTION_WRAPPER_ACTION_IS_NULL;
import static ru.adk.core.constants.ExceptionMessages.EXCEPTION_WRAPPER_FACTORY_IS_NULL;
import java.util.concurrent.Callable;

public interface ExceptionWrapper {
    @SneakyThrows
    static void wrap(Runnable action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        action.run();
    }

    @SneakyThrows
    static <T> T wrap(Callable<T> action) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        return action.call();
    }

    static void wrap(Runnable action, ExceptionFactory<?> exceptionFactory) {
        if (isNull(action)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_ACTION_IS_NULL);
        if (isNull(exceptionFactory)) throw new InternalRuntimeException(EXCEPTION_WRAPPER_FACTORY_IS_NULL);
        try {
            action.run();
        } catch (Exception e) {
            throw exceptionFactory.create(e);
        }
    }

    static <T> T wrap(Callable<T> action, ExceptionFactory<?> exceptionFactory) {
        try {
            return action.call();
        } catch (Exception e) {
            throw exceptionFactory.create(e);
        }
    }
}
