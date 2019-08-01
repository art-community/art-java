package ru.art.config.extensions.metrics;

import lombok.Getter;
import ru.art.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static ru.art.config.extensions.ConfigExtensions.configString;
import static ru.art.config.extensions.common.CommonConfigKeys.PATH;
import static ru.art.config.extensions.metrics.MetricsConfigKeys.METRICS_MODULE_SECTION_ID;

@Getter
public class MetricsAgileConfiguration extends MetricModuleDefaultConfiguration {
    private String path;

    public MetricsAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        path = configString(METRICS_MODULE_SECTION_ID, PATH, super.getPath());
    }
}
