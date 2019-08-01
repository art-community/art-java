package ru.adk.service.execution;

import ru.adk.service.model.ServiceExecutionConfiguration;
import ru.adk.service.model.ServiceMethodCommand;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.constants.ServiceExecutionFeatureTarget.METHOD;
import java.util.concurrent.Callable;

public interface ServiceExecutor {
    static <ResponseType> ResponseType executeServiceWithConfiguration(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        return executeServiceWithBreaker(serviceExecution, command, executionConfiguration);
    }

    static <ResponseType> ResponseType executeServiceWithBreaker(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isBreakable()) {
            return executeServiceWithRateLimiter(serviceExecution, command, executionConfiguration);
        }
        return serviceModule()
                .getCircuitBreakerRegistry()
                .circuitBreaker(executionConfiguration.getCircuitBreakTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getCircuitBreakerConfig())
                .executeCallable(() -> executeServiceWithRateLimiter(serviceExecution, command, executionConfiguration));
    }

    static <ResponseType> ResponseType executeServiceWithRateLimiter(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isLimited()) {
            return executeServiceWithBulkHeaded(serviceExecution, command, executionConfiguration);
        }
        return serviceModule()
                .getRateLimiterRegistry()
                .rateLimiter(executionConfiguration.getRateLimiterTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getRateLimiterConfig())
                .executeCallable(() -> executeServiceWithBulkHeaded(serviceExecution, command, executionConfiguration));
    }

    static <ResponseType> ResponseType executeServiceWithBulkHeaded(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isBulkHeaded()) {
            return executeServiceWithRetrying(serviceExecution, command, executionConfiguration);
        }
        return serviceModule()
                .getBulkheadRegistry()
                .bulkhead(executionConfiguration.getBulkheadTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getBulkheadConfig())
                .executeCallable(() -> executeServiceWithRetrying(serviceExecution, command, executionConfiguration));
    }

    static <ResponseType> ResponseType executeServiceWithRetrying(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isRetryable()) {
            return serviceExecution.call();
        }
        return serviceModule()
                .getRetryRegistry()
                .retry(executionConfiguration.getRetryTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getRetryConfig())
                .executeCallable(serviceExecution);
    }
}
