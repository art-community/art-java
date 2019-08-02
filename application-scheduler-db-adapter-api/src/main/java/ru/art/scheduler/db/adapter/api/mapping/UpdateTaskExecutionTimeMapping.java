/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

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
