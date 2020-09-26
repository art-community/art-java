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
import static io.art.core.checker.NullityChecker.orElse;
import static io.art.core.model.InterceptionResult.next;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static io.art.server.interceptor.ServiceMethodInterceptor.ExceptionInterceptionResult.*;
import static java.text.MessageFormat.*;

public class ServiceLoggingInterceptor implements ServiceMethodInterceptor<Object, Object> {
    private final static Logger logger = logger(ServiceLoggingInterceptor.class);

    @Override
    public InterceptionResult interceptRequest(Object request, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        switch (specification.getRequestProcessingMode()) {
            case BLOCKING:
                logBlockingRequest(request, specification);
                break;
            case REACTIVE_MONO:
                request = orElse(request, Mono.empty());
                logger.info(format(STARTING_REACTIVE_SERVICE_MESSAGE, implementation.getServiceId(), implementation.getMethodId()));
                return next(Mono.from(cast(request)).doOnNext(data -> logReactiveInput(data, specification)));
            case REACTIVE_FLUX:
                request = orElse(request, Flux.empty());
                logger.info(format(STARTING_REACTIVE_SERVICE_MESSAGE, implementation.getServiceId(), implementation.getMethodId()));
                return next(Flux.from(cast(request)).doOnNext(data -> logReactiveInput(data, specification)));
        }
        return next(request);
    }

    @Override
    public InterceptionResult interceptResponse(Object response, ServiceMethodSpecification specification) {
        switch (specification.getResponseProcessingMode()) {
            case BLOCKING:
                logBlockingResponse(response, specification);
                break;
            case REACTIVE_MONO:
                return next(Mono.from(cast(response))
                        .doOnNext(data -> logReactiveOutput(data, specification))
                        .doOnError(exception -> logException(exception, specification)));
            case REACTIVE_FLUX:
                return next(Flux.from(cast(response))
                        .doOnNext(data -> logReactiveOutput(data, specification))
                        .doOnError(exception -> logException(exception, specification)));
        }
        return next(response);
    }

    @Override
    public ExceptionInterceptionResult interceptException(Throwable throwable, ServiceMethodSpecification specification) {
        logException(throwable, specification);
        return exceptionInterceptionResult().inException(throwable).outException(throwable).build();
    }

    private static void logBlockingRequest(Object data, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.info(format(EXECUTING_BLOCKING_SERVICE_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
    }

    private static void logBlockingResponse(Object data, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.info(format(BLOCKING_SERVICE_EXECUTED_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
    }

    private static void logReactiveInput(Object data, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.info(format(REACTIVE_SERVICE_INPUT_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
    }

    private static void logReactiveOutput(Object data, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.info(format(REACTIVE_SERVICE_OUTPUT_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
    }

    private static void logException(Throwable exception, ServiceMethodSpecification specification) {
        ServiceMethodImplementation implementation = specification.getImplementation();
        logger.error(format(SERVICE_FAILED_MESSAGE, implementation.getServiceId(), implementation.getMethodId()), exception);
    }
}
