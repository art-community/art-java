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

package ru.art.module.executor.mapper;

import ru.art.entity.mapper.ValueToModelMapper.EntityToModelMapper;
import ru.art.module.executor.model.ExecutionRequest;
import static ru.art.module.executor.mapper.ExecutorRequestMapper.Fields.*;

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
