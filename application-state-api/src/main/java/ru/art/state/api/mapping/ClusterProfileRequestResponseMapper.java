package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.PrimitiveMapping;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ClusterProfileRequest;
import ru.art.state.api.model.ClusterProfileResponse;
import static ru.art.core.factory.CollectionsFactory.mapOf;

public interface ClusterProfileRequestResponseMapper {
    interface ClusterProfileRequestMapper {
        ValueToModelMapper<ClusterProfileRequest, Entity> toClusterProfileRequest = entity -> ClusterProfileRequest.builder()
                .profile(entity.getString("profile"))
                .build();

        ValueFromModelMapper<ClusterProfileRequest, Entity> fromClusterProfileRequest = model -> Entity.entityBuilder()
                .stringField("profile", model.getProfile())
                .build();
    }

    interface ClusterProfileResponseMapper {
        ValueToModelMapper<ClusterProfileResponse, Entity> toClusterProfileResponse = entity -> ClusterProfileResponse.builder()
                .moduleEndpointStates(mapOf(entity.getMap("moduleEndpointStates", PrimitiveMapping.StringPrimitive.toModel, ModuleNetworkResponseMapper.toModuleNetworkResponse)))
                .build();

        ValueFromModelMapper<ClusterProfileResponse, Entity> fromClusterProfileResponse = model -> Entity.entityBuilder()
                .mapField("moduleEndpointStates", model.getModuleEndpointStates(), PrimitiveMapping.StringPrimitive.fromModel, ModuleNetworkResponseMapper.fromModuleNetworkResponse)
                .build();
    }
}
