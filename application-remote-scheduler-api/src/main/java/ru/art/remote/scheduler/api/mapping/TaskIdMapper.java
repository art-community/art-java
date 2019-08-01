package ru.art.remote.scheduler.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Fields.ID;

public interface TaskIdMapper {
    EntityToModelMapper<String> taskIdToModelMapper = entity -> entity.getString(ID);
    EntityFromModelMapper<String> taskIdFromModelMapper = taskId -> entityBuilder().stringField(ID, taskId).build();

    ValueMapper<String, Entity> taskIdMapper = mapper(taskIdFromModelMapper, taskIdToModelMapper);
}
