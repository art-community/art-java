package ru.art.module.executor.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.core.module.ModuleConfiguration;
import ru.art.core.module.ModuleState;
import ru.art.module.executor.specification.ModuleExecutorServiceSpecification;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.art.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.art.module.executor.constants.ModuleExecutorConstants.MODULE_EXECUTOR_MODULE_ID;
import static ru.art.service.ServiceModule.serviceModule;

/**
 * Module class is a main class where all needed modules are loading
 * and services are registering
 */

@Getter
public class ModuleExecutorModule implements Module<ModuleConfiguration, ModuleState> {
    private final ModuleConfiguration defaultConfiguration = null;
    private final String id = MODULE_EXECUTOR_MODULE_ID;

    public static void startModuleExecutor() {
        useAgileConfigurations(MODULE_EXECUTOR_MODULE_ID);
        serviceModule()
                .getServiceRegistry()
                .registerService(new ModuleExecutorServiceSpecification());
        httpServerInSeparatedThread();
    }

    public static void main(String[] args) {
        startModuleExecutor();
    }
}