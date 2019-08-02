/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.metrics.configuration;

import io.github.mweirauch.micrometer.jvm.extras.ProcessMemoryMetrics;
import io.github.mweirauch.micrometer.jvm.extras.ProcessThreadMetrics;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.binder.system.UptimeMetrics;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Getter;
import lombok.Setter;
import ru.art.core.module.ModuleConfiguration;
import static io.micrometer.prometheus.PrometheusConfig.DEFAULT;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.core.factory.CollectionsFactory.setOf;
import java.util.Set;

public interface MetricModuleConfiguration extends ModuleConfiguration {
    Set<MeterBinder> getMeterBinders();

    String getPath();

    PrometheusMeterRegistry getPrometheusMeterRegistry();

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
        @Setter(value = PRIVATE)
        private String path = EMPTY_STRING;
    }
}
