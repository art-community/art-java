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

package ru.art.metrics.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleState;
import ru.art.metrics.configuration.MetricModuleConfiguration;
import ru.art.metrics.configuration.MetricModuleConfiguration.MetricModuleDefaultConfiguration;
import static ru.art.core.context.Context.context;
import static ru.art.metrics.constants.MetricsModuleConstants.METRICS_MODULE_ID;

@Getter
public class MetricsModule implements Module<MetricModuleConfiguration, ModuleState> {
    private final MetricModuleConfiguration defaultConfiguration = new MetricModuleDefaultConfiguration();
    private final String id = METRICS_MODULE_ID;

    public static MetricModuleConfiguration metricsModule() {
        return context().getModule(METRICS_MODULE_ID, MetricsModule::new);
    }

    @Override
    public void onLoad() {
        metricsModule().getMeterBinders().forEach(meter -> meter.bindTo(metricsModule().getPrometheusMeterRegistry()));
    }
}
