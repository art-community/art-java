package ru.adk.state.api.model;

import lombok.*;
import static java.lang.System.currentTimeMillis;
import static java.util.Comparator.comparingInt;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import static ru.adk.core.factory.CollectionsFactory.priorityQueueOf;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

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
