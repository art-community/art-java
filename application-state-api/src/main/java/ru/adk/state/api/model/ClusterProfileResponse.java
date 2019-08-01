package ru.adk.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import static ru.adk.core.factory.CollectionsFactory.mapOf;
import java.util.Map;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ClusterProfileResponse {
    @Builder.Default
    private final Map<String, ModuleNetworkResponse> moduleEndpointStates = mapOf();
}