package ru.adk.network.manager.state;

import lombok.Getter;
import ru.adk.core.module.ModuleState;
import static ru.adk.network.manager.balancer.Balancer.balancerOf;
import static ru.adk.network.manager.module.NetworkManagerModule.networkManagerModule;

@Getter
public class NetworkManagerModuleState implements ModuleState {
    @Getter(lazy = true)
    private final ClusterState clusterState = new ClusterState(balancerOf(networkManagerModule().getBalancerMode()));
}
