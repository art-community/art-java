/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.state.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.http.server.specification.HttpWebUiServiceSpecification;
import ru.art.metrics.http.specification.MetricServiceSpecification;
import ru.art.state.ApplicationState;
import ru.art.state.configuration.ApplicationStateModuleConfiguration;
import ru.art.state.configuration.ApplicationStateModuleConfiguration.ApplicationStateModuleDefaultConfiguration;
import ru.art.state.service.NetworkService;
import ru.art.state.specification.LockServiceSpecification;
import ru.art.state.specification.NetworkServiceSpecification;
import static java.time.Duration.ofSeconds;
import static ru.art.config.extensions.activator.AgileConfigurationsActivator.useAgileConfigurations;
import static ru.art.core.context.Context.context;
import static ru.art.core.context.Context.insideDefaultContext;
import static ru.art.grpc.server.GrpcServer.grpcServer;
import static ru.art.http.server.HttpServer.httpServerInSeparatedThread;
import static ru.art.http.server.constants.HttpServerModuleConstants.HttpWebUiServiceConstants.Methods.IMAGE;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.state.configuration.ApplicationStateModuleConfiguration.DEFAULT_CONFIGURATION;
import static ru.art.state.constants.StateModuleConstants.APPLICATION_STATE_MODULE_ID;
import static ru.art.state.dao.ClusterDao.loadCluster;
import static ru.art.task.deferred.executor.IdentifiedRunnableFactory.commonTask;
import static ru.art.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;

@Getter
public class ApplicationStateModule implements Module<ApplicationStateModuleConfiguration, ApplicationState> {
    @Getter(lazy = true)
    private final static ApplicationStateModuleConfiguration applicationStateModule = context()
            .getModule(APPLICATION_STATE_MODULE_ID, ApplicationStateModule::new);
    @Getter(lazy = true)
    private final static ApplicationState applicationState = context()
            .getModuleState(APPLICATION_STATE_MODULE_ID, ApplicationStateModule::new);
    private final String id = APPLICATION_STATE_MODULE_ID;
    private final ApplicationStateModuleConfiguration defaultConfiguration = new ApplicationStateModuleDefaultConfiguration();
    private final ApplicationState state = new ApplicationState();

    public static ApplicationStateModuleConfiguration applicationStateModule() {
        if (insideDefaultContext()) {
            return DEFAULT_CONFIGURATION;
        }
        return getApplicationStateModule();
    }

    public static ApplicationState applicationState() {
        return getApplicationState();
    }

    public static void main(String[] args) {
        startApplicationState();
    }

    @SuppressWarnings("WeakerAccess")
    public static void startApplicationState() {
        useAgileConfigurations(APPLICATION_STATE_MODULE_ID);
        String httpPath = httpServerModule().getPath();
        serviceModule().getServiceRegistry()
                .registerService(new NetworkServiceSpecification())
                .registerService(new LockServiceSpecification())
                .registerService(new HttpWebUiServiceSpecification(httpPath, httpPath + IMAGE))
                .registerService(new MetricServiceSpecification(httpPath));
        applicationState().setCluster(loadCluster());
        asynchronousPeriod(commonTask(NetworkService::removeDeadEndpoints), ofSeconds(applicationStateModule().getModuleEndpointCheckRateSeconds()));
        httpServerInSeparatedThread();
        grpcServer().await();
    }
}
