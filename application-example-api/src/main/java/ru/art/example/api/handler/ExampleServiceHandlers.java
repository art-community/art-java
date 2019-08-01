package ru.art.example.api.handler;

import ru.art.example.api.model.ExampleRequest;
import ru.art.example.api.model.ExampleResponse;
import ru.art.service.model.ServiceResponse;
import static ru.art.logging.LoggingModule.loggingModule;
import java.util.Optional;


/**
 * handlers are needed to do something when asynchronous service execution failed or executed successfully
 */
public interface ExampleServiceHandlers {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handleRequestResponseHandlingExampleCompletion(Optional<ExampleRequest> request, ServiceResponse<ExampleResponse> response) {
        loggingModule().getLogger().info("SERVICE METHOD EXECUTED SUCCESSFULLY WITH REQUEST: " + request.toString());
        loggingModule().getLogger().info("SERVICE METHOD EXECUTED SUCCESSFULLY WITH RESPONSE: " + response.toString());
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    static void handleRequestResponseHandlingExampleError(Optional<ExampleRequest> request, Throwable exception) {
        loggingModule().getLogger().info("SERVICE METHOD FAILED WITH ERROR: " + exception.toString());
        loggingModule().getLogger().info("SERVICE METHOD FAILED WITH REQUEST: " + request.toString());
    }
}
