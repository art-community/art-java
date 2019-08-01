package ru.art.scheduler.db.adapter.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper;
import ru.art.entity.mapper.ValueToModelMapper;
import ru.art.scheduler.db.adapter.api.model.UpdateTaskStatusRequest;
import static ru.art.core.extension.StringExtensions.emptyIfNull;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_ID;
import static ru.art.scheduler.db.adapter.api.constants.SchedulerDbAdapterApiConstants.Fields.TASK_STATUS;

public interface UpdateTaskStatusRequestMapping {
    ValueToModelMapper<UpdateTaskStatusRequest, Entity> updateTaskStatusRequestToModelMapper = entity -> new UpdateTaskStatusRequest(entity.getString(TASK_ID), TaskStatus.valueOf(entity.getString(TASK_STATUS)));
    ValueFromModelMapper<UpdateTaskStatusRequest, Entity> updateTaskStatusRequestFromModelMapper = request -> entityBuilder().stringField(TASK_ID, request.getTaskId())
            .stringField(TASK_STATUS, emptyIfNull(request.getStatus()))
            .build();
}
