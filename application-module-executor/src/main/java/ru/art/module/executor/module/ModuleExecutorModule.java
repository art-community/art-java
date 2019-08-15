/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
    private final String id = MODULE_EXECUTOR_MODULE_ID;
    private final ModuleConfiguration defaultConfiguration = null;

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