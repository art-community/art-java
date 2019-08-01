package ru.adk.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.RetryConfig;
import ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import ru.adk.service.model.*;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.service.constants.ServiceModuleConstants.DEFAULT_SERVICE_TYPE;
import static ru.adk.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
import java.util.List;
import java.util.Map;

public interface Specification {
    String getServiceId();

    <P, R> R executeMethod(String methodId, P request);

    default ServiceDeactivationConfig getDeactivationConfig() {
        return ServiceDeactivationConfig.builder().build();
    }

    default List<ServiceRequestInterceptor> getRequestInterceptors() {
        return ServiceModule.serviceModule().getRequestInterceptors();
    }

    default List<ServiceResponseInterceptor> getResponseInterceptors() {
        return serviceModule().getResponseInterceptors();
    }

    default ServiceExecutionExceptionWrapper getExceptionWrapper() {
        return serviceModule().getExceptionWrapper();
    }

    default ServiceExecutionConfiguration getServiceExecutionConfiguration() {
        CircuitBreakerServiceConfig circuitBreakerServiceConfig = CircuitBreakerServiceConfig
                .builder()
                .breakable(false)
                .circuitBreakerConfigBuilder(CircuitBreakerConfig.custom())
                .build();

        RateLimiterServiceConfig rateLimiterServiceConfig = RateLimiterServiceConfig
                .builder()
                .limited(false)
                .rateLimiterConfigBuilder(RateLimiterConfig.custom())
                .build();

        RetryServiceConfig retryServiceConfig = RetryServiceConfig
                .builder()
                .retryable(false)
                .retryConfigBuilder(RetryConfig.custom())
                .build();

        return ServiceExecutionConfiguration.builder()
                .circuitBreakerConfig(circuitBreakerServiceConfig)
                .rateLimiterConfig(rateLimiterServiceConfig)
                .retryConfig(retryServiceConfig)
                .build();
    }

    default Map<String, List<ServiceRequestInterceptor>> getMethodRequestInterceptors() {
        return mapOf();
    }

    default Map<String, List<ServiceResponseInterceptor>> getMethodResponseInterceptors() {
        return mapOf();
    }

    default List<String> getServiceTypes() {
        return fixedArrayOf(DEFAULT_SERVICE_TYPE);
    }
}
