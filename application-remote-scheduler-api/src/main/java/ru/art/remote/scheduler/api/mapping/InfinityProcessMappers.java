package ru.art.remote.scheduler.api.mapping;

import ru.art.entity.Entity;
import ru.art.entity.mapper.ValueFromModelMapper.EntityFromModelMapper;
import ru.art.entity.mapper.ValueMapper;
import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import ru.art.remote.scheduler.api.model.InfinityProcess;
import ru.art.remote.scheduler.api.model.InfinityProcessRequest;
import static java.util.stream.Collectors.toSet;
import static ru.art.entity.Entity.entityBuilder;
import static ru.art.entity.mapper.ValueMapper.mapper;
import static ru.art.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Fields.*;
import static ru.art.remote.scheduler.api.mapping.InfinityProcessMappers.InfinityProcessMapper.infinityProcessFromModelMapper;
import static ru.art.remote.scheduler.api.mapping.InfinityProcessMappers.InfinityProcessMapper.infinityProcessToModelMapper;
import java.util.Set;


public interface InfinityProcessMappers {

    interface InfinityProcessRequestMapper {
        EntityToModelMapper<InfinityProcessRequest> infinityProcessRequestToModelMapper = entity -> InfinityProcessRequest.builder()
                .executableServletPath(entity.getString(SERVLET_PATH))
                .executableServiceId(entity.getString(SERVICE_ID))
                .executableMethodId(entity.getString(METHOD_ID))
                .executableRequest(entity.getValue(REQUEST))
                .executionPeriodSeconds(entity.getLong(EXECUTION_PERIOD_SECONDS))
                .executionDelay(entity.getLong(EXECUTION_DELAY))
                .build();

        EntityFromModelMapper<InfinityProcessRequest> infinityProcessRequestFromModelMapper = request -> entityBuilder()
                .stringField(SERVLET_PATH, request.getExecutableServletPath())
                .stringField(SERVICE_ID, request.getExecutableServiceId())
                .stringField(METHOD_ID, request.getExecutableMethodId())
                .valueField(REQUEST, request.getExecutableRequest())
                .longField(EXECUTION_PERIOD_SECONDS, request.getExecutionPeriodSeconds())
                .longField(EXECUTION_DELAY, request.getExecutionDelay())
                .build();
    }


    interface InfinityProcessMapper {
        EntityToModelMapper<InfinityProcess> infinityProcessToModelMapper = entity -> InfinityProcess.builder()
                .id(entity.getString(ID))
                .executableServletPath(entity.getString(SERVLET_PATH))
                .executableServiceId(entity.getString(SERVICE_ID))
                .executableMethodId(entity.getString(METHOD_ID))
                .executableRequest(entity.getValue(REQUEST))
                .executionPeriodSeconds(entity.getLong(EXECUTION_PERIOD_SECONDS))
                .executionDelay(entity.getLong(EXECUTION_DELAY))
                .build();

        EntityFromModelMapper<InfinityProcess> infinityProcessFromModelMapper = process -> entityBuilder()
                .stringField(ID, process.getId())
                .stringField(SERVLET_PATH, process.getExecutableServletPath())
                .stringField(SERVICE_ID, process.getExecutableServiceId())
                .stringField(METHOD_ID, process.getExecutableMethodId())
                .valueField(REQUEST, process.getExecutableRequest())
                .longField(EXECUTION_PERIOD_SECONDS, process.getExecutionPeriodSeconds())
                .longField(EXECUTION_DELAY, process.getExecutionDelay())
                .build();
    }

    interface InfinityProcessCollectionMapper {
        EntityToModelMapper<Set<InfinityProcess>> processCollectionToModelMapper = entity -> entity.getEntityList(PROCESSES)
                .stream()
                .map(infinityProcessToModelMapper::map)
                .collect(toSet());

        EntityFromModelMapper<Set<InfinityProcess>> processCollectionFromModelMapper = processCollection -> entityBuilder()
                .entityCollectionField(PROCESSES, processCollection.stream().map(infinityProcessFromModelMapper::map).collect(toSet()))
                .build();

        ValueMapper<Set<InfinityProcess>, Entity> processCollectionMapper = mapper(processCollectionFromModelMapper, processCollectionToModelMapper);
    }
}
