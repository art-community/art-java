package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.ModuleEndpoint;
import ru.adk.state.api.model.ModuleNetworkResponse;
import static java.util.Comparator.comparingInt;
import static ru.adk.core.factory.CollectionsFactory.priorityQueueOf;

public interface ModuleNetworkResponseMapper {
    ValueToModelMapper<ModuleNetworkResponse, Entity> toModuleNetworkResponse = entity -> ModuleNetworkResponse.builder()
            .endpoints(priorityQueueOf(comparingInt(ModuleEndpoint::getSessions), entity.getEntityList("endpoints", ModuleEndpointMapper.toModuleEndpoint)))
            .build();

    ValueFromModelMapper<ModuleNetworkResponse, Entity> fromModuleNetworkResponse = model -> Entity.entityBuilder()
            .entityCollectionField("endpoints", model.getEndpoints(), ModuleEndpointMapper.fromModuleEndpoint)
            .build();
}
