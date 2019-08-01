package ru.art.state.api.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import static ru.art.core.factory.CollectionsFactory.mapOf;
import java.util.Map;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ClusterProfileResponse {
    @Builder.Default
    private final Map<String, ModuleNetworkResponse> moduleEndpointStates = mapOf();
}