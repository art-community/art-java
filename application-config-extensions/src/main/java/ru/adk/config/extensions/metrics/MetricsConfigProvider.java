package ru.adk.config.extensions.metrics;

import ru.adk.config.Config;
import static ru.adk.config.extensions.ConfigExtensions.configInner;
import static ru.adk.config.extensions.metrics.MetricsConfigKeys.METRICS_MODULE_SECTION_ID;

public interface MetricsConfigProvider {
    static Config metricsConfig() {
        return configInner(METRICS_MODULE_SECTION_ID);
    }
}
