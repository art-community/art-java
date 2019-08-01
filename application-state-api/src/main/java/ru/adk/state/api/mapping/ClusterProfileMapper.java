package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.PrimitiveMapping;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.ClusterProfile;
import static ru.adk.core.factory.CollectionsFactory.mapOf;

public interface ClusterProfileMapper {
    ValueToModelMapper<ClusterProfile, Entity> toClusterProfile = entity -> ClusterProfile.builder()
            .modules(mapOf(entity.getMap("modules", PrimitiveMapping.StringPrimitive.toModel, ModuleNetworkMapper.toModuleNetwork)))
            .build();

    ValueFromModelMapper<ClusterProfile, Entity> fromClusterProfile = model -> Entity.entityBuilder()
            .mapField("modules", model.getModules(), PrimitiveMapping.StringPrimitive.fromModel, ModuleNetworkMapper.fromModuleNetwork)
            .build();
}
