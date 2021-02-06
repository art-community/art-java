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
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.server.configuration.*;
import io.art.server.specification.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.Property.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import java.util.*;
import java.util.function.*;

public class ServiceLoggingDecorator implements UnaryOperator<Flux<Object>> {
    private final MethodDecoratorScope scope;
    private final ServiceMethodIdentifier serviceMethodId;
    private final Property<Boolean> enabled;

    @Getter(lazy = true, value = PRIVATE)
    private final UnaryOperator<Flux<Object>> decorator = createDecorator();

    @Getter(lazy = true, value = PRIVATE)
    private final Logger logger = logger(ServiceLoggingDecorator.class.getSimpleName() + SPACE + OPENING_SQUARE_BRACES + scope + CLOSING_SQUARE_BRACES);

    public ServiceLoggingDecorator(ServiceMethodIdentifier serviceMethodId, MethodDecoratorScope scope) {
        this.scope = scope;
        this.serviceMethodId = serviceMethodId;
        enabled = property(() -> configuration().isLogging(serviceMethodId)).listenConsumer(() -> configuration().getConsumer().serverLoggingConsumer());
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return getDecorator().apply(input);
    }

    private void logSubscribe(ServiceMethodSpecification specification) {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_SUBSCRIBED_MESSAGE, specification.getServiceId(), specification.getMethodId()));
    }

    private void logComplete(ServiceMethodSpecification specification) {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_COMPLETED_MESSAGE, specification.getServiceId(), specification.getMethodId()));
    }

    private void logInput(Object data, ServiceMethodSpecification specification) {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_INPUT_DATA, specification.getServiceId(), specification.getMethodId(), data));
    }

    private void logOutput(Object data, ServiceMethodSpecification specification) {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_OUTPUT_DATA, specification.getServiceId(), specification.getMethodId(), data));
    }

    private void logException(Throwable exception, ServiceMethodSpecification specification) {
        if (!enabled.get()) return;
        getLogger().error(format(SERVICE_FAILED_MESSAGE, specification.getServiceId(), specification.getMethodId()), exception);
    }

    private UnaryOperator<Flux<Object>> createDecorator() {
        Optional<ServiceMethodSpecification> possibleSpecification = specifications().findMethodById(serviceMethodId);
        if (!possibleSpecification.isPresent()) {
            return UnaryOperator.identity();
        }
        ServiceMethodSpecification specification = possibleSpecification.get();
        switch (scope) {
            case INPUT:
                return input -> input
                        .doOnSubscribe(subscription -> logSubscribe(specification))
                        .doOnNext(data -> logInput(data, specification))
                        .doOnError(exception -> logException(exception, specification));
            case OUTPUT:
                return output -> output
                        .doOnNext(data -> logOutput(data, specification))
                        .doOnError(exception -> logException(exception, specification))
                        .doOnComplete(() -> logComplete(specification));
        }
        return UnaryOperator.identity();
    }

    private ServerModuleConfiguration configuration() {
        return serverModule().configuration();
    }
}
