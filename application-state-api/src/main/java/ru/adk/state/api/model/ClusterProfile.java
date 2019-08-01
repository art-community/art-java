package ru.adk.state.api.model;

import lombok.*;
import static java.util.Objects.nonNull;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.state.api.model.ModuleNetwork.moduleNetworkOf;
import java.util.Map;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClusterProfile {
    @Builder.Default
    private final Map<String, ModuleNetwork> modules = mapOf();

    static ClusterProfile clusterProfileOf(String modulePath, ModuleNetwork network) {
        ClusterProfile profile = new ClusterProfile();
        profile.modules.put(modulePath, network);
        return profile;
    }

    public void putEndpoint(String modulePath, ModuleEndpoint endpoint) {
        ModuleNetwork moduleNetwork;
        if (nonNull(moduleNetwork = modules.get(modulePath))) {
            moduleNetwork.addEndpoint(endpoint);
            return;
        }
        modules.put(modulePath, moduleNetworkOf(endpoint));
    }

    public void removeEndpoint(String modulePath, ModuleEndpoint endpoint) {
        ModuleNetwork moduleNetwork;
        if (nonNull(moduleNetwork = modules.get(modulePath))) {
            moduleNetwork.removeEndpoint(endpoint);
        }
    }

    public ModuleNetwork getModuleNetwork(String modulePath) {
        return modules.get(modulePath);
    }
}
