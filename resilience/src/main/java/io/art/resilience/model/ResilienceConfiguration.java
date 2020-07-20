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

package io.art.resilience.model;

import io.art.core.module.*;
import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.*;
import io.github.resilience4j.ratelimiter.*;
import io.github.resilience4j.retry.*;
import io.github.resilience4j.timelimiter.*;
import lombok.Builder;
import lombok.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.resilience.constants.ResilienceModuleConstants.ConfigurationKeys.*;
import static io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.*;
import static io.github.resilience4j.retry.RetryConfig.*;
import static java.time.Duration.*;

@Getter
@Builder
public class ResilienceConfiguration {
    private final RetryConfig retry;
    private final CircuitBreakerConfig circuitBreaker;
    private final RateLimiterConfig rateLimiter;
    private final BulkheadConfig bulkhead;
    private final TimeLimiterConfig timeLimiter;

    public static ResilienceConfiguration from(ModuleConfigurationSource source) {
        ResilienceConfigurationBuilder builder = ResilienceConfiguration.builder();
        boolean hasRetry = source.has(RETRY_KEY);
        boolean hasCircuitBreaker = source.has(CIRCUIT_BREAKER_KEY);
        boolean hasRateLimiter = source.has(RATE_LIMITER_KEY);
        boolean hasBulkhead = source.has(BULKHEAD_KEY);
        boolean hasTimeLimiter = source.has(TIME_LIMITER_KEY);
        if (hasRetry) {
            RetryConfig defaults = RetryConfig.ofDefaults();
            builder.retry(RetryConfig.custom()
                    .maxAttempts(getOrElse(source.getInt(RETRY_MAX_ATTEMPTS_KEY), defaults.getMaxAttempts()))
                    .waitDuration(getOrElse(source.getDuration(RETRY_WAIT_DURATION_KEY), ofMillis(DEFAULT_WAIT_DURATION)))
                    .build());
        }
        if (hasCircuitBreaker) {
            CircuitBreakerConfig defaults = CircuitBreakerConfig.ofDefaults();
            builder.circuitBreaker(CircuitBreakerConfig.custom()
                    .automaticTransitionFromOpenToHalfOpenEnabled(
                            getOrElse(source.getBool(CIRCUIT_BREAKER_AUTOMATIC_TRANSITION_FROM_OPEN_TO_HALF_OPEN_ENABLED_KEY), defaults.isAutomaticTransitionFromOpenToHalfOpenEnabled())
                    )
                    .failureRateThreshold(
                            getOrElse(source.getFloat(CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD_KEY), defaults.getFailureRateThreshold())
                    )
                    .permittedNumberOfCallsInHalfOpenState(
                            getOrElse(source.getInt(CIRCUIT_BREAKER_PERMITTED_NUMBER_OF_CALLS_IN_HALF_OPEN_STATE_KEY), defaults.getPermittedNumberOfCallsInHalfOpenState())
                    )
                    .minimumNumberOfCalls(
                            getOrElse(source.getInt(CIRCUIT_BREAKER_SLIDING_WINDOW_MINIMUM_NUMBER_OF_CALLS_KEY), defaults.getMinimumNumberOfCalls())
                    )
                    .slidingWindowSize(
                            getOrElse(source.getInt(CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE_KEY), defaults.getSlidingWindowSize())
                    )
                    .slidingWindowType(
                            SlidingWindowType.valueOf(getOrElse(source.getString(CIRCUIT_BREAKER_SLIDING_WINDOW_TYPE_KEY), defaults.getSlidingWindowType().name()).toUpperCase())
                    )
                    .slowCallDurationThreshold(
                            getOrElse(source.getDuration(CIRCUIT_BREAKER_SLOW_CALL_DURATION_THRESHOLD_KEY), defaults.getSlowCallDurationThreshold())
                    )
                    .slowCallRateThreshold(
                            getOrElse(source.getFloat(CIRCUIT_BREAKER_SLOW_CALL_RATE_THRESHOLD_KEY), defaults.getSlowCallRateThreshold())
                    )
                    .waitDurationInOpenState(
                            getOrElse(source.getDuration(CIRCUIT_BREAKER_WAIT_DURATION_IN_OPEN_STATE_KEY), defaults.getWaitDurationInOpenState())
                    )
                    .build());
        }
        if (hasRateLimiter) {
            RateLimiterConfig defaults = RateLimiterConfig.ofDefaults();
            builder.rateLimiter(RateLimiterConfig.custom()
                    .limitForPeriod(getOrElse(source.getInt(RATE_LIMITER_LIMIT_FOR_PERIOD_KEY), defaults.getLimitForPeriod()))
                    .limitRefreshPeriod(getOrElse(source.getDuration(RATE_LIMITER_LIMIT_REFRESH_PERIOD_KEY), defaults.getLimitRefreshPeriod()))
                    .timeoutDuration(getOrElse(source.getDuration(RATE_LIMITER_TIMEOUT_DURATION_KEY), defaults.getTimeoutDuration()))
                    .writableStackTraceEnabled(getOrElse(source.getBool(RATE_LIMITER_WRITABLE_STACK_TRACE_ENABLED_KEY), defaults.isWritableStackTraceEnabled()))
                    .build());
        }
        if (hasBulkhead) {
            BulkheadConfig defaults = BulkheadConfig.ofDefaults();
            builder.bulkhead(BulkheadConfig.custom()
                    .maxConcurrentCalls(getOrElse(source.getInt(BULKHEAD_MAX_CONCURRENT_CALLS_KEY), defaults.getMaxConcurrentCalls()))
                    .maxWaitDuration(getOrElse(source.getDuration(BULKHEAD_MAX_WAIT_DURATION_KEY), defaults.getMaxWaitDuration()))
                    .writableStackTraceEnabled(getOrElse(source.getBool(BULKHEAD_WRITABLE_STACK_TRACE_ENABLED_KEY), defaults.isWritableStackTraceEnabled()))
                    .build());
        }
        if (hasTimeLimiter) {
            TimeLimiterConfig defaults = TimeLimiterConfig.ofDefaults();
            builder.timeLimiter(TimeLimiterConfig.custom()
                    .timeoutDuration(getOrElse(source.getDuration(TIME_LIMITER_TIMEOUT_DURATION_KEY), defaults.getTimeoutDuration()))
                    .cancelRunningFuture(getOrElse(source.getBool(TIME_LIMITER_CANCEL_RUNNING_FUTURE_KEY), defaults.shouldCancelRunningFuture()))
                    .build());
        }
        return builder.build();
    }
}
