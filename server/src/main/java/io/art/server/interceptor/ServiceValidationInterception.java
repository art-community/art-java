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

package io.art.server.interceptor;

import io.art.server.exception.*;
import io.art.server.service.validation.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.REQUEST_DATA_IS_NULL_CODE;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.VALIDATION_EXCEPTION_CODE;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.NON_VALIDATABLE;
import static io.art.server.constants.ServerModuleConstants.RequestValidationPolicy.NOT_NULL;
import static java.util.Objects.*;
import static io.art.server.constants.RequestValidationPolicy.*;
import static io.art.server.constants.ServiceExceptionsMessages.*;
import static io.art.server.model.ServiceInterceptionResult.*;

public class ServiceValidationInterception implements ServiceRequestInterception {
    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        if (request.getValidationPolicy() == NOT_NULL) {
            if (isNull(request.getRequestData())) throw new ValidationException(REQUEST_DATA_IS_NULL);
            return nextInterceptor(request);
        }
        if (request.getValidationPolicy() == NON_VALIDATABLE) return nextInterceptor(request);
        if (isNull(request.getRequestData())) {
            return stopHandling(request, ServiceResponse.builder()
                    .command(request.getServiceMethodCommand())
                    .serviceException(new ServiceExecutionException(request.getServiceMethodCommand(), REQUEST_DATA_IS_NULL_CODE, new ValidationException(REQUEST_DATA_IS_NULL)))
                    .build());
        }
        try {
            Validatable requestData = (Validatable) request.getRequestData();
            requestData.onValidating(new Validator(requestData));
        } catch (Throwable throwable) {
            return stopHandling(request, ServiceResponse.builder()
                    .command(request.getServiceMethodCommand())
                    .serviceException(new ServiceExecutionException(request.getServiceMethodCommand(), VALIDATION_EXCEPTION_CODE, throwable))
                    .build());
        }
        return nextInterceptor(request);
    }
}
