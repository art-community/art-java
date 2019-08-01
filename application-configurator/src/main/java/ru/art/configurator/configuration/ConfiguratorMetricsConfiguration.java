package ru.art.configurator.configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Getter;
import ru.art.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.HTTP_PATH;
import static ru.art.metrics.configurator.PrometheusRegistryConfigurator.prometheusRegistryForApplication;

@Getter
public class ConfiguratorMetricsConfiguration extends MetricModuleDefaultConfiguration {
    private final String path = HTTP_PATH;
    private final PrometheusMeterRegistry prometheusMeterRegistry = prometheusRegistryForApplication(CONFIGURATOR_MODULE_ID);
}
