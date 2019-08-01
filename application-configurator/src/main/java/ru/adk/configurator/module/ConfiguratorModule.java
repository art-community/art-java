package ru.adk.configurator.module;

import io.advantageous.config.Config;
import lombok.Getter;
import ru.adk.config.module.ConfigModule;
import ru.adk.configurator.configuration.*;
import ru.adk.configurator.specification.ConfiguratorServiceSpecification;
import ru.adk.configurator.specification.UserServiceSpecification;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleState;
import ru.adk.grpc.client.module.GrpcClientModule;
import ru.adk.grpc.server.module.GrpcServerModule;
import ru.adk.http.client.module.HttpClientModule;
import ru.adk.http.server.module.HttpServerModule;
import ru.adk.http.server.specification.HttpWebUiServiceSpecification;
import ru.adk.json.module.JsonModule;
import ru.adk.logging.LoggingModule;
import ru.adk.metrics.http.specification.MetricServiceSpecification;
import ru.adk.metrics.module.MetricsModule;
import ru.adk.rocks.db.module.RocksDbModule;
import ru.adk.service.ServiceModule;
import static lombok.AccessLevel.PRIVATE;
import static ru.adk.config.ConfigProvider.config;
import static ru.adk.config.constants.ConfigType.YAML;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.CONFIGURATOR_MODULE_ID;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.ConfiguratorLocalConfigKeys.*;
import static ru.adk.configurator.constants.ConfiguratorModuleConstants.HTTP_SERVER_BOOTSTRAP_THREAD;
import static ru.adk.configurator.service.UserService.register;
import static ru.adk.core.configuration.ContextInitialConfiguration.ApplicationContextConfiguration;
import static ru.adk.core.context.Context.context;
import static ru.adk.core.context.Context.initContext;
import static ru.adk.core.extension.ThreadExtensions.thread;
import static ru.adk.grpc.server.GrpcServer.grpcServer;
import static ru.adk.http.server.HttpServer.httpServer;
import static ru.adk.service.ServiceModule.serviceModule;

@Getter
public class ConfiguratorModule implements Module<ConfiguratorModuleConfiguration, ModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final ConfiguratorModuleConfiguration configuratorModule = context().getModule(CONFIGURATOR_MODULE_ID, new ConfiguratorModule());

    private final ConfiguratorModuleConfiguration defaultConfiguration = new ConfiguratorModuleConfiguration();
    private final String id = CONFIGURATOR_MODULE_ID;

    public static void startConfigurator() {
        initContext(new ApplicationContextConfiguration(CONFIGURATOR_MODULE_ID))
                .loadModule(new ConfigModule())
                .loadModule(new JsonModule())
                .loadModule(new LoggingModule())
                .loadModule(new ServiceModule())
                .loadModule(new RocksDbModule(), new ConfiguratorRocksDbConfiguration())
                .loadModule(new HttpServerModule(), new ConfiguratorHttpServerConfiguration())
                .loadModule(new GrpcServerModule(), new ConfiguratorProtobufServerConfiguration())
                .loadModule(new MetricsModule(), new ConfiguratorMetricsConfiguration())
                .loadModule(new ConfiguratorModule())
                .loadModule(new GrpcClientModule())
                .loadModule(new HttpClientModule(), new ConfiguratorHttpClientConfiguration());
        serviceModule()
                .getServiceRegistry()
                .registerService(new ConfiguratorServiceSpecification())
                .registerService(new HttpWebUiServiceSpecification())
                .registerService(new UserServiceSpecification())
                .registerService(new MetricServiceSpecification());
        thread(HTTP_SERVER_BOOTSTRAP_THREAD, () -> httpServer().await());
        grpcServer().await();
    }

    public static ConfiguratorModuleConfiguration configuratorModule() {
        return getConfiguratorModule();
    }

    public static void main(String[] args) {
        startConfigurator();
    }

    @Override
    public void onLoad() {
        Config config;
        register((config = config(CONFIGURATOR_SECTION_ID, YAML).asYamlConfig()).getString(CONFIGURATOR_USER), config.getString(CONFIGURATOR_PASSWORD));
    }
}
