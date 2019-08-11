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

package ru.art.network.manager.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.network.manager.client.ApplicationStateClient;
import ru.art.network.manager.configuration.NetworkManagerModuleConfiguration;
import ru.art.network.manager.refresher.ModuleEndpointsRefresher;
import ru.art.network.manager.state.NetworkManagerModuleState;
import ru.art.state.api.communication.grpc.LockServiceProxySpecification;
import ru.art.state.api.communication.grpc.NetworkServiceProxySpecification;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static ru.art.core.context.Context.context;
import static ru.art.network.manager.configuration.NetworkManagerModuleConfiguration.NetworkManagerModuleDefaultConfiguration;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.NETWORK_MANAGER_MODULE_ID;
import static ru.art.service.ServiceModule.serviceModule;
import static ru.art.task.deferred.executor.IdentifiedRunnableFactory.commonTask;
import static ru.art.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;

@Getter
public class NetworkManagerModule implements Module<NetworkManagerModuleConfiguration, NetworkManagerModuleState> {
    private final NetworkManagerModuleConfiguration defaultConfiguration = new NetworkManagerModuleDefaultConfiguration();
    private final NetworkManagerModuleState state = new NetworkManagerModuleState();
    private final String id = NETWORK_MANAGER_MODULE_ID;

    public static NetworkManagerModuleConfiguration networkManagerModule() {
        return context().getModule(NETWORK_MANAGER_MODULE_ID, new NetworkManagerModule());
    }

    public static NetworkManagerModuleState networkManagerModuleState() {
        return context().getModuleState(NETWORK_MANAGER_MODULE_ID, new NetworkManagerModule());
    }

    @Override
    public void onLoad() {
        asynchronousPeriod(commonTask(ModuleEndpointsRefresher::refreshModuleEndpoints), of(networkManagerModule().getRefreshRateSeconds(), SECONDS));
        asynchronousPeriod(commonTask(ApplicationStateClient::connect), of(networkManagerModule().getConnectionPingRateSeconds(), SECONDS));
        int statePort = networkManagerModule().getStatePort();
        String stateHost = networkManagerModule().getStateHost();
        String statePath = networkManagerModule().getStatePath();
        serviceModule().getServiceRegistry()
                .registerService(new NetworkServiceProxySpecification(statePath, stateHost, statePort))
                .registerService(new LockServiceProxySpecification(statePath, stateHost, statePort));
    }
}
