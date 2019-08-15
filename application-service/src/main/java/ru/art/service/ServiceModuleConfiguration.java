/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.service;

import io.github.resilience4j.bulkhead.BulkheadRegistry;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.art.core.module.ModuleConfiguration;
import ru.art.service.interceptor.ServiceExecutionInterceptor.RequestInterceptor;
import ru.art.service.interceptor.ServiceExecutionInterceptor.ResponseInterceptor;
import ru.art.service.interceptor.ServiceValidationInterception;
import ru.art.service.validation.Validator;
import static java.text.MessageFormat.format;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.factory.CollectionsFactory.linkedListOf;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.service.ServiceExceptionWrapperBuilder.exceptionWrapperBuilder;
import static ru.art.service.constants.ServiceLoggingMessages.SERVICE_REGISTRATION_MESSAGE;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.interceptRequest;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.interceptResponse;
import java.util.List;
import java.util.Map;


public interface ServiceModuleConfiguration extends ModuleConfiguration {
    ServiceRegistry getServiceRegistry();

    Validator getValidator();

    List<RequestInterceptor> getRequestInterceptors();

    List<ResponseInterceptor> getResponseInterceptors();

    ServiceExecutionExceptionWrapper getExceptionWrapper();

    CircuitBreakerRegistry getCircuitBreakerRegistry();

    RateLimiterRegistry getRateLimiterRegistry();

    RetryRegistry getRetryRegistry();

    BulkheadRegistry getBulkheadRegistry();

    ServiceModuleDefaultConfiguration DEFAULT_CONFIGURATION = new ServiceModuleDefaultConfiguration();

	@Getter
	class ServiceModuleDefaultConfiguration implements ServiceModuleConfiguration {
        private final ServiceRegistry serviceRegistry = new ServiceRegistry();
        private final CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
        private final RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();
        private final RetryRegistry retryRegistry = RetryRegistry.ofDefaults();
        private final BulkheadRegistry bulkheadRegistry = BulkheadRegistry.ofDefaults();
        private final Validator validator = new Validator();
        @Getter
        private final List<RequestInterceptor> requestInterceptors = linkedListOf(interceptRequest(new ServiceLoggingInterception()), interceptRequest(new ServiceValidationInterception()));
        @Getter
        private final List<ResponseInterceptor> responseInterceptors = linkedListOf(interceptResponse(new ServiceLoggingInterception()));
        @Getter(lazy = true)
        private final ServiceExecutionExceptionWrapper exceptionWrapper = exceptionWrapperBuilder()
                .addExceptionWrapper(new NpeWrapper())
                .addExceptionWrapper(new InternalExceptionWrapper())
                .addExceptionWrapper(new UnknownServiceMethodExceptionWrapper())
                .addExceptionWrapper(new ChildServiceExceptionWrapper())
                .setThrowableExceptionWrapper(new RuntimeExceptionWrapper())
                .build();
    }

    @NoArgsConstructor(access = PRIVATE)
    class ServiceRegistry {
        @Getter
        private final Map<String, Specification> services = mapOf();

        public Specification getService(String serviceId) {
            return services.get(serviceId);
        }

        public ServiceRegistry registerService(Specification specification) {
            loggingModule()
                    .getLogger(ServiceRegistry.class)
                    .info(format(SERVICE_REGISTRATION_MESSAGE, specification.getServiceId(), specification.getClass().getName()));
            services.put(specification.getServiceId(), specification);
            return this;
        }
    }
}
