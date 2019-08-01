package ru.art.example.interceptor;

import ru.art.service.interceptor.ServiceRequestInterception;
import ru.art.service.interceptor.ServiceResponseInterception;
import ru.art.service.model.ServiceInterceptionResult;
import ru.art.service.model.ServiceRequest;
import ru.art.service.model.ServiceResponse;
import static ru.art.example.module.ExampleModule.exampleState;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.service.model.ServiceInterceptionResult.nextInterceptor;

/**
 * Interceptor can do some logic before service method execution
 * In this example interceptor increments state requestNumber
 */
public class ExampleServiceInterception implements ServiceRequestInterception, ServiceResponseInterception {

    /**
     * request interception can do something before executing service method
     */
    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        loggingModule().getLogger().info("Request interceptor executed");
        exampleState().incrementRequests();
        return nextInterceptor(request);
    }

    /**
     * response interception can do something after executing service method
     */
    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response) {
        loggingModule().getLogger().info("Request interceptor executed");
        loggingModule().getLogger().info("Service response: " + response.toString());
        return nextInterceptor(request, response);
    }

}