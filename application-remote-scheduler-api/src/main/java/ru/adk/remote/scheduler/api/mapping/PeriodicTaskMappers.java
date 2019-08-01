package ru.adk.remote.scheduler.api.mapping;

import ru.adk.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import ru.adk.remote.scheduler.api.model.PeriodicTask;
import ru.adk.remote.scheduler.api.model.PeriodicTaskRequest;
import static java.util.stream.Collectors.toSet;
import static ru.adk.core.constants.DateConstants.DD_MM_YYYY_HH_MM_SS_24H_DOT;
import static ru.adk.entity.Entity.entityBuilder;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Fields.*;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.TaskStatus.valueOf;
import static ru.adk.remote.scheduler.api.mapping.PeriodicTaskMappers.PeriodicTaskMapper.periodicTaskFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.PeriodicTaskMappers.PeriodicTaskMapper.periodicTaskToModelMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;


public interface PeriodicTaskMappers {

    interface PeriodicTaskRequestMapper {
        EntityToModelMapper<PeriodicTaskRequest> periodicTaskRequestToModelMapper = entity -> PeriodicTaskRequest.builder()
                .executableServletPath(entity.getString(SERVLET_PATH))
                .executableServiceId(entity.getString(SERVICE_ID))
                .executableMethodId(entity.getString(METHOD_ID))
                .executableRequest(entity.getValue(REQUEST))
                .executionDateTime(LocalDateTime.parse(entity.getString(EXECUTION_DATE), DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .maxExecutionCount(entity.getInt(MAX_EXECUTION_COUNT))
                .executionPeriodSeconds(entity.getLong(INTERVAL_IN_SECONDS))
                .finishAfterCompletion(entity.getBool(FINISH_AFTER_COMPLETION))
                .build();


        EntityFromModelMapper<PeriodicTaskRequest> periodicTaskRequestFromModelMapper = request -> entityBuilder()
                .stringField(SERVLET_PATH, request.getExecutableServletPath())
                .stringField(SERVICE_ID, request.getExecutableServiceId())
                .stringField(METHOD_ID, request.getExecutableMethodId())
                .valueField(REQUEST, request.getExecutableRequest())
                .stringField(EXECUTION_DATE, request.getExecutionDateTime().format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .intField(MAX_EXECUTION_COUNT, request.getMaxExecutionCount())
                .longField(INTERVAL_IN_SECONDS, request.getExecutionPeriodSeconds())
                .boolField(FINISH_AFTER_COMPLETION, request.isFinishAfterCompletion())
                .build();
    }

    interface PeriodicTaskMapper {
        EntityToModelMapper<PeriodicTask> periodicTaskToModelMapper = entity -> PeriodicTask.builder()
                .id(entity.getString(ID))
                .status(valueOf(entity.getString(STATUS)))
                .creationDateTime(LocalDateTime.parse(entity.getString(CREATION_DATE), DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .executionCount(entity.getInt(EXECUTION_COUNT))
                .executableServletPath(entity.getString(SERVLET_PATH))
                .executableServiceId(entity.getString(SERVICE_ID))
                .executableMethodId(entity.getString(METHOD_ID))
                .executableRequest(entity.getValue(REQUEST))
                .executionDateTime(LocalDateTime.parse(entity.getString(EXECUTION_DATE), DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .maxExecutionCount(entity.getInt(MAX_EXECUTION_COUNT))
                .executionPeriodSeconds(entity.getLong(INTERVAL_IN_SECONDS))
                .finishAfterCompletion(entity.getBool(FINISH_AFTER_COMPLETION))
                .build();

        EntityFromModelMapper<PeriodicTask> periodicTaskFromModelMapper = task -> entityBuilder()
                .stringField(ID, task.getId())
                .stringField(STATUS, task.getStatus().toString())
                .stringField(CREATION_DATE, task.getExecutionDateTime().format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .intField(EXECUTION_COUNT, task.getExecutionCount())
                .stringField(SERVLET_PATH, task.getExecutableServletPath())
                .stringField(SERVICE_ID, task.getExecutableServiceId())
                .stringField(METHOD_ID, task.getExecutableMethodId())
                .valueField(REQUEST, task.getExecutableRequest())
                .stringField(EXECUTION_DATE, task.getExecutionDateTime().format(DateTimeFormatter.ofPattern(DD_MM_YYYY_HH_MM_SS_24H_DOT)))
                .intField(MAX_EXECUTION_COUNT, task.getMaxExecutionCount())
                .longField(INTERVAL_IN_SECONDS, task.getExecutionPeriodSeconds())
                .boolField(FINISH_AFTER_COMPLETION, task.isFinishAfterCompletion())
                .build();
    }

    interface PeriodicTaskCollectionMapper {
        EntityToModelMapper<Set<PeriodicTask>> periodicTaskCollectionToModelMapper = entity -> entity.getEntityList(TASKS)
                .stream()
                .map(periodicTaskToModelMapper::map)
                .collect(toSet());

        EntityFromModelMapper<Set<PeriodicTask>> periodicTaskCollectionFromModelMapper = processCollection -> entityBuilder()
                .entityCollectionField(TASKS, processCollection.stream().map(periodicTaskFromModelMapper::map).collect(toSet()))
                .build();
    }
}
