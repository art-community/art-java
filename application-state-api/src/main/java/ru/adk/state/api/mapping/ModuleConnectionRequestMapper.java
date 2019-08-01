package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.ModuleConnectionRequest;

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
