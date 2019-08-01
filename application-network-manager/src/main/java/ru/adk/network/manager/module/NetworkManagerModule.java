package ru.adk.network.manager.module;

import lombok.Getter;
import ru.adk.core.module.Module;
import ru.adk.network.manager.client.ApplicationStateClient;
import ru.adk.network.manager.configuration.NetworkManagerModuleConfiguration;
import ru.adk.network.manager.refresher.ModuleEndpointsRefresher;
import ru.adk.network.manager.state.NetworkManagerModuleState;
import ru.adk.state.api.communication.grpc.LockServiceProxySpecification;
import ru.adk.state.api.communication.grpc.NetworkServiceProxySpecification;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static ru.adk.core.context.Context.context;
import static ru.adk.network.manager.configuration.NetworkManagerModuleConfiguration.NetworkManagerModuleDefaultConfiguration;
import static ru.adk.network.manager.constants.NetworkManagerModuleConstants.NETWORK_MANAGER_MODULE_ID;
import static ru.adk.service.ServiceModule.serviceModule;
import static ru.adk.task.deferred.executor.IdentifiedRunnableFactory.commonTask;
import static ru.adk.task.deferred.executor.SchedulerModuleActions.asynchronousPeriod;

@Getter
public class NetworkManagerModule implements Module<NetworkManagerModuleConfiguration, NetworkManagerModuleState> {
    @Getter(lazy = true)
    private static final NetworkManagerModuleConfiguration networkManagerModule = context().getModule(NETWORK_MANAGER_MODULE_ID, new NetworkManagerModule());
    @Getter(lazy = true)
    private static final NetworkManagerModuleState networkManagerModuleState = context().getModuleState(NETWORK_MANAGER_MODULE_ID, new NetworkManagerModule());
    private final NetworkManagerModuleConfiguration defaultConfiguration = new NetworkManagerModuleDefaultConfiguration();
    private final NetworkManagerModuleState state = new NetworkManagerModuleState();
    private final String id = NETWORK_MANAGER_MODULE_ID;

    public static NetworkManagerModuleConfiguration networkManagerModule() {
        return getNetworkManagerModule();
    }

    public static NetworkManagerModuleState networkManagerModuleState() {
        return getNetworkManagerModuleState();
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
