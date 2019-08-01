package ru.adk.remote.scheduler.api.mapping;

import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import ru.adk.remote.scheduler.api.model.DeferredTask;
import ru.adk.remote.scheduler.api.model.DeferredTaskRequest;
import static java.util.stream.Collectors.toSet;
import static ru.adk.core.constants.DateConstants.DD_MM_YYYY_HH_MM_SS_24H_DOT;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Fields.*;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus.valueOf;
import static ru.adk.remote.scheduler.api.mapping.DeferredTaskMappers.DeferredTaskMapper.deferredTaskFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.DeferredTaskMappers.DeferredTaskMapper.deferredTaskToModelMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


public interface DeferredTaskMappers {

    interface DeferredTaskRequestMapper {
        EntityToModelMapper<DeferredTaskRequest> deferredTaskRequestToModelMapper = entity -> DeferredTaskRequest.builder()
                .executableServletPath(entity.getString(SERVLET_PATH))
                .executableServiceId(entity.getString(SERVICE_ID))
                .executableMethodId(entity.getString(METHOD_ID))
                .executableRequest(entity.getValue(REQUEST))
                .executionDateTime(LocalDateTime.parse(entity.getString(EXECUTION_DATE), DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .build();

        EntityFromModelMapper<DeferredTaskRequest> deferredTaskRequestFromModelMapper = request -> entityBuilder()
                .stringField(SERVLET_PATH, request.getExecutableServletPath())
                .stringField(SERVICE_ID, request.getExecutableServiceId())
                .stringField(METHOD_ID, request.getExecutableMethodId())
                .valueField(REQUEST, request.getExecutableRequest())
                .stringField(EXECUTION_DATE, request.getExecutionDateTime().format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .build();
    }

    interface DeferredTaskMapper {
        EntityToModelMapper<DeferredTask> deferredTaskToModelMapper = entity -> DeferredTask.builder()
                .id(entity.getString(ID))
                .status(valueOf(entity.getString(STATUS)))
                .creationDateTime(LocalDateTime.parse(entity.getString(CREATION_DATE), DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .executableServletPath(entity.getString(SERVLET_PATH))
                .executableServiceId(entity.getString(SERVICE_ID))
                .executableMethodId(entity.getString(METHOD_ID))
                .executableRequest(entity.getValue(REQUEST))
                .executionDateTime(LocalDateTime.parse(entity.getString(EXECUTION_DATE), DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .build();

        EntityFromModelMapper<DeferredTask> deferredTaskFromModelMapper = task -> entityBuilder()
                .stringField(ID, task.getId())
                .stringField(STATUS, task.getStatus().toString())
                .stringField(CREATION_DATE, task.getExecutionDateTime().format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .stringField(SERVLET_PATH, task.getExecutableServletPath())
                .stringField(SERVICE_ID, task.getExecutableServiceId())
                .stringField(METHOD_ID, task.getExecutableMethodId())
                .valueField(REQUEST, task.getExecutableRequest())
                .stringField(EXECUTION_DATE, task.getExecutionDateTime().format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .build();
    }

    interface DeferredTaskCollectionMapper {
        EntityToModelMapper<Set<DeferredTask>> deferredTaskCollectionToModelMapper = entity -> entity.getEntityList(TASKS)
                .stream()
                .map(deferredTaskToModelMapper::map)
                .collect(toSet());

        EntityFromModelMapper<Set<DeferredTask>> deferredTaskCollectionFromModelMapper = taskCollection -> entityBuilder()
                .entityCollectionField(TASKS, taskCollection.stream().map(deferredTaskFromModelMapper::map).collect(toSet()))
                .build();
    }
}
