package ru.art.reactive.service.wrapper;

import lombok.*;
import ru.art.service.exception.*;
import ru.art.service.model.*;
import static ru.art.core.factory.CollectionsFactory.*;
import java.util.*;


public class ReactiveServiceExceptionWrappers {
    @Getter
    private final Map<Class<? extends Throwable>, ReactiveServiceExceptionWrapper<?>> reactiveServiceExceptionWrappers = mapOf();

    public <T extends Throwable> ReactiveServiceExceptionWrappers add(Class<T> exceptionClass, ReactiveServiceExceptionWrapper<T> wrapper) {
        reactiveServiceExceptionWrappers.put(exceptionClass, wrapper);
        return this;
    }

    @FunctionalInterface
    public interface ReactiveServiceExceptionWrapper<T extends Throwable> {
        ServiceExecutionException wrap(ServiceMethodCommand command, T exception);
    }
}
