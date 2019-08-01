package ru.art.config.remote.initializer;

import ru.art.config.Config;
import ru.art.config.module.ConfigModule;
import ru.art.config.remote.specification.RemoteConfigServiceSpecification;
import ru.art.configurator.api.specification.ConfiguratorProxyServiceSpecification;
import ru.art.core.context.Context;
import ru.art.grpc.client.module.GrpcClientModule;
import ru.art.logging.LoggingModule;
import ru.art.service.ServiceModule;
import static ru.art.config.ConfigProvider.config;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.CONFIGURATOR_CONNECTION_PROPERTIES_NOT_EXISTS;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.LocalConfigKeys.CONFIGURATOR_HOST;
import static ru.art.config.remote.constants.RemoteConfigLoaderConstants.LocalConfigKeys.CONFIGURATOR_PORT;
import static ru.art.core.constants.StringConstants.EMPTY_STRING;
import static ru.art.logging.LoggingModule.loggingModule;
import static ru.art.service.ServiceModule.serviceModule;

public interface RemoteConfigInitializer {
    static Context withRemoteConfig(Context context) {
        context.loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new ConfigModule())
                .loadModule(new GrpcClientModule());
        Config applicationConfig = config(EMPTY_STRING);
        if (!applicationConfig.hasPath(CONFIGURATOR_HOST) || !applicationConfig.hasPath(CONFIGURATOR_PORT)) {
            loggingModule().getLogger(RemoteConfigInitializer.class).warn(CONFIGURATOR_CONNECTION_PROPERTIES_NOT_EXISTS);
            return context;
        }
        serviceModule()
                .getServiceRegistry()
                .registerService(new ConfiguratorProxyServiceSpecification(applicationConfig.getString(CONFIGURATOR_HOST), applicationConfig.getInt(CONFIGURATOR_PORT)))
                .registerService(new RemoteConfigServiceSpecification());
        return context;
    }
}
