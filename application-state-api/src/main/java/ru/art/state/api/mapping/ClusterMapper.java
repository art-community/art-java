package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.Cluster;
import static ru.art.core.factory.CollectionsFactory.concurrentHashMap;

public interface ClusterMapper {
    ValueToModelMapper<Cluster, Entity> toCluster = entity -> Cluster.builder()
            .profiles(concurrentHashMap(entity.getMap("profiles", PrimitiveMapping.StringPrimitive.toModel, ClusterProfileMapper.toClusterProfile)))
            .build();

    ValueFromModelMapper<Cluster, Entity> fromCluster = model -> Entity.entityBuilder()
            .mapField("profiles", model.getProfiles(), PrimitiveMapping.StringPrimitive.fromModel, ClusterProfileMapper.fromClusterProfile)
            .build();
}
