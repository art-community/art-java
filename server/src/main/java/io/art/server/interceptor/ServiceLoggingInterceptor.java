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

import io.art.server.service.implementation.*;
import io.art.server.service.specification.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static com.google.common.base.Throwables.*;
import static io.art.core.caster.Caster.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;

public class ServiceLoggingInterceptor implements ServiceExecutionInterceptor<Object, Object> {
    private final Logger logger = logger(ServiceLoggingInterceptor.class);

    @Override
    public void intercept(ServiceInterceptionContext<Object, Object> context) {
        ServiceMethodSpecification specification = context.getImplementation().getMethodSpecification();
        switch (specification.getRequestProcessingMode()) {
            case BLOCKING:
                logBlocking(context);
                return;
            case REACTIVE_MONO:
                logReactiveMono(context);
                return;
            case REACTIVE_FLUX:
                logReactiveFlux(context);
        }
    }

    private void logBlocking(ServiceInterceptionContext<Object, Object> context) {
        ServiceMethodImplementation implementation = context.getImplementation();
        logger.info(format(EXECUTION_SERVICE_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), context.getRequest()));
        try {
            context.process();
            logger.info(format(SERVICE_EXECUTED_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), context.getResponse()));
        } catch (Throwable throwable) {
            logger.error(format(SERVICE_FAILED_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), getStackTraceAsString(throwable)));
            throw throwable;
        }
    }

    private void logReactiveMono(ServiceInterceptionContext<Object, Object> context) {
        context.processRequest(Mono.from(cast(context.getRequest())).doOnNext(data -> logReactivePayload(context, data)));
    }

    private void logReactiveFlux(ServiceInterceptionContext<Object, Object> context) {
        context.processRequest(Flux.from(cast(context.getRequest())).doOnNext(data -> logReactivePayload(context, data)));
    }

    private void logReactivePayload(ServiceInterceptionContext<Object, Object> context, Object data) {
        ServiceMethodImplementation implementation = context.getImplementation();
        logger.info(format(EXECUTION_SERVICE_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), data));
        context.process();
        logger.info(format(SERVICE_EXECUTED_MESSAGE, implementation.getServiceId(), implementation.getMethodId(), context.getResponse()));
    }
}
