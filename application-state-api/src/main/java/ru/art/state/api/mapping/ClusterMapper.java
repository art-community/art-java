/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
