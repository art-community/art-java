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

package io.art.metrics.configuration;

import com.codahale.metrics.*;
import io.github.mweirauch.micrometer.jvm.extras.*;
import io.micrometer.core.instrument.binder.*;
import io.micrometer.core.instrument.binder.jvm.*;
import io.micrometer.core.instrument.binder.system.*;
import io.micrometer.prometheus.*;
import lombok.*;
import io.art.core.module.*;
import static io.micrometer.prometheus.PrometheusConfig.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.metrics.factory.DropwizardMetricRegistryFactory.*;
import java.util.*;

public interface MetricModuleConfiguration extends ModuleConfiguration {
    Set<MeterBinder> getMeterBinders();

    PrometheusMeterRegistry getPrometheusMeterRegistry();

    MetricRegistry getDropwizardMetricRegistry();

    MetricModuleDefaultConfiguration DEFAULT_CONFIGURATION = new MetricModuleDefaultConfiguration();

    @Getter
    class MetricModuleDefaultConfiguration implements MetricModuleConfiguration {
        private final Set<MeterBinder> meterBinders = setOf(new ClassLoaderMetrics(),
                new JvmMemoryMetrics(),
                new JvmGcMetrics(),
                new ProcessorMetrics(),
                new JvmThreadMetrics(),
                new ProcessMemoryMetrics(),
                new ProcessThreadMetrics(),
                new UptimeMetrics());
        private final PrometheusMeterRegistry prometheusMeterRegistry = new PrometheusMeterRegistry(DEFAULT);
        private final MetricRegistry dropwizardMetricRegistry = createDefaultDropwizardMetricRegistry();
    }
}
