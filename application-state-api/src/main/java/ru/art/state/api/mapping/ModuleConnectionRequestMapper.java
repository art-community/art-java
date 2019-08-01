package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ModuleConnectionRequest;

public interface ModuleConnectionRequestMapper {
    ValueToModelMapper<ModuleConnectionRequest, Entity> toModuleConnectionRequest = entity -> ModuleConnectionRequest.builder()
            .profile(entity.getString("profile"))
            .modulePath(entity.getString("modulePath"))
            .moduleEndpoint(entity.getValue("moduleEndpoint", ModuleEndpointMapper.toModuleEndpoint))
            .build();

    ValueFromModelMapper<ModuleConnectionRequest, Entity> fromModuleConnectionRequest = model -> Entity.entityBuilder()
            .stringField("profile", model.getProfile())
            .stringField("modulePath", model.getModulePath())
            .entityField("moduleEndpoint", model.getModuleEndpoint(), ModuleEndpointMapper.fromModuleEndpoint)
            .build();
}
