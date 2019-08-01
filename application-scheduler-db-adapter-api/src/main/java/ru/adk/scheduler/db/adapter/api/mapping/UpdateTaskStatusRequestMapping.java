package ru.adk.scheduler.db.adapter.api.mapping;

import ru.adk.entity.Entity;
import ru.adk.entity.mapper.ValueFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper;
import ru.adk.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import static ru.adk.core.extension.StringExtensions.emptyIfNull;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_ID;
import static ru.adk.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_STATUS;

public interface UpdateTaskStatusRequestMapping {
    ValueToModelMapper<UpdateTaskStatusRequest, Entity> updateTaskStatusRequestToModelMapper = entity -> new UpdateTaskStatusRequest(entity.getString(TASK_ID), TaskStatus.valueOf(entity.getString(TASK_STATUS)));
    ValueFromModelMapper<UpdateTaskStatusRequest, Entity> updateTaskStatusRequestFromModelMapper = request -> entityBuilder().stringField(TASK_ID, request.getTaskId())
            .stringField(TASK_STATUS, emptyIfNull(request.getStatus()))
            .build();
}
