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

package ru.art.service;

import ru.art.service.constants.ServiceErrorCodes;
import ru.art.service.factory.ServiceResponseFactory;
import ru.art.service.model.ServiceMethodCommand;
import ru.art.service.model.ServiceResponse;

public class RuntimeExceptionWrapper extends ServiceExecutionExceptionWrapper {
    @Override
    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) {
        try {
            return previousWrapper.wrapServiceExecution(command, request);
        } catch (Exception e) {
            return ServiceResponseFactory.errorResponse(command, ServiceErrorCodes.UNDECLARED_INTERNAL_ERROR, e);
        }
    }
}
