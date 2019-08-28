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

package ru.art.state.api.model;

import lombok.*;
import static java.lang.System.*;
import static java.util.Comparator.*;
import static ru.art.core.factory.CollectionsFactory.*;
import java.util.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ModuleNetwork {
    @Builder.Default
    private final Queue<ModuleEndpoint> endpoints = priorityQueueOf(comparingInt(ModuleEndpoint::getSessions));
    @Builder.Default
    private final Map<ModuleEndpoint, Long> endpointMarks = mapOf();

    static ModuleNetwork moduleNetworkOf(ModuleEndpoint endpoint) {
        ModuleNetwork moduleNetwork = new ModuleNetwork();
        moduleNetwork.endpoints.add(endpoint);
        moduleNetwork.endpointMarks.put(endpoint, currentTimeMillis());
        return moduleNetwork;
    }

    public void incrementSessions(ModuleEndpoint moduleEndpoint) {
        Optional<ModuleEndpoint> foundEndpoint;
        if (!(foundEndpoint = endpoints.stream().filter(moduleEndpoint::equals).findFirst()).isPresent()) {
            endpoints.add(moduleEndpoint);
            endpointMarks.put(moduleEndpoint, currentTimeMillis());
            moduleEndpoint.setSessions(1);
            return;
        }
        ModuleEndpoint endpoint = foundEndpoint.get();
        endpoint.setSessions(endpoint.getSessions() + 1);
        endpointMarks.put(moduleEndpoint, currentTimeMillis());
    }

    public void decrementSessions(ModuleEndpoint moduleEndpoint) {
        Optional<ModuleEndpoint> foundEndpoint = endpoints.stream().filter(moduleEndpoint::equals).findFirst();
        if (!foundEndpoint.isPresent()) return;
        ModuleEndpoint endpoint = foundEndpoint.get();
        endpoint.setSessions(endpoint.getSessions() - 1);
        endpointMarks.put(moduleEndpoint, currentTimeMillis());
    }

    public int getSessions(ModuleEndpoint moduleEndpoint) {
        return endpoints.stream().filter(moduleEndpoint::equals)
                .findFirst()
                .map(ModuleEndpoint::getSessions)
                .orElse(0);
    }

    public void addEndpoint(ModuleEndpoint moduleEndpoint) {
        if (!endpointMarks.containsKey(moduleEndpoint)) {
            endpoints.add(moduleEndpoint);
        }
        endpointMarks.put(moduleEndpoint, currentTimeMillis());
    }

    public void removeEndpoint(ModuleEndpoint moduleEndpoint) {
        endpoints.remove(moduleEndpoint);
        endpointMarks.remove(moduleEndpoint);
    }
}
