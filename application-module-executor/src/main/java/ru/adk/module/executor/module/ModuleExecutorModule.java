package ru.adk.module.executor.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.core.module.ModuleConfiguration;
import ru.adk.core.module.ModuleState;
import ru.adk.module.executor.specification.ModuleExecutorServiceSpecification;
import static ru.adk.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.adk.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.adk.module.executor.constants.ModuleExecutorConstants.MODULE_EXECUTOR_MODULE_ID;
import static ru.adk.service.ServiceModule.serviceModule;

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