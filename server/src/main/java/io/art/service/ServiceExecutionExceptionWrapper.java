/*
 * ART
 *
 * Copyright 2020 ART
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

package io.art.service;

import lombok.*;
import io.art.service.constants.*;
import io.art.service.exception.*;
import io.art.service.model.*;
import static java.util.Objects.*;
import static io.art.service.ServerModule.*;
import static io.art.service.execution.ServiceExecutionWrapper.*;
import static io.art.service.factory.ServiceResponseFactory.*;

public class ServiceExecutionExceptionWrapper {
    @Getter
    @Setter
    protected ServiceExecutionExceptionWrapper previousWrapper;

    <RequestType, ResponseType> ServiceResponse<ResponseType> executeServiceWrapped(ServiceMethodCommand command, ServiceRequest<RequestType> request) {
        try {
            return wrapServiceExecution(command, request.getRequestData());
        } catch (ServiceExecutionException throwable) {
            return errorResponse(command, throwable);
        } catch (Throwable throwable) {
            return errorResponse(command, ServiceErrorCodes.UNCAUGHT_INTERNAL_ERROR, throwable);
        }
    }

    public ServiceExecutionExceptionWrapper addExceptionWrapper(ServiceExecutionExceptionWrapper wrapper) {
        if (isNull(previousWrapper)) {
            previousWrapper = wrapper;
            return this;
        }
        ServiceExecutionExceptionWrapper currentWrapper = previousWrapper;
        ServiceExecutionExceptionWrapper serviceExecutionExceptionWrapper = previousWrapper.getPreviousWrapper();

        while (serviceExecutionExceptionWrapper != null && !serviceExecutionExceptionWrapper.getClass().equals(ServiceExecutionExceptionWrapper.class)) {
            currentWrapper = serviceExecutionExceptionWrapper;
            serviceExecutionExceptionWrapper = currentWrapper.getPreviousWrapper();
        }

        if (isNull(serviceExecutionExceptionWrapper)) {
            return this;
        }

        wrapper.setPreviousWrapper(serviceExecutionExceptionWrapper);
        currentWrapper.setPreviousWrapper(wrapper);
        return this;
    }


    public <RequestType, ResponseType> ServiceResponse<ResponseType> wrapServiceExecution(ServiceMethodCommand command, RequestType request) throws Exception {
        Specification service = serviceModuleState().getServiceRegistry().getService(command.getServiceId());
        ResponseType responseData = executeServiceWithConfiguration(() -> service.executeMethod(command.getMethodId(), request), command, service.getExecutionConfiguration());
        return okResponse(command, responseData);
    }
}
