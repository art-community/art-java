package ru.art.network.manager.selector;

import ru.art.state.api.model.ModuleEndpoint;
import static ru.art.network.manager.module.NetworkManagerModule.networkManagerModuleState;

public interface ModuleEndpointSelector {
    static ModuleEndpoint selectModuleEndpoint(String modulePath) {
        return networkManagerModuleState()
                .getClusterState()
                .getBalancer()
                .select(modulePath);
    }
}
