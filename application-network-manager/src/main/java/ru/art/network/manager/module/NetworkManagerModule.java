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
