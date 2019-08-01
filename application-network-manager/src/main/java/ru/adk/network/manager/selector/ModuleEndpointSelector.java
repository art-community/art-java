package ru.adk.network.manager.selector;

import ru.adk.state.api.model.ModuleEndpoint;
import static ru.adk.network.manager.module.NetworkManagerModule.networkManagerModuleState;

public interface ModuleEndpointSelector {
    static ModuleEndpoint selectModuleEndpoint(String modulePath) {
        return networkManagerModuleState()
                .getClusterState()
                .getBalancer()
                .select(modulePath);
    }
}
