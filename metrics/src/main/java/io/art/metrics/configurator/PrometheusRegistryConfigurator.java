/*
 * ART
 *
 * Copyright 2019-2022 ART
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

package io.art.metrics.configurator;

import com.codahale.metrics.*;
import io.micrometer.prometheus.*;
import io.prometheus.client.*;
import io.prometheus.client.dropwizard.*;
import lombok.experimental.*;
import static io.micrometer.prometheus.PrometheusConfig.*;
import static io.art.metrics.constants.MetricsModuleConstants.*;
import static io.art.metrics.module.MetricsModule.*;

@UtilityClass
public class PrometheusRegistryConfigurator {
    @SuppressWarnings("Duplicates")
    public static PrometheusMeterRegistry prometheusRegistryForApplication(String applicationId) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(DEFAULT);
        CollectorRegistry prometheusRegistry = registry.getPrometheusRegistry();
        prometheusRegistry.register(new DropwizardExports(metricsModule().getDropwizardMetricRegistry()));
        registry.config().commonTags(MODULE_TAG, applicationId);
        return registry;
    }

    @SuppressWarnings("Duplicates")
    public static PrometheusMeterRegistry prometheusRegistryForApplication(String applicationId, PrometheusConfig config) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(config);
        registry.getPrometheusRegistry().register(new DropwizardExports(metricsModule().getDropwizardMetricRegistry()));
        registry.config().commonTags(MODULE_TAG, applicationId);
        return registry;
    }

    @SuppressWarnings("Duplicates")
    public static PrometheusMeterRegistry prometheusRegistryForApplication(String applicationId, MetricRegistry metricRegistry) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(DEFAULT);
        CollectorRegistry prometheusRegistry = registry.getPrometheusRegistry();
        prometheusRegistry.register(new DropwizardExports(metricRegistry));
        registry.config().commonTags(MODULE_TAG, applicationId);
        return registry;
    }

    @SuppressWarnings("Duplicates")
    public static PrometheusMeterRegistry prometheusRegistryForApplication(String applicationId, MetricRegistry metricRegistry, PrometheusConfig config) {
        PrometheusMeterRegistry registry = new PrometheusMeterRegistry(config);
        registry.getPrometheusRegistry().register(new DropwizardExports(metricRegistry));
        registry.config().commonTags(MODULE_TAG, applicationId);
        return registry;
    }
}
