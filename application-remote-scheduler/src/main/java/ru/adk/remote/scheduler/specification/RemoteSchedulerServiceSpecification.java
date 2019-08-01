package ru.adk.remote.scheduler.specification;

import lombok.Getter;
import ru.adk.grpc.server.model.GrpcService;
import ru.adk.grpc.server.specification.GrpcServiceSpecification;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.remote.scheduler.api.model.DeferredTaskRequest;
import ru.adk.remote.scheduler.api.model.InfinityProcessRequest;
import ru.adk.remote.scheduler.api.model.PeriodicTaskRequest;
import ru.adk.service.Specification;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.core.factory.CollectionsFactory.fixedArrayOf;
import static ru.adk.entity.PrimitiveMapping.stringMapper;
import static ru.adk.grpc.server.constants.GrpcServerModuleConstants.GRPC_SERVICE_TYPE;
import static ru.adk.grpc.server.model.GrpcService.GrpcMethod.grpcMethod;
import static ru.adk.grpc.server.model.GrpcService.grpcService;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.adk.http.server.constants.HttpServerModuleConstants.HTTP_SERVICE_TYPE;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.Methods.*;
import static ru.adk.remote.scheduler.api.constants.RemoteSchedulerApiConstants.REMOTE_SCHEDULER_SERVICE_ID;
import static ru.adk.remote.scheduler.api.mapping.DeferredTaskMappers.DeferredTaskCollectionMapper.deferredTaskCollectionFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.DeferredTaskMappers.DeferredTaskMapper.deferredTaskFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.DeferredTaskMappers.DeferredTaskRequestMapper.deferredTaskRequestToModelMapper;
import static ru.adk.remote.scheduler.api.mapping.InfinityProcessMappers.InfinityProcessCollectionMapper.processCollectionFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.InfinityProcessMappers.InfinityProcessRequestMapper.infinityProcessRequestToModelMapper;
import static ru.adk.remote.scheduler.api.mapping.PeriodicTaskMappers.PeriodicTaskCollectionMapper.periodicTaskCollectionFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.PeriodicTaskMappers.PeriodicTaskMapper.periodicTaskFromModelMapper;
import static ru.adk.remote.scheduler.api.mapping.PeriodicTaskMappers.PeriodicTaskRequestMapper.periodicTaskRequestToModelMapper;
import static ru.adk.remote.scheduler.api.mapping.TaskIdMapper.taskIdMapper;
import static ru.adk.remote.scheduler.constants.RemoteSchedulerModuleConstants.*;
import static ru.adk.remote.scheduler.module.RemoteSchedulerModule.remoteSchedulerModule;
import static ru.adk.remote.scheduler.service.RemoteSchedulerService.*;
import static ru.adk.service.constants.RequestValidationPolicy.NOT_NULL;
import static ru.adk.service.constants.RequestValidationPolicy.VALIDATABLE;
import java.util.List;

@Getter
public class RemoteSchedulerServiceSpecification implements Specification, HttpServiceSpecification, GrpcServiceSpecification {
    private final String serviceId = REMOTE_SCHEDULER_SERVICE_ID;

    private final HttpService httpService = httpService()
            .post(ADD_DEFERRED_TASK)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(deferredTaskRequestToModelMapper)
            .produces(applicationJsonUtf8())
            .responseMapper(taskIdMapper.getFromModel())
            .listen(ADD_DEFERRED_PATH)

            .post(ADD_PERIODIC_TASK)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(periodicTaskRequestToModelMapper)
            .produces(applicationJsonUtf8())
            .responseMapper(taskIdMapper.getFromModel())
            .listen(ADD_PERIODIC_PATH)

            .post(ADD_INFINITY_PROCESS)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(infinityProcessRequestToModelMapper)
            .produces(applicationJsonUtf8())
            .responseMapper(taskIdMapper.getFromModel())
            .listen(ADD_PROCESS_PATH)

            .post(GET_DEFERRED_TASK_BY_ID)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(NOT_NULL)
            .requestMapper(taskIdMapper.getToModel())
            .produces(applicationJsonUtf8())
            .responseMapper(deferredTaskFromModelMapper)
            .listen(GET_DEFERRED_PATH)

            .post(GET_PERIODIC_TASK_BY_ID)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(NOT_NULL)
            .requestMapper(taskIdMapper.getToModel())
            .produces(applicationJsonUtf8())
            .responseMapper(periodicTaskFromModelMapper)
            .listen(GET_PERIODIC_PATH)

            .post(GET_ALL_DEFERRED_TASKS)
            .consumes(applicationJsonUtf8())
            .responseMapper(deferredTaskCollectionFromModelMapper)
            .listen(GET_ALL_DEFERRED_PATH)

            .post(GET_ALL_PERIODIC_TASKS)
            .consumes(applicationJsonUtf8())
            .responseMapper(periodicTaskCollectionFromModelMapper)
            .listen(GET_ALL_PERIODIC_PATH)

            .post(GET_ALL_INFINITY_PROCESSES)
            .consumes(applicationJsonUtf8())
            .responseMapper(processCollectionFromModelMapper)
            .listen(GET_ALL_PROCESSES_PATH)

            .post(CANCEL_PERIODIC_TASK)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(NOT_NULL)
            .requestMapper(taskIdMapper.getToModel())
            .listen(cancel_PATH)

            .serve(remoteSchedulerModule().getHttpPath());

    private final GrpcService grpcService = grpcService()
            .method(ADD_DEFERRED_TASK, grpcMethod()
                    .requestMapper(deferredTaskRequestToModelMapper)
                    .responseMapper(stringMapper.getFromModel()))
            .method(ADD_PERIODIC_TASK, grpcMethod()
                    .requestMapper(periodicTaskRequestToModelMapper)
                    .responseMapper(stringMapper.getFromModel()))
            .method(GET_DEFERRED_TASK_BY_ID, grpcMethod()
                    .requestMapper(stringMapper.getToModel())
                    .validationPolicy(NOT_NULL)
                    .responseMapper(deferredTaskFromModelMapper))
            .method(GET_PERIODIC_TASK_BY_ID, grpcMethod()
                    .requestMapper(stringMapper.getToModel())
                    .validationPolicy(NOT_NULL)
                    .responseMapper(periodicTaskFromModelMapper))
            .method(GET_ALL_DEFERRED_TASKS, grpcMethod()
                    .responseMapper(deferredTaskCollectionFromModelMapper))
            .method(GET_ALL_PERIODIC_TASKS, grpcMethod()
                    .responseMapper(periodicTaskCollectionFromModelMapper))
            .method(GET_ALL_INFINITY_PROCESSES, grpcMethod()
                    .responseMapper(processCollectionFromModelMapper))
            .method(CANCEL_PERIODIC_TASK, grpcMethod()
                    .requestMapper(stringMapper.getToModel())
                    .validationPolicy(NOT_NULL))
            .serve();

    private final List<String> serviceTypes = fixedArrayOf(GRPC_SERVICE_TYPE, HTTP_SERVICE_TYPE);


    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        switch (methodId) {
            case ADD_DEFERRED_TASK:
                return cast(addDeferredTask((DeferredTaskRequest) request));
            case ADD_PERIODIC_TASK:
                return cast(addPeriodicTask((PeriodicTaskRequest) request));
            case ADD_INFINITY_PROCESS:
                return cast(addInfinityProcess((InfinityProcessRequest) request));
            case GET_DEFERRED_TASK_BY_ID:
                return cast(getDeferredTaskById((String) request));
            case GET_PERIODIC_TASK_BY_ID:
                return cast(getPeriodicTaskById((String) request));
            case GET_ALL_DEFERRED_TASKS:
                return cast(getAllDeferredTasks());
            case GET_ALL_PERIODIC_TASKS:
                return cast(getAllPeriodicTasks());
            case GET_ALL_INFINITY_PROCESSES:
                return cast(getAllInfinityProcesses());
            case CANCEL_PERIODIC_TASK:
                cancelPeriodicTask((String) request);
                return null;
            default:
                throw new UnknownServiceMethodException(serviceId, methodId);
        }
    }
}
