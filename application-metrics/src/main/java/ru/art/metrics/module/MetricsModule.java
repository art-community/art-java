package ru.art.metrics.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.metrics.configuration.MetricModuleConfiguration;
import ru.art.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.core.context.Context.context;
import static ru.art.metrics.constants.MetricsModuleConstants.METRICS_MODULE_ID;

@Getter
public class MetricsModule implements Module<MetricModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final MetricModuleConfiguration metricsModule = context().getModule(METRICS_MODULE_ID, new MetricsModule());
    private final MetricModuleConfiguration defaultConfiguration = new MetricModuleDefaultConfiguration();
    private final String id = METRICS_MODULE_ID;

    public static MetricModuleConfiguration metricsModule() {
        return getMetricsModule();
    }

    @Override
    public void onLoad() {
        metricsModule().getMeterBinders().forEach(meter -> meter.bindTo(metricsModule().getPrometheusMeterRegistry()));
    }
}
