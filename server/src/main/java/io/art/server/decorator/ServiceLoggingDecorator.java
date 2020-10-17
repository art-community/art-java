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

package io.art.server.decorator;

import io.art.core.constants.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.specification.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class ServiceLoggingDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private final static Logger logger = logger(ServiceLoggingDecorator.class);
    private final UnaryOperator<Flux<Object>> decorator;
    private final Supplier<Boolean> enabled;

    public ServiceLoggingDecorator(ServiceMethodSpecification specification, MethodDecoratorScope scope, Supplier<Boolean> enabled) {
        this.enabled = enabled;
        switch (scope) {
            case INPUT:
                switch (specification.getInputMode()) {
                    case BLOCKING:
                        decorator = input -> input
                                .doOnNext(data -> logBlockingInput(data, specification))
                                .doOnError(exception -> logException(exception, specification));
                        return;
                    case MONO:
                    case FLUX:
                        decorator = input -> input
                                .doOnSubscribe(subscription -> logReactiveSubscribe(specification))
                                .doOnNext(data -> logReactiveInput(data, specification))
                                .doOnError(exception -> logException(exception, specification));
                        return;
                }
            case OUTPUT:
                switch (specification.getInputMode()) {
                    case BLOCKING:
                        decorator = input -> input
                                .doOnNext(data -> logBlockingOutput(data, specification))
                                .doOnError(exception -> logException(exception, specification));
                        return;
                    case MONO:
                    case FLUX:
                        decorator = input -> input
                                .doOnNext(data -> logReactiveOutput(data, specification))
                                .doOnError(exception -> logException(exception, specification));
                        return;
                }
        }
        decorator = UnaryOperator.identity();
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return decorator.apply(input);
    }

    private void logBlockingInput(Object data, ServiceMethodSpecification specification) {
        if (!enabled.get()) {
            return;
        }
        getLogger().info(format(EXECUTING_BLOCKING_SERVICE_MESSAGE, specification.getServiceId(), specification.getMethodId(), data));
    }

    private void logBlockingOutput(Object data, ServiceMethodSpecification specification) {
        if (!enabled.get()) {
            return;
        }
        getLogger().info(format(BLOCKING_SERVICE_EXECUTED_MESSAGE, specification.getServiceId(), specification.getMethodId(), data));
    }

    private void logReactiveSubscribe(ServiceMethodSpecification specification) {
        if (!enabled.get()) {
            return;
        }
        getLogger().info(format(REACTIVE_SERVICE_SUBSCRIBED_MESSAGE, specification.getServiceId(), specification.getMethodId()));
    }

    private void logReactiveInput(Object data, ServiceMethodSpecification specification) {
        if (!enabled.get()) {
            return;
        }
        getLogger().info(format(REACTIVE_SERVICE_INPUT_MESSAGE, specification.getServiceId(), specification.getMethodId(), data));
    }

    private void logReactiveOutput(Object data, ServiceMethodSpecification specification) {
        if (!enabled.get()) {
            return;
        }
        getLogger().info(format(REACTIVE_SERVICE_OUTPUT_MESSAGE, specification.getServiceId(), specification.getMethodId(), data));
    }

    private void logException(Throwable exception, ServiceMethodSpecification specification) {
        if (!enabled.get()) {
            return;
        }
        getLogger().error(format(SERVICE_FAILED_MESSAGE, specification.getServiceId(), specification.getMethodId()), exception);
    }

}
