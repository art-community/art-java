package ru.adk.configurator.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.adk.configurator.api.entity.ModuleKey;
import ru.adk.entity.Entity;
import ru.adk.entity.Value;
import static java.util.Objects.isNull;
import static java.util.Optional.empty;
import static ru.adk.configurator.constants.ConfiguratorDbConstants.APPLICATION;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.*;
import static ru.adk.configurator.dao.ConfiguratorDao.getConfig;
import static ru.adk.core.checker.CheckerForEmptiness.isEmpty;
import static ru.adk.entity.Value.asPrimitive;
import java.util.Optional;

public interface ApplicationModulesParametersProvider {
    static Optional<ApplicationModuleParameters> getApplicationModuleParameters(ModuleKey moduleKey) {
        Optional<Entity> entityOptional = getConfig(APPLICATION).map(Value::asEntity);
        if (!entityOptional.isPresent()) {
            return empty();
        }
        Entity configuration = entityOptional.get();
        String balancerHost = configuration.getString(APPLICATION_BALANCER_HOST_CONFIG_KEY);
        if (isEmpty(balancerHost)) {
            return empty();
        }
        Integer balancerPort = asPrimitive(configuration.getValue(APPLICATION_BALANCER_PORT_CONFIG_KEY)).parseInt();
        if (isNull(balancerPort)) {
            return empty();
        }
        return getConfig(moduleKey.formatKey())
                .map(Value::asEntity)
                .map(moduleEntity -> moduleEntity.findString(APPLICATION_MODULE_GRPC_PATH_CONFIG_KEY))
                .map(path -> new ApplicationModuleParameters(balancerHost, balancerPort, path));
    }

    @Getter
    @AllArgsConstructor
    class ApplicationModuleParameters {
        private final String balancerHost;
        private final int balancerPort;
        private final String path;
    }
}