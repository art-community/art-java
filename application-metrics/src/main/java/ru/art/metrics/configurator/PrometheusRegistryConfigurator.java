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

package ru.art.metrics.configurator;

import com.codahale.metrics.*;
import com.codahale.metrics.jvm.*;
import io.micrometer.prometheus.*;
import io.prometheus.client.*;
import io.prometheus.client.dropwizard.*;
import static io.github.resilience4j.metrics.CircuitBreakerMetrics.*;
import static io.github.resilience4j.metrics.RateLimiterMetrics.*;
import static io.github.resilience4j.metrics.RetryMetrics.*;
import static io.micrometer.prometheus.PrometheusConfig.*;
import static java.util.concurrent.TimeUnit.*;
import static ru.art.metrics.constants.MetricsModuleConstants.*;
import static ru.art.service.ServiceModule.*;

public interface PrometheusRegistryConfigurator {
    @SuppressWarnings("Duplicates")
    static PrometheusMeterRegistry prometheusRegistryForApplication(String applicationId) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(DEFAULT);
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.register(GC_METRICS, new GarbageCollectorMetricSet());
        metricRegistry.register(THREADS_METRICS, new CachedThreadStatesGaugeSet(THREAD_METRICS_INTERVAL_GAUGE_SET, SECONDS));
        metricRegistry.register(MEMORY_METRICS, new MemoryUsageGaugeSet());
        metricRegistry.register(CIRCUIT_BREAKER_METRICS, ofCircuitBreakerRegistry(serviceModule().getCircuitBreakerRegistry()));
        metricRegistry.register(RATE_LIMITER_METRICS, ofRateLimiterRegistry(serviceModule().getRateLimiterRegistry()));
        metricRegistry.register(RETRY_METRICS, ofRetryRegistry(serviceModule().getRetryRegistry()));
        CollectorRegistry prometheusRegistry = registry.getPrometheusRegistry();
        prometheusRegistry.register(new DropwizardExports(metricRegistry));
        registry.config().commonTags(MODULE_TAG, applicationId);
        return registry;
    }

    @SuppressWarnings("Duplicates")
    static PrometheusMeterRegistry prometheusRegistryForApplication(String applicationId, PrometheusConfig config) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(config);
        MetricRegistry metricRegistry = new MetricRegistry();
        metricRegistry.register(GC_METRICS, new GarbageCollectorMetricSet());
        metricRegistry.register(THREADS_METRICS, new CachedThreadStatesGaugeSet(THREAD_METRICS_INTERVAL_GAUGE_SET, SECONDS));
        metricRegistry.register(MEMORY_METRICS, new MemoryUsageGaugeSet());
        metricRegistry.register(CIRCUIT_BREAKER_METRICS, ofCircuitBreakerRegistry(serviceModule().getCircuitBreakerRegistry()));
        metricRegistry.register(RATE_LIMITER_METRICS, ofRateLimiterRegistry(serviceModule().getRateLimiterRegistry()));
        metricRegistry.register(RETRY_METRICS, ofRetryRegistry(serviceModule().getRetryRegistry()));
        registry.getPrometheusRegistry().register(new DropwizardExports(metricRegistry));
        registry.config().commonTags(MODULE_TAG, applicationId);
        return registry;
    }
}
