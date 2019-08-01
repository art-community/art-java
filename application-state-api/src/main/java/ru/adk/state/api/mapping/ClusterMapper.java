package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.PrimitiveMapping;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.Cluster;
import static ru.adk.core.factory.CollectionsFactory.concurrentHashMap;

public interface ClusterMapper {
    ValueToModelMapper<Cluster, Entity> toCluster = entity -> Cluster.builder()
            .profiles(concurrentHashMap(entity.getMap("profiles", PrimitiveMapping.StringPrimitive.toModel, ClusterProfileMapper.toClusterProfile)))
            .build();

    ValueFromModelMapper<Cluster, Entity> fromCluster = model -> Entity.entityBuilder()
            .mapField("profiles", model.getProfiles(), PrimitiveMapping.StringPrimitive.fromModel, ClusterProfileMapper.fromClusterProfile)
            .build();
}
