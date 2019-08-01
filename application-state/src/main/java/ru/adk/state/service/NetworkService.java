package ru.adk.state.service;

import ru.adk.state.api.model.*;
import static java.lang.System.currentTimeMillis;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;
import static ru.adk.core.factory.CollectionsFactory.setOf;
import static ru.adk.state.api.model.ModuleNetworkResponse.fromNetwork;
import static ru.adk.state.dao.ClusterDao.saveCluster;
import static ru.adk.state.module.ApplicationStateModule.applicationState;
import static ru.adk.state.module.ApplicationStateModule.applicationStateModule;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface NetworkService {
    static ClusterProfileResponse getClusterProfile(ClusterProfileRequest request) {
        removeDeadEndpoints();
        ClusterProfile clusterProfile;
        if (isNull(clusterProfile = applicationState().getClusterProfile(request.getProfile()))) {
            return ClusterProfileResponse.builder().build();
        }
        return ClusterProfileResponse.builder().moduleEndpointStates(clusterProfile
                .getModules()
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, entry -> fromNetwork(entry.getValue()))))
                .build();
    }

    static void connect(ModuleConnectionRequest request) {
        applicationState()
                .getCluster()
                .putEndpoint(request.getProfile(), request.getModulePath(), request.getModuleEndpoint());
        saveCluster(applicationState().getCluster());
    }

    static void incrementSession(ModuleConnectionRequest request) {
        Optional<ModuleNetwork> moduleNetwork;
        if ((moduleNetwork = applicationState().getModuleNetwork(request.getProfile(), request.getModulePath())).isPresent()) {
            moduleNetwork.ifPresent(network -> network.incrementSessions(request.getModuleEndpoint()));
            saveCluster(applicationState().getCluster());
        }
    }

    static void decrementSession(ModuleConnectionRequest request) {
        Optional<ModuleNetwork> moduleNetwork;
        if ((moduleNetwork = applicationState().getModuleNetwork(request.getProfile(), request.getModulePath())).isPresent()) {
            moduleNetwork.ifPresent(network -> network.decrementSessions(request.getModuleEndpoint()));
            saveCluster(applicationState().getCluster());
        }
    }

    static int getSessions(ModuleConnectionRequest request) {
        return applicationState()
                .getModuleNetwork(request.getProfile(), request.getModulePath())
                .map(network -> network.getSessions(request.getModuleEndpoint()))
                .orElse(0);
    }

    static Set<String> getProfiles() {
        return setOf(applicationState().getCluster().getProfiles().keySet());
    }

    static void removeDeadEndpoints() {
        Duration lifeTime = ofMinutes(applicationStateModule().getModuleEndpointLifeTimeMinutes());
        Collection<ClusterProfile> profiles = applicationState()
                .getCluster()
                .getProfiles()
                .values();
        for (ClusterProfile profile : profiles) {
            for (ModuleNetwork network : profile.getModules().values()) {
                Map<ModuleEndpoint, Long> endpointMarks = network.getEndpointMarks();
                Predicate<ModuleEndpoint> endpointPredicate = endpoint -> !endpointMarks.containsKey(endpoint);
                endpointPredicate.or(endpoint -> ofMillis(currentTimeMillis() - endpointMarks.get(endpoint)).compareTo(lifeTime) >= 1);
                network.getEndpoints().removeIf(endpointPredicate);
            }
        }
        saveCluster(applicationState().getCluster());
    }
}