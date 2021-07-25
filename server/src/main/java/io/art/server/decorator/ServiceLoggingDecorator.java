/*
 * ART
 *
 * Copyright 2019-2021 ART
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
import io.art.logging.logger.*;
import io.art.server.configuration.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.constants.CompilerSuppressingWarnings.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.Property.*;
import static io.art.logging.module.LoggingModule.*;
import static io.art.server.constants.ServerModuleConstants.LoggingMessages.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class ServiceLoggingDecorator implements UnaryOperator<Flux<Object>> {
    private final MethodDecoratorScope scope;
    private final Property<Boolean> enabled;
    ServiceMethodIdentifier id;

    @Getter(lazy = true, value = PRIVATE)
    private final UnaryOperator<Flux<Object>> decorator = createDecorator();

    @Getter(lazy = true, value = PRIVATE)
    private final Logger logger = logger(ServiceLoggingDecorator.class.getName() + SPACE + OPENING_SQUARE_BRACES + scope + CLOSING_SQUARE_BRACES);

    public ServiceLoggingDecorator(ServiceMethodIdentifier id, MethodDecoratorScope scope) {
        this.scope = scope;
        this.id = id;
        enabled = property(() -> withLogging() && configuration().isLogging(id)).listenConsumer(() -> configuration()
                .getConsumer()
                .loggingConsumer());
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return getDecorator().apply(input);
    }

    private void logSubscribe() {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_SUBSCRIBED_MESSAGE, id.getServiceId(), id.getMethodId()));
    }

    private void logComplete() {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_COMPLETED_MESSAGE, id.getServiceId(), id.getMethodId()));
    }

    private void logInput(Object data) {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_INPUT_DATA, id.getServiceId(), id.getMethodId(), data));
    }

    private void logOutput(Object data) {
        if (!enabled.get()) return;
        getLogger().info(format(SERVICE_OUTPUT_DATA, id.getServiceId(), id.getMethodId(), data));
    }

    private void logException(Throwable exception) {
        if (!enabled.get()) return;
        getLogger().error(format(SERVICE_FAILED_MESSAGE, id.getServiceId(), id.getMethodId()), exception);
    }

    @SuppressWarnings(CONSTANT_CONDITIONS)
    private UnaryOperator<Flux<Object>> createDecorator() {
        switch (scope) {
            case INPUT:
                return input -> input
                        .doOnSubscribe(subscription -> logSubscribe())
                        .doOnNext(this::logInput)
                        .doOnError(this::logException);
            case OUTPUT:
                return output -> output
                        .doOnNext(this::logOutput)
                        .doOnError(this::logException)
                        .doOnComplete(this::logComplete);
        }
        return UnaryOperator.identity();
    }

    private ServerModuleConfiguration configuration() {
        return serverModule().configuration();
    }
}
