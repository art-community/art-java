package ru.art.scheduler.db.adapter.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_EXECUTION_PERIOD;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_ID;

public interface UpdateTaskExecutionTimeMapping {
    ValueToModelMapper<UpdateTaskExecutionTimeRequest, Entity> updateTaskExecutionTimeRequestToModelMapper = entity -> new UpdateTaskExecutionTimeRequest(entity.getString(TASK_ID), entity.getLong(TASK_EXECUTION_PERIOD));
    ValueFromModelMapper<UpdateTaskExecutionTimeRequest, Entity> updateTaskExecutionTimeRequestFromModelMapper = request -> entityBuilder().stringField(TASK_ID, request.getTaskId())
            .longField(TASK_EXECUTION_PERIOD, request.getExecutionPeriodSeconds())
            .build();
}
