/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.service;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.retry.RetryConfig;
import ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceResponseInterceptor;
import ru.art.service.model.*;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.service.constants.ServiceModuleConstants.DEFAULT_SERVICE_TYPE;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.ServiceRequestInterceptor;
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

    default ServiceExecutionConfiguration getExecutionConfiguration() {
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
