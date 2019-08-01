package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.ModuleEndpoint;

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
