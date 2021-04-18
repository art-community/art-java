/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.resilience.constants;

public interface ResilienceModuleConstants {
    interface ConfigurationKeys {
        String RESILIENCE_SECTION = "resilience";

        String CIRCUIT_BREAKER_KEY = "circuitBreaker";
        String CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD_KEY = "circuitBreaker.failureRateThreshold";
        String CIRCUIT_BREAKER_PERMITTED_NUMBER_OF_CALLS_IN_HALF_OPEN_STATE_KEY = "circuitBreaker.permittedNumberOfCallsInHalfOpenState";
        String CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE_KEY = "circuitBreaker.slidingWindow.size";
        String CIRCUIT_BREAKER_SLIDING_WINDOW_TYPE_KEY = "circuitBreaker.slidingWindow.type";
        String CIRCUIT_BREAKER_SLOW_CALL_RATE_THRESHOLD_KEY = "circuitBreaker.slowCallRateThreshold";
        String CIRCUIT_BREAKER_SLOW_CALL_DURATION_THRESHOLD_KEY = "circuitBreaker.slowCallDurationThreshold";
        String CIRCUIT_BREAKER_SLIDING_WINDOW_MINIMUM_NUMBER_OF_CALLS_KEY = "circuitBreaker.slidingWindow.minimumNumberOfCalls";
        String CIRCUIT_BREAKER_WAIT_DURATION_IN_OPEN_STATE_KEY = "circuitBreaker.waitDurationInOpenState";
        String CIRCUIT_BREAKER_AUTOMATIC_TRANSITION_FROM_OPEN_TO_HALF_OPEN_ENABLED_KEY = "circuitBreaker.automaticTransitionFromOpenToHalfOpenEnabled";

        String RATE_LIMITER_KEY = "rateLimiter";
        String RATE_LIMITER_LIMIT_FOR_PERIOD_KEY = "rateLimiter.limitForPeriod";
        String RATE_LIMITER_LIMIT_REFRESH_PERIOD_KEY = "rateLimiter.limitRefreshPeriod";
        String RATE_LIMITER_TIMEOUT_DURATION_KEY = "rateLimiter.timeoutDuration";
        String RATE_LIMITER_WRITABLE_STACK_TRACE_ENABLED_KEY = "rateLimiter.writableStackTraceEnabled";

        String BULKHEAD_KEY = "bulkhead";
        String BULKHEAD_MAX_CONCURRENT_CALLS_KEY = "bulkhead.maxConcurrentCalls";
        String BULKHEAD_MAX_WAIT_DURATION_KEY = "bulkhead.maxWaitDuration";
        String BULKHEAD_WRITABLE_STACK_TRACE_ENABLED_KEY = "bulkhead.writableStackTraceEnabled";

        String RETRY_KEY = "retry";
        String RETRY_MAX_ATTEMPTS_KEY = "retry.maxAttempts";
        String RETRY_WAIT_DURATION_KEY = "retry.waitDuration";

        String TIME_LIMITER_KEY = "timeLimiter";
        String TIME_LIMITER_TIMEOUT_DURATION_KEY = "timeLimiter.timeoutDuration";
        String TIME_LIMITER_CANCEL_RUNNING_FUTURE_KEY = "timeLimiter.cancelRunningFuture";
    }
}
