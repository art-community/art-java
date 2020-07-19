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
import org.apache.logging.log4j.*;
import io.art.logging.*;
import io.art.service.exception.*;
import io.art.service.interceptor.*;
import io.art.service.model.*;
import static java.text.MessageFormat.*;
import static java.util.Objects.*;
import static lombok.AccessLevel.*;
import static org.apache.logging.log4j.ThreadContext.*;
import static io.art.core.checker.EmptinessChecker.isEmpty;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.extensions.NullCheckingExtensions.*;
import static io.art.core.factory.CollectionsFactory.*;
import static io.art.logging.LoggingModule.*;
import static io.art.logging.LoggingModuleConstants.DEFAULT_REQUEST_ID;
import static io.art.logging.LoggingModuleConstants.LoggingParameters.*;
import static io.art.logging.LoggingContext.*;
import static io.art.logging.ThreadContextExtensions.*;
import static io.art.service.ServiceModule.*;
import static io.art.service.constants.ServiceLoggingMessages.*;
import static io.art.service.constants.ServiceModuleConstants.*;
import static io.art.service.model.ServiceInterceptionResult.*;
import java.util.*;

public class ServiceLoggingInterception implements ServiceRequestInterception, ServiceResponseInterception {
    private final static ThreadLocal<Stack<ServiceLoggingContext>> serviceLoggingParameters = new ThreadLocal<>();
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = loggingModule().getLogger(ServiceLoggingInterception.class);

    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request) {
        if (isNull(serviceLoggingParameters.get())) {
            serviceLoggingParameters.set(stackOf());
        }
        ServiceLoggingContext parameters = ServiceLoggingContext.builder()
                .serviceId(request.getServiceMethodCommand().getServiceId())
                .serviceMethodId(request.getServiceMethodCommand().toString())
                .serviceMethodCommand(request.getServiceMethodCommand().toString() + DOT + getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID))
                .logEventType(REQUEST_EVENT)
                .loadedServices(serviceModuleState().getServiceRegistry().getServices().keySet())
                .build();
        serviceLoggingParameters.get().push(parameters);
        putRequestResponseParameters(request, parameters);
        getLogger().info(format(EXECUTION_SERVICE_MESSAGE, request.getServiceMethodCommand(), getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID), request));
        remove(LOG_EVENT_TYPE);
        return nextInterceptor(request);
    }

    @Override
    public ServiceInterceptionResult intercept(ServiceRequest<?> request, ServiceResponse<?> response) {
        if (!isEmpty(serviceLoggingParameters.get())) {
            putRequestResponseParameters(request, serviceLoggingParameters.get().pop());
        }
        ServiceExecutionException serviceException = response.getServiceException();
        if (nonNull(serviceException)) {
            putIfNotNull(RESPONSE_KEY, response);
            putIfNotNull(SERVICE_EXCEPTION_KEY, serviceException);
            putIfNotNull(LOG_EVENT_TYPE, RESPONSE_EVENT);
            getLogger().error(format(SERVICE_FAILED_MESSAGE,
                    request.getServiceMethodCommand(),
                    getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID),
                    serviceException.getErrorCode(),
                    serviceException.getErrorMessage(),
                    serviceException.getStackTraceText()),
                    serviceException);
            return nextInterceptor(request, response);
        }
        remove(SERVICE_EXCEPTION_KEY);
        putIfNotNull(RESPONSE_KEY, response);
        putIfNotNull(LOG_EVENT_TYPE, RESPONSE_EVENT);
        getLogger().info(format(SERVICE_EXECUTED_MESSAGE, request.getServiceMethodCommand(), getOrElse(get(REQUEST_ID_KEY), DEFAULT_REQUEST_ID), response));
        return nextInterceptor(request, response);
    }

    private static void putRequestResponseParameters(ServiceRequest<?> request, ServiceLoggingContext parameters) {
        putIfNotNull(REQUEST_KEY, request);
        putLoggingParameters(parameters);
        List<String> serviceTypes = serviceModuleState()
                .getServiceRegistry()
                .getService(request.getServiceMethodCommand().getServiceId())
                .getServiceTypes();
        putIfNotNull(SERVICE_TYPES_KEY, serviceTypes);
    }
}
