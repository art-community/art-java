package ru.adk.module.executor.specification;

import lombok.Getter;
import ru.adk.http.server.model.HttpService;
import ru.adk.http.server.specification.HttpServiceSpecification;
import ru.adk.module.executor.model.ExecutionRequest;
import ru.adk.service.exception.UnknownServiceMethodException;
import static ru.adk.core.caster.Caster.cast;
import static ru.adk.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.adk.http.server.model.HttpService.httpService;
import static ru.adk.http.server.module.HttpServerModule.httpServerModule;
import static ru.adk.module.executor.constants.ModuleExecutorConstants.ServiceConstants.*;
import static ru.adk.module.executor.mapper.ExecutorRequestMapper.executionRequestToModelMapper;
import static ru.adk.module.executor.mapper.ExecutorResponseMapper.executionResponseFromModelMapper;
import static ru.adk.module.executor.service.ModuleExecutorService.executeModule;
import static ru.adk.service.constants.RequestValidationPolicy.VALIDATABLE;

/**
 * Specification with only one method in http server
 * needed to register execute module ModuleExecutorService
 */

@Getter
public class ModuleExecutorServiceSpecification implements HttpServiceSpecification {
    private final String serviceId = MODULE_EXECUTOR_SERVICE_ID;

    private final HttpService httpService = httpService()
            .post(EXECUTE_MODULE)
            .consumes(applicationJsonUtf8())
            .fromBody()
            .validationPolicy(VALIDATABLE)
            .requestMapper(executionRequestToModelMapper)
            .produces(applicationJsonUtf8())
            .responseMapper(executionResponseFromModelMapper)
            .listen(EXECUTE_MODULE_PATH)

            .serve(httpServerModule().getPath());

    @Override
    public <P, R> R executeMethod(String methodId, P request) {
        if (EXECUTE_MODULE.equals(methodId)) {
            return cast(executeModule((ExecutionRequest) request));
        }
        throw new UnknownServiceMethodException(serviceId, methodId);
    }
}
