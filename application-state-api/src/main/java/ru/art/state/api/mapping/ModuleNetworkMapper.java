package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ModuleEndpoint;
import ru.art.state.api.model.ModuleNetwork;
import static java.util.Comparator.comparingInt;
import static ru.art.core.factory.CollectionsFactory.priorityQueueOf;

public interface ModuleNetworkMapper {
    ValueToModelMapper<ModuleNetwork, Entity> toModuleNetwork = entity -> ModuleNetwork.builder()
            .endpoints(priorityQueueOf(comparingInt(ModuleEndpoint::getSessions), entity.getEntityList("endpoints", ModuleEndpointMapper.toModuleEndpoint)))
            .build();

    ValueFromModelMapper<ModuleNetwork, Entity> fromModuleNetwork = model -> Entity.entityBuilder()
            .entityCollectionField("endpoints", model.getEndpoints(), ModuleEndpointMapper.fromModuleEndpoint)
            .build();
}
