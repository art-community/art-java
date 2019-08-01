package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.ModuleEndpoint;

public interface ModuleEndpointMapper {
    ValueToModelMapper<ModuleEndpoint, Entity> toModuleEndpoint = entity -> ModuleEndpoint.builder()
            .host(entity.getString("host"))
            .port(entity.getInt("port"))
            .sessions(entity.getInt("sessions"))
            .build();

    ValueFromModelMapper<ModuleEndpoint, Entity> fromModuleEndpoint = model -> Entity.entityBuilder()
            .stringField("host", model.getHost())
            .intField("port", model.getPort())
            .intField("sessions", model.getSessions())
            .build();
}
