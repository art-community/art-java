package ru.adk.config.remote.initializer;

import ru.adk.config.Config;
import ru.adk.config.module.ConfigModule;
import ru.adk.config.remote.specification.RemoteConfigServiceSpecification;
import ru.adk.configurator.api.specification.ConfiguratorProxyServiceSpecification;
import ru.adk.core.context.Context;
import ru.adk.grpc.client.module.GrpcClientModule;
import ru.adk.logging.LoggingModule;
import ru.adk.service.ServiceModule;
import static ru.adk.config.ConfigProvider.config;
import static ru.adk.config.remote.constants.RemoteConfigLoaderConstants.CONFIGURATOR_CONNECTION_PROPERTIES_NOT_EXISTS;
import static ru.adk.config.remote.constants.RemoteConfigLoaderConstants.LocalConfigKeys.CONFIGURATOR_HOST;
import static ru.adk.config.remote.constants.RemoteConfigLoaderConstants.LocalConfigKeys.CONFIGURATOR_PORT;
import static ru.adk.core.constants.StringConstants.EMPTY_STRING;
import static ru.adk.logging.LoggingModule.loggingModule;
import static ru.adk.service.ServiceModule.serviceModule;

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
