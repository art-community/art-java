/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.server.service.wrapper;

import io.art.server.model.*;
import lombok.experimental.*;
import static io.art.server.constants.ServerModuleConstants.ServiceExecutionFeatureTarget.*;
import static io.art.server.module.ServerModule.*;
import java.util.concurrent.*;

@UtilityClass
public class ServiceExecutionWrapper {
    public static <ResponseType> ResponseType executeServiceWithConfiguration(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        return executeServiceWithBreaker(serviceExecution, command, executionConfiguration);
    }

    public static <ResponseType> ResponseType executeServiceWithBreaker(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isBreakable()) {
            return executeServiceWithRateLimiter(serviceExecution, command, executionConfiguration);
        }
        return serviceModule()
                .getCircuitBreakerRegistry()
                .circuitBreaker(executionConfiguration.getCircuitBreakTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getCircuitBreakerConfig())
                .executeCallable(() -> executeServiceWithRateLimiter(serviceExecution, command, executionConfiguration));
    }

    public static <ResponseType> ResponseType executeServiceWithRateLimiter(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isLimited()) {
            return executeServiceWithBulkHeaded(serviceExecution, command, executionConfiguration);
        }
        return serviceModule()
                .getRateLimiterRegistry()
                .rateLimiter(executionConfiguration.getRateLimiterTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getRateLimiterConfig())
                .executeCallable(() -> executeServiceWithBulkHeaded(serviceExecution, command, executionConfiguration));
    }

    public static <ResponseType> ResponseType executeServiceWithBulkHeaded(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isBulkHeaded()) {
            return executeServiceWithRetrying(serviceExecution, command, executionConfiguration);
        }
        return serviceModule()
                .getBulkheadRegistry()
                .bulkhead(executionConfiguration.getBulkheadTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getBulkheadConfig())
                .executeCallable(() -> executeServiceWithRetrying(serviceExecution, command, executionConfiguration));
    }

    public static <ResponseType> ResponseType executeServiceWithRetrying(Callable<ResponseType> serviceExecution, ServiceMethodCommand command, ServiceExecutionConfiguration executionConfiguration) throws Exception {
        if (!executionConfiguration.isRetryable()) {
            return serviceExecution.call();
        }
        return serviceModule()
                .getRetryRegistry()
                .retry(executionConfiguration.getRetryTarget() == METHOD ? command.toString() : command.getServiceId(), executionConfiguration.getRetryConfig())
                .executeCallable(serviceExecution);
    }
}
