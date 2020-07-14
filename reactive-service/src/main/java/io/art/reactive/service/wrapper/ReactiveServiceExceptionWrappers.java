package io.art.reactive.service.wrapper;

import lombok.*;
import io.art.service.exception.*;
import io.art.service.model.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.service.constants.ServiceErrorCodes.*;
import java.util.*;


@Getter
@NoArgsConstructor
public class ReactiveServiceExceptionWrappers {
    private final Map<Class<? extends Throwable>, ReactiveServiceExceptionWrapper<?>> reactiveServiceExceptionWrappers = mapOf();
    private ReactiveServiceExceptionWrapper<Throwable> undeclaredExceptionWrapper = ((command, exception) -> new ServiceExecutionException(command, UNDECLARED_INTERNAL_ERROR, exception));

    public ReactiveServiceExceptionWrappers(ReactiveServiceExceptionWrapper<Throwable> undeclaredExceptionWrapper) {
        this.undeclaredExceptionWrapper = undeclaredExceptionWrapper;
    }

    public <T extends Throwable> ReactiveServiceExceptionWrappers add(Class<T> exceptionClass, ReactiveServiceExceptionWrapper<T> wrapper) {
        reactiveServiceExceptionWrappers.put(exceptionClass, wrapper);
        return this;
    }

    @FunctionalInterface
    public interface ReactiveServiceExceptionWrapper<T extends Throwable> {
        ServiceExecutionException wrap(ServiceMethodCommand command, T exception);
    }
}
