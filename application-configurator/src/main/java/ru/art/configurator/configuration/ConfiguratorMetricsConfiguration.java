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

package ru.art.configurator.configuration;

import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.Getter;
import ru.art.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static ru.art.config.ConfigProvider.config;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.DEFAULT_CONFIGURATOR_PATH;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.art.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.*;
import static ru.art.core.extension.ExceptionExtensions.ifExceptionOrEmpty;
import static ru.art.metrics.configurator.PrometheusRegistryConfigurator.prometheusRegistryForApplication;

@Getter
public class ConfiguratorMetricsConfiguration extends MetricModuleDefaultConfiguration {
    private final String path = ifExceptionOrEmpty(() -> config(CONFIGURATOR_SECTION_ID).getString(CONFIGURATOR_HTTP_PATH_PROPERTY), DEFAULT_CONFIGURATOR_PATH);
    private final PrometheusMeterRegistry prometheusMeterRegistry = prometheusRegistryForApplication(CONFIGURATOR_MODULE_ID);
}
