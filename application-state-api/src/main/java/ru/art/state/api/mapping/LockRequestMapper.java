package ru.art.state.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.state.api.model.LockRequest;

public interface LockRequestMapper {
    ValueToModelMapper<LockRequest, Entity> toLockRequest = entity -> LockRequest.builder()
            .name(entity.getString("name"))
            .build();

    ValueFromModelMapper<LockRequest, Entity> fromLockRequest = model -> Entity.entityBuilder()
            .stringField("name", model.getName())
            .build();
}
