package ru.art.module.executor.specification;

import lombok.Getter;
import ru.art.http.server.model.HttpService;
import ru.art.http.server.specification.HttpServiceSpecification;
import ru.art.module.executor.model.ExecutionRequest;
import ru.art.service.exception.UnknownServiceMethodException;
import static ru.art.core.caster.Caster.cast;
import static ru.art.http.constants.MimeToContentTypeMapper.applicationJsonUtf8;
import static ru.art.http.server.model.HttpService.httpService;
import static ru.art.http.server.module.HttpServerModule.httpServerModule;
import static ru.art.module.executor.constants.ModuleExecutorConstants.ServiceConstants.*;
import static ru.art.module.executor.mapper.ExecutorRequestMapper.executionRequestToModelMapper;
import static ru.art.module.executor.mapper.ExecutorResponseMapper.executionResponseFromModelMapper;
import static ru.art.module.executor.service.ModuleExecutorService.executeModule;
import static ru.art.service.constants.RequestValidationPolicy.VALIDATABLE;

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
