package ru.adk.metrics.configuration;

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
import ru.adk.core.module.ModuleConfiguration;
import static io.micrometer.prometheus.PrometheusConfig.DEFAULT;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.core.factory.CollectionsFactory.setOf;
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
