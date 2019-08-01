package ru.adk.state.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.state.api.model.LockRequest;

public interface LockRequestMapper {
    ValueToModelMapper<LockRequest, Entity> toLockRequest = entity -> LockRequest.builder()
            .name(entity.getString("name"))
            .build();

    ValueFromModelMapper<LockRequest, Entity> fromLockRequest = model -> Entity.entityBuilder()
            .stringField("name", model.getName())
            .build();
}
