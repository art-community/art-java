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

package io.art.resilience.module;

import io.art.core.module.*;
import io.art.resilience.configuration.*;
import io.art.resilience.state.*;
import io.github.resilience4j.bulkhead.*;
import io.github.resilience4j.circuitbreaker.*;
import io.github.resilience4j.ratelimiter.*;
import io.github.resilience4j.retry.*;
import io.github.resilience4j.timelimiter.*;
import lombok.*;
import static io.art.core.context.Context.*;

@Getter
public class ResilienceModule implements StatefulModule<ResilienceModuleConfiguration, ResilienceModuleConfiguration.Configurator, ResilienceModuleState> {
    private final String id = ResilienceModule.class.getSimpleName();
    private final ResilienceModuleConfiguration configuration = new ResilienceModuleConfiguration();
    private final ResilienceModuleConfiguration.Configurator configurator = new ResilienceModuleConfiguration.Configurator(configuration);
    private final ResilienceModuleState state = new ResilienceModuleState();
    @Getter(lazy = true)
    private static final
    StatefulModuleProxy<ResilienceModuleConfiguration, ResilienceModuleState> resilienceModule = context().getStatefulModule(ResilienceModule.class.getSimpleName());

    public static StatefulModuleProxy<ResilienceModuleConfiguration, ResilienceModuleState> resilienceModule() {
        return getResilienceModule();
    }

    public static Retry retry(String id, RetryConfig config) {
        return resilienceModule()
                .state()
                .getRetriers()
                .retry(id, config);
    }

    public static CircuitBreaker circuitBreaker(String id, CircuitBreakerConfig config) {
        return resilienceModule()
                .state()
                .getCircuitBreakers()
                .circuitBreaker(id, config);
    }

    public static RateLimiter rateLimiter(String id, RateLimiterConfig config) {
        RateLimiter rateLimiter = resilienceModule()
                .state()
                .getRateLimiters()
                .rateLimiter(id, config);
        rateLimiter.changeLimitForPeriod(config.getLimitForPeriod());
        rateLimiter.changeTimeoutDuration(config.getTimeoutDuration());
        return rateLimiter;
    }

    public static Bulkhead bulkhead(String id, BulkheadConfig config) {
        Bulkhead bulkhead = resilienceModule()
                .state()
                .getBulkheads()
                .bulkhead(id, config);
        bulkhead.changeConfig(config);
        return bulkhead;
    }

    public static TimeLimiter timeLimiter(String id, TimeLimiterConfig config) {
        return resilienceModule()
                .state()
                .getTimeLimiters()
                .timeLimiter(id, config);
    }
}
