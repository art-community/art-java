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

package ru.art.config.extensions.service;

import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.*;
import io.github.resilience4j.ratelimiter.*;
import io.github.resilience4j.retry.*;
import lombok.experimental.*;
import ru.art.config.*;
import ru.art.core.annotation.*;
import ru.art.service.model.*;
import static io.github.resilience4j.bulkhead.BulkheadConfig.*;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.*;
import static io.github.resilience4j.retry.RetryConfig.*;
import static java.time.Duration.*;
import static java.util.Collections.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.service.RateLimiterDefaults.*;
import static ru.art.config.extensions.service.ServiceConfigKeys.*;
import static ru.art.core.constants.StringConstants.*;
import java.util.*;

@PublicApi
@UtilityClass
public class ServiceConfigProvider {
    public static CircuitBreakerServiceConfig getCircuitBreakerServiceConfig(String sectionId) {
        boolean breakable = false;
        CircuitBreakerConfig.Builder circuitBreakerBuilder = CircuitBreakerConfig.custom();
        if (hasPath(sectionId) && (breakable = configBoolean(sectionId, BREAKABLE))) {
            circuitBreakerBuilder
                    .failureRateThreshold(configDouble(sectionId, FAILURE_RATE_THRESHOLD, DEFAULT_MAX_FAILURE_THRESHOLD).floatValue())
                    .ringBufferSizeInClosedState(configInt(sectionId, RING_BUFFER_SIZE_IN_CLOSED_STATE, DEFAULT_RING_BUFFER_SIZE_IN_CLOSED_STATE))
                    .ringBufferSizeInHalfOpenState(configInt(sectionId, RING_BUFFER_SIZE_IN_HALF_OPEN_STATE, DEFAULT_RING_BUFFER_SIZE_IN_HALF_OPEN_STATE))
                    .waitDurationInOpenState(ofSeconds(configLong(sectionId, WAIT_DURATION_IN_OPEN_STATE, DEFAULT_WAIT_DURATION_IN_OPEN_STATE)));
            if (configBoolean(sectionId, AUTOMATIC_TRANSITION_FROM_OPEN_TO_HALF_OPEN_ENABLED)) {
                circuitBreakerBuilder.enableAutomaticTransitionFromOpenToHalfOpen();
            }
        }
        return CircuitBreakerServiceConfig.builder()
                .breakable(breakable)
                .circuitBreakerConfigBuilder(circuitBreakerBuilder)
                .build();
    }

    public static RateLimiterServiceConfig getRateLimiterServiceConfig(String sectionId) {
        boolean limited = false;
        RateLimiterConfig.Builder rateLimiterConfigBuilder = RateLimiterConfig.custom();
        if (hasPath(sectionId) && (limited = configBoolean(sectionId, LIMITED))) {
            rateLimiterConfigBuilder
                    .limitForPeriod(configInt(sectionId, LIMIT_FOR_PERIOD, DEFAULT_LIMIT_FOR_PERIOD))
                    .limitRefreshPeriod(ofNanos(configInt(sectionId, LIMIT_REFRESH_PERIOD, DEFAULT_LIMIT_REFRESH_PERIOD.getNano())))
                    .timeoutDuration(ofSeconds(configLong(sectionId, TIMEOUT_DURATION, DEFAULT_TIMEOUT_DURATION.getSeconds())));
        }
        return RateLimiterServiceConfig.builder()
                .limited(limited)
                .rateLimiterConfigBuilder(rateLimiterConfigBuilder)
                .build();
    }

    public static BulkheadServiceConfig getBulkheadServiceConfig(String sectionId) {
        boolean bulkheaded = false;
        BulkheadConfig.Builder bulkheadConfigBuilder = BulkheadConfig.custom();
        if (hasPath(sectionId) && (bulkheaded = configBoolean(sectionId, BULKHEADED))) {
            bulkheadConfigBuilder
                    .maxConcurrentCalls(configInt(sectionId, MAX_CONCURRENT_CALLS, DEFAULT_MAX_CONCURRENT_CALLS))
                    .maxWaitTime(configLong(sectionId, MAX_WAIT_TIME, DEFAULT_MAX_WAIT_TIME));
        }
        return BulkheadServiceConfig.builder()
                .bulkheaded(bulkheaded)
                .bulkheadConfigBuilder(bulkheadConfigBuilder)
                .build();
    }

    public static RetryServiceConfig getRetryServiceConfig(String sectionId) {
        boolean retryable = false;
        RetryConfig.Builder retryConfigBuilder = RetryConfig.custom();
        if (hasPath(sectionId) && (retryable = configBoolean(sectionId, RETRYABLE))) {
            retryConfigBuilder
                    .maxAttempts(configInt(sectionId, MAX_ATTEMPTS, DEFAULT_MAX_ATTEMPTS))
                    .waitDuration(ofMillis(configLong(sectionId, WAIT_DURATION, DEFAULT_WAIT_DURATION)));
        }
        return RetryServiceConfig.builder()
                .retryable(retryable)
                .retryConfigBuilder(retryConfigBuilder)
                .build();
    }

    public static DeactivationConfig getServiceDeactivationConfig(String serviceId) {
        if (!hasPath(DEACTIVATION)) return DeactivationConfig.builder().build();
        if (!hasPath(DEACTIVATION, serviceId)) return DeactivationConfig.builder().build();
        boolean deactivated = hasPath(DEACTIVATION, serviceId) || configBoolean(DEACTIVATION, serviceId + DOT + DEACTIVATED);
        List<String> deactivatedMethods = hasPath(DEACTIVATION + DOT + serviceId, METHODS)
                ? configStringList(DEACTIVATION + DOT + serviceId, METHODS)
                : emptyList();
        return DeactivationConfig.builder().deactivated(deactivated).deactivatedMethods(deactivatedMethods).build();
    }

    public static Config serviceConfig(String serviceId) {
        return configInner(SERVICE).getConfig(serviceId);
    }

    public static Config serviceConfig() {
        return configInner(SERVICE);
    }
}
