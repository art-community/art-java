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

import io.art.core.model.*;
import io.art.server.service.implementation.*;
import io.art.server.service.specification.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.model.InterceptionResult.next;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static io.art.server.interceptor.ServiceMethodInterceptor.ExceptionInterceptionResult.*;
import static java.text.MessageFormat.*;

public class ServiceLoggingRequestInterceptor implements ServiceMethodInterceptor<Object, Object> {
    private final static Logger logger = logger(ServiceLoggingRequestInterceptor.class);

    @Override
    public InterceptionResult interceptRequest(Object request, ServiceMethodSpecification specification) {
        switch (specification.getRequestProcessingMode()) {
            case BLOCKING:
                logRequest(request, specification);
                break;
            case REACTIVE_MONO:
                return next(Mono.from(cast(request)).doOnNext(data -> logRequest(data, specification)));
            case REACTIVE_FLUX:
                return next(Flux.from(cast(request)).doOnNext(data -> logRequest(data, specification)));
        }
        return next(request);
    }

    @Override
    public InterceptionResult interceptResponse(Object response, ServiceMethodSpecification specification) {
        switch (specification.getResponseProcessingMode()) {
            case BLOCKING:
                logResponse(response, specification);
                break;
            case REACTIVE_MONO:
                return next(Mono.from(cast(response))
                        .doOnNext(data -> logResponse(data, specification))
                        .doOnError(exception -> logException(exception, specification)));
            case REACTIVE_FLUX:
                return next(Flux.from(cast(response))
                        .doOnNext(data -> logResponse(data, specification))
                        .doOnError(exception -> logException(exception, specification)));
        }
        return next(response);
    }

    @Override
    public ExceptionInterceptionResult interceptException(Throwable throwable, ServiceMethodSpecification specification) {
        logException(throwable, specification);
        return exceptionInterceptionResult().inException(throwable).outException(throwable).build();
    }

    private static void logRequest(Object data, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.info(format(EXECUTING_SERVICE_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
    }

    private static void logResponse(Object data, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.info(format(SERVICE_EXECUTED_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
    }

    private static void logException(Throwable exception, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.error(format(SERVICE_FAILED_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), getStackTraceAsString(exception)));
    }
}
