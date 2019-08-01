package ru.art.tarantool.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.tarantool.configuration.TarantoolConfiguration.TarantoolEntityMapping;
import ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInitializationMode;
import ru.art.tarantool.exception.TarantoolConnectionException;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import static ru.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_CONNECTION_TIMEOUT;
import static ru.art.tarantool.constants.TarantoolModuleConstants.DEFAULT_TARANTOOL_PROBE_CONNECTION_TIMEOUT;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.CONFIGURATION_IS_NULL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.ExceptionMessages.ENTITY_MAPPING_IS_NULL;
import static ru.art.tarantool.constants.TarantoolModuleConstants.TarantoolInitializationMode.ON_MODULE_LOAD;
import static ru.art.tarantool.module.TarantoolModule.tarantoolModule;
import java.util.Map;

public interface TarantoolModuleConfiguration extends ModuleConfiguration {
    Map<String, TarantoolConfiguration> getTarantoolConfigurations();

    TarantoolLocalConfiguration getLocalConfiguration();

    long getProbeConnectionTimeout();

    long getConnectionTimeout();

    boolean isEnableTracing();

    TarantoolInitializationMode getInitializationMode();

    @Getter
    class TarantoolModuleDefaultConfiguration implements TarantoolModuleConfiguration {
        private final Map<String, TarantoolConfiguration> tarantoolConfigurations = mapOf();
        private final long connectionTimeout = DEFAULT_TARANTOOL_CONNECTION_TIMEOUT;
        private final long probeConnectionTimeout = DEFAULT_TARANTOOL_PROBE_CONNECTION_TIMEOUT;
        private final boolean enableTracing = false;
        private final TarantoolLocalConfiguration localConfiguration = TarantoolLocalConfiguration.builder().build();
        private final TarantoolInitializationMode initializationMode = ON_MODULE_LOAD;
    }

    static TarantoolEntityMapping entityMapping(String instanceId, String entity) {
        TarantoolConfiguration tarantoolConfiguration = tarantoolModule().getTarantoolConfigurations().get(instanceId);
        if (isNull(tarantoolConfiguration)) {
            throw new TarantoolConnectionException(format(CONFIGURATION_IS_NULL, instanceId));
        }
        Map<String, TarantoolEntityMapping> entityMapping = tarantoolConfiguration.getEntityMapping();
        if (isNull(entityMapping)) {
            throw new TarantoolConnectionException(format(ENTITY_MAPPING_IS_NULL, instanceId));
        }
        return entityMapping.get(entity);
    }
}
