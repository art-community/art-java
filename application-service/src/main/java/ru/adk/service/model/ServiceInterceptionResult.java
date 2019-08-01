package ru.adk.service.model;

import lombok.Builder;
import lombok.Getter;
import ru.adk.core.constants.InterceptionStrategy;
import static ru.adk.core.constants.InterceptionStrategy.*;

@Getter
@Builder(builderMethodName = "interceptionResult")
public class ServiceInterceptionResult {
    @Builder.Default
    private final InterceptionStrategy nextInterceptionStrategy = NEXT_INTERCEPTOR;
    private final ServiceRequest<?> request;
    private ServiceResponse<?> response;

    public static ServiceInterceptionResult nextInterceptor(ServiceRequest<?> request) {
        return interceptionResult().request(request).build();
    }

    public static ServiceInterceptionResult nextInterceptor(ServiceRequest<?> request, ServiceResponse<?> response) {
        return interceptionResult().request(request).response(response).build();
    }

    public static ServiceInterceptionResult processHandling(ServiceRequest<?> request) {
        return interceptionResult().request(request).nextInterceptionStrategy(PROCESS_HANDLING).build();
    }

    public static ServiceInterceptionResult processHandling(ServiceRequest<?> request, ServiceResponse<?> response) {
        return interceptionResult().request(request).nextInterceptionStrategy(PROCESS_HANDLING).response(response).build();
    }

    public static ServiceInterceptionResult stopHandling(ServiceRequest<?> request) {
        return interceptionResult().request(request).nextInterceptionStrategy(STOP_HANDLING).build();
    }

    public static ServiceInterceptionResult stopHandling(ServiceRequest<?> request, ServiceResponse<?> response) {
        return interceptionResult().request(request).nextInterceptionStrategy(STOP_HANDLING).response(response).build();
    }
}
