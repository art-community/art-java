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
        return builder.build();
    }
}
