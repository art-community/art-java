package ru.adk.service.interceptor;

import ru.adk.service.model.ServiceInterceptionResult;
import ru.adk.service.model.ServiceRequest;
import ru.adk.service.model.ServiceResponse;
import static ru.adk.service.model.ServiceInterceptionResult.*;
import java.util.function.BiConsumer;

@FunctionalInterface
public interface ServiceResponseInterception {
    static ServiceResponseInterception interceptAndContinue(BiConsumer<ServiceRequest<?>, ServiceResponse<?>> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return nextInterceptor(request, response);
        };
    }

    static ServiceResponseInterception interceptAndCall(BiConsumer<ServiceRequest<?>, ServiceResponse<?>> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return processHandling(request, response);
        };
    }

    static ServiceResponseInterception interceptAndReturn(BiConsumer<ServiceRequest<?>, ServiceResponse<?>> runnable) {
        return (request, response) -> {
            runnable.accept(request, response);
            return stopHandling(request, response);
        };
    }

    ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response);

}