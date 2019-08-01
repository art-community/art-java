package ru.art.config.remote.loader;

import lombok.NoArgsConstructor;
import ru.art.configurator.api.entity.Configuration;
import ru.art.entity.Entity;
import ru.art.service.ServiceController;
import static lombok.AccessLevel.PRIVATE;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.CONFIGURATION_IS_EMPTY;
import static ru.art.configurator.api.constants.ConfiguratorProxyServiceConstants.CONFIGURATOR_COMMUNICATION_SERVICE_ID;
import static ru.art.configurator.api.constants.ConfiguratorServiceConstants.Methods.GET_PROTOBUF_CONFIG;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.logging.LoggingModule.loggingModule;
import java.util.Optional;

@NoArgsConstructor(access = PRIVATE)
public class RemoteConfigLoader {
    public static Entity loadRemoteConfig() {
        Optional<Entity> configuration = ServiceController.<Configuration>executeServiceMethod(CONFIGURATOR_COMMUNICATION_SERVICE_ID, GET_PROTOBUF_CONFIG)
                .map(Configuration::getConfiguration);
        if (!configuration.isPresent()) {
            loggingModule().getLogger(RemoteConfigLoader.class).warn(CONFIGURATION_IS_EMPTY);
        }
        return configuration.orElse(entityBuilder().build());
    }
}