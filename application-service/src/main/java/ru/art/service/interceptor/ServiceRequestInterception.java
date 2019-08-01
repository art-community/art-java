package ru.art.service.interceptor;

import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import static ru.art.service.model.ServiceInterceptionResult.*;
import java.util.function.Consumer;

@FunctionalInterface
public interface ServiceRequestInterception {
    static ServiceRequestInterception interceptAndContinue(Consumer<ServiceRequest<?>> runnable) {
        return request -> {
            runnable.accept(request);
            return nextInterceptor(request);
        };
    }

    static ServiceRequestInterception interceptAndCall(Consumer<ServiceRequest<?>> runnable) {
        return request -> {
            runnable.accept(request);
            return processHandling(request);
        };
    }

    static ServiceRequestInterception interceptAndReturn(Consumer<ServiceRequest<?>> runnable) {
        return request -> {
            runnable.accept(request);
            return stopHandling(request);
        };
    }

    ServiceInterceptionResult intercept(ServiceRequest<?> request);
}