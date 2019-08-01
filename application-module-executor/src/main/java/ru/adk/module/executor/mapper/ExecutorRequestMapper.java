package ru.adk.module.executor.mapper;

import ru.adk.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import ru.adk.module.executor.model.ExecutionRequest;
import static ru.adk.module.executor.mapper.ExecutorRequestMapper.Fields.*;

/**
 * Mapper for {@link ExecutionRequest}
 * Maps from Entity to pojo object
 * Used in http server
 */

public interface ExecutorRequestMapper {
    EntityToModelMapper<ExecutionRequest> executionRequestToModelMapper = entity -> ExecutionRequest.builder()
            .moduleHost(entity.getString(MODULE_HOST))
            .modulePort(entity.getInt(MODULE_PORT))
            .executableServletPath(entity.getString(SERVLET_PATH))
            .executableServiceId(entity.getString(SERVICE_ID))
            .executableMethodId(entity.getString(METHOD_ID))
            .executableRequest(entity.getValue(REQUEST))
            .build();

    interface Fields {
        String MODULE_HOST = "moduleHost";
        String MODULE_PORT = "modulePort";
        String SERVLET_PATH = "executableServletPath";
        String SERVICE_ID = "executableServiceId";
        String METHOD_ID = "executableMethodId";
        String REQUEST = "executableRequest";
    }
}
