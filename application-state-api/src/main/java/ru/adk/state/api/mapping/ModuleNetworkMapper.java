package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.ModuleEndpoint;
import ru.adk.state.api.model.ModuleNetwork;
import static java.util.Comparator.comparingInt;
import static ru.adk.core.factory.CollectionsFactory.priorityQueueOf;

public interface ModuleNetworkMapper {
    ValueToModelMapper<ModuleNetwork, Entity> toModuleNetwork = entity -> ModuleNetwork.builder()
            .endpoints(priorityQueueOf(comparingInt(ModuleEndpoint::getSessions), entity.getEntityList("endpoints", ModuleEndpointMapper.toModuleEndpoint)))
            .build();

    ValueFromModelMapper<ModuleNetwork, Entity> fromModuleNetwork = model -> Entity.entityBuilder()
            .entityCollectionField("endpoints", model.getEndpoints(), ModuleEndpointMapper.fromModuleEndpoint)
            .build();
}
