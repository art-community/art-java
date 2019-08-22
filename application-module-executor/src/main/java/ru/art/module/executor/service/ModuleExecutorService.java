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

package ru.art.module.executor.service;

import ru.art.core.caster.*;
import ru.art.entity.*;
import ru.art.module.executor.model.*;

import static ru.art.core.caster.Caster.*;
import static ru.art.grpc.client.communicator.GrpcCommunicator.*;


/**
 * Main service with module executing method
 */
public interface ModuleExecutorService {

    /**
     * Method sends request to remote module by it's host/port with needed request
     *
     * @param request - data about executing module service method with request
     * @return method response
     */
    static Value executeModule(ExecutionRequest request) {
        return cast(grpcCommunicator(request.getModuleHost(), request.getModulePort(), request.getExecutableServletPath())
                .serviceId(request.getExecutableServiceId())
                .methodId(request.getExecutableMethodId())
                .requestMapper(Caster::cast)
                .responseMapper(Caster::cast)
                .execute(request.getExecutableRequest()).getResponseData());
    }
}