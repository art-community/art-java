package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ClusterProfile;
import static ru.art.core.factory.CollectionsFactory.mapOf;

public interface ClusterProfileMapper {
    ValueToModelMapper<ClusterProfile, Entity> toClusterProfile = entity -> ClusterProfile.builder()
            .modules(mapOf(entity.getMap("modules", PrimitiveMapping.StringPrimitive.toModel, ModuleNetworkMapper.toModuleNetwork)))
            .build();

    ValueFromModelMapper<ClusterProfile, Entity> fromClusterProfile = model -> Entity.entityBuilder()
            .mapField("modules", model.getModules(), PrimitiveMapping.StringPrimitive.fromModel, ModuleNetworkMapper.fromModuleNetwork)
            .build();
}
