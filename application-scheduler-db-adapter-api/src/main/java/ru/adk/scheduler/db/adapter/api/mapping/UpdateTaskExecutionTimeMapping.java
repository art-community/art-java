package ru.adk.scheduler.db.adapter.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskExecutionTimeRequest;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_EXECUTION_PERIOD;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_ID;

public interface UpdateTaskExecutionTimeMapping {
    ValueToModelMapper<UpdateTaskExecutionTimeRequest, Entity> updateTaskExecutionTimeRequestToModelMapper = entity -> new UpdateTaskExecutionTimeRequest(entity.getString(TASK_ID), entity.getLong(TASK_EXECUTION_PERIOD));
    ValueFromModelMapper<UpdateTaskExecutionTimeRequest, Entity> updateTaskExecutionTimeRequestFromModelMapper = request -> entityBuilder().stringField(TASK_ID, request.getTaskId())
            .longField(TASK_EXECUTION_PERIOD, request.getExecutionPeriodSeconds())
            .build();
}
