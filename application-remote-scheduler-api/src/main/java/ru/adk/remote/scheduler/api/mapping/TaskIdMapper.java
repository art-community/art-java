package ru.adk.remote.scheduler.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.entity.mapper.ValueMapper.mapper;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Fields.ID;

public interface TaskIdMapper {
    EntityToModelMapper<String> taskIdToModelMapper = entity -> entity.getString(ID);
    EntityFromModelMapper<String> taskIdFromModelMapper = taskId -> entityBuilder().stringField(ID, taskId).build();

    ValueMapper<String, Entity> taskIdMapper = mapper(taskIdFromModelMapper, taskIdToModelMapper);
}
