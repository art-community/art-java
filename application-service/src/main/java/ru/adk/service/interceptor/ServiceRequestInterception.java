package ru.adk.service.interceptor;

import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import static ru.adk.service.model.ServiceInterceptionResult.*;
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