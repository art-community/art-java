/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.state.service;

import ru.art.state.api.model.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

import static java.lang.System.*;
import static java.time.Duration.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.state.api.model.ModuleNetworkResponse.*;
import static ru.art.state.dao.ClusterDao.*;
import static ru.art.state.module.ApplicationStateModule.*;

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