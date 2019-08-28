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

package ru.art.service;

import lombok.*;
import ru.art.service.constants.*;
import ru.art.service.exception.*;
import ru.art.service.model.*;
import static java.util.Objects.*;
import static ru.art.service.ServiceModule.*;
import static ru.art.service.constants.ServiceExceptionsMessages.*;
import static ru.art.service.execution.ServiceExecutor.*;
import static ru.art.service.factory.ServiceResponseFactory.*;

public class ServiceExecutionExceptionWrapper {
    @Getter
    @Setter
    protected ServiceExecutionExceptionWrapper previousWrapper;

    <RequestType, ResponseType> ServiceResponse<ResponseType> executeServiceWrapped(ServiceMethodCommand command, ServiceRequest<RequestType> request) {
        try {
            return wrapServiceExecution(command, request.getRequestData());
        } catch (ServiceExecutionException e) {
            return errorResponse(command, e);
        } catch (Throwable e) {
            return errorResponse(command, ServiceErrorCodes.UNCAUGHT_INTERNAL_ERROR, e);
        }
    }

    public ServiceExecutionExceptionWrapper addExceptionWrapper(ServiceExecutionExceptionWrapper wrapper) {
        if (isNull(wrapper)) {
            throw new NullPointerException(WRAPPER_IS_NULL);
        }
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
