package ru.art.config.extensions.metrics;

import ru.art.config.Config;
import static ru.art.config.extensions.ConfigExtensions.configInner;
import static ru.art.config.extensions.metrics.MetricsConfigKeys.METRICS_MODULE_SECTION_ID;

public interface MetricsConfigProvider {
    static Config metricsConfig() {
        return configInner(METRICS_MODULE_SECTION_ID);
    }
}
