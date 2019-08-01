package ru.adk.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import static java.util.Objects.nonNull;
import static ru.adk.core.factory.CollectionsFactory.concurrentHashMap;
import static ru.adk.state.api.model.ClusterProfile.clusterProfileOf;
import static ru.adk.state.api.model.ModuleNetwork.moduleNetworkOf;
import java.util.Map;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Cluster {
    @Builder.Default
    private final Map<String, ClusterProfile> profiles = concurrentHashMap();

    public void putEndpoint(String profileId, String modulePath, ModuleEndpoint endpoint) {
        ClusterProfile clusterProfile;
        if (nonNull(clusterProfile = profiles.get(profileId))) {
            clusterProfile.putEndpoint(modulePath, endpoint);
            return;
        }
        profiles.put(profileId, clusterProfileOf(modulePath, moduleNetworkOf(endpoint)));
    }

    public void removeEndpoint(String profileId, String modulePath, ModuleEndpoint endpoint) {
        ClusterProfile clusterProfile;
        if (nonNull(clusterProfile = profiles.get(profileId))) {
            clusterProfile.removeEndpoint(modulePath, endpoint);
        }
    }
}
