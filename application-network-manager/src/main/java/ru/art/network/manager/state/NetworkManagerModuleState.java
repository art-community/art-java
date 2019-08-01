package ru.art.network.manager.state;

import lombok.Getter;
import ru.art.core.module.ModuleState;
import static ru.art.network.manager.balancer.Balancer.balancerOf;
import static ru.art.network.manager.module.NetworkManagerModule.networkManagerModule;

@Getter
public class NetworkManagerModuleState implements ModuleState {
    @Getter(lazy = true)
    private final ClusterState clusterState = new ClusterState(balancerOf(networkManagerModule().getBalancerMode()));
}
