/*
 * ART Java
 *
 * Copyright 2019 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.art.module.executor.specification;

import lombok.*;
import ru.art.http.server.model.*;
import ru.art.http.server.specification.*;
import ru.art.module.executor.model.*;
import ru.art.service.exception.*;
import static ru.art.core.caster.Caster.*;
import static ru.art.http.constants.MimeToContentTypeMapper.*;
import static ru.art.http.server.model.HttpService.*;
import static ru.art.http.server.module.HttpServerModule.*;
import static ru.art.module.executor.constants.ModuleExecutorConstants.ServiceConstants.*;
import static ru.art.module.executor.mapper.ExecutorRequestMapper.*;
import static ru.art.module.executor.mapper.ExecutorResponseMapper.*;
import static ru.art.module.executor.service.ModuleExecutorService.*;
import static ru.art.service.constants.RequestValidationPolicy.*;

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
