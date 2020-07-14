/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.config.extensions.service;

import lombok.*;
import io.art.service.ServiceModuleConfiguration.*;
import io.art.service.model.*;
import static io.art.config.extensions.ConfigExtensions.*;
import static io.art.config.extensions.common.CommonConfigKeys.*;
import static io.art.config.extensions.service.ServiceConfigProvider.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extension.ExceptionExtensions.*;
import java.util.*;

@Getter
public class ServiceAgileConfiguration extends ServiceModuleDefaultConfiguration {
    private final Map<String, ServiceExecutionConfiguration> executionConfigurations = super.getExecutionConfigurations();
    private final Map<String, DeactivationConfig> deactivationConfigurations = super.getDeactivationConfigurations();

    public ServiceAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        executionConfigurations.clear();
        if (!ifException(() -> hasPath(SERVICES), false)) {
            return;
        }
        configInnerMap(SERVICES)
                .keySet()
                .stream()
                .peek(serviceId -> executionConfigurations.put(serviceId,
                        ServiceExecutionConfiguration.builder()
                                .circuitBreakerConfig(getCircuitBreakerServiceConfig(SERVICES + DOT + serviceId))
                                .rateLimiterConfig(getRateLimiterServiceConfig(SERVICES + DOT + serviceId))
                                .retryConfig(getRetryServiceConfig(SERVICES + DOT + serviceId))
                                .bulkheadConfig(getBulkheadServiceConfig(SERVICES + DOT + serviceId))
                                .build()))
                .forEach(serviceId -> deactivationConfigurations.put(serviceId, getServiceDeactivationConfig(SERVICES + DOT + serviceId)));
    }
}
