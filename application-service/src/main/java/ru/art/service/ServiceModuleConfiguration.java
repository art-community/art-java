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

import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.*;
import io.github.resilience4j.ratelimiter.*;
import io.github.resilience4j.retry.*;
import lombok.*;
import ru.art.core.module.*;
import ru.art.service.interceptor.ServiceExecutionInterceptor.*;
import ru.art.service.interceptor.*;
import ru.art.service.validation.*;
import java.util.*;

import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.logging.LoggingModule.*;
import static ru.art.service.ServiceExceptionWrapperBuilder.*;
import static ru.art.service.constants.ServiceLoggingMessages.*;
import static ru.art.service.interceptor.ServiceExecutionInterceptor.*;


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
