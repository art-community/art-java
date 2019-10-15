package ru.art.config.extensions.service;

import lombok.*;
import ru.art.service.ServiceModuleConfiguration.*;
import ru.art.service.model.*;
import static ru.art.config.extensions.ConfigExtensions.*;
import static ru.art.config.extensions.common.CommonConfigKeys.*;
import static ru.art.config.extensions.service.ServiceConfigProvider.*;
import static ru.art.core.constants.StringConstants.*;
import static ru.art.core.extension.ExceptionExtensions.*;
import java.util.*;

@Getter
public class ServiceAgileConfiguration extends ServiceModuleDefaultConfiguration {
    private final Map<String, ServiceExecutionConfiguration> executionConfigurations = super.getExecutionConfigurations();

    public ServiceAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        executionConfigurations.clear();
        if (!ifException(() -> hasPath(SERVICES), false)) {
            return;
        }
        configMap(SERVICES)
                .keySet()
                .forEach(serviceId -> executionConfigurations.put(serviceId,
                        ServiceExecutionConfiguration.builder()
                                .circuitBreakerConfig(getCircuitBreakerServiceConfig(SERVICES + DOT + serviceId))
                                .rateLimiterConfig(getRateLimiterServiceConfig(SERVICES + DOT + serviceId))
                                .retryConfig(getRetryServiceConfig(SERVICES + DOT + serviceId))
                                .bulkheadConfig(getBulkheadServiceConfig(SERVICES + DOT + serviceId))
                                .build()));
    }
}
