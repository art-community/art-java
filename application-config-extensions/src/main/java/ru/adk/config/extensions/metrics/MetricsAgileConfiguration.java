package ru.adk.config.extensions.metrics;

import lombok.Getter;
import ru.adk.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static ru.adk.config.extensions.ConfigExtensions.configString;
import static ru.adk.config.extensions.common.CommonConfigKeys.PATH;
import static ru.adk.config.extensions.metrics.MetricsConfigKeys.METRICS_MODULE_SECTION_ID;

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
