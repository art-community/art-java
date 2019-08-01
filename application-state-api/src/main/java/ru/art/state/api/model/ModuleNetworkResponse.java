package ru.art.state.api.model;

import lombok.*;
import static java.util.Comparator.comparingInt;
import static ru.art.core.factory.CollectionsFactory.priorityQueueOf;
import java.util.Queue;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ModuleNetworkResponse {
    @Builder.Default
    private final Queue<ModuleEndpoint> endpoints = priorityQueueOf(comparingInt(ModuleEndpoint::getSessions));

    public static ModuleNetworkResponse fromNetwork(ModuleNetwork network) {
        return ModuleNetworkResponse.builder().endpoints(network.getEndpoints()).build();
    }
}