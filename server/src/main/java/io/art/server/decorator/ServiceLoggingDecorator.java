/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import io.art.core.exception.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.server.configuration.*;
import reactor.core.publisher.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.property.Property.*;
import static io.art.logging.Logging.*;
import static io.art.server.constants.ServerConstants.LoggingMessages.*;
import static io.art.transport.extensions.TransportExtensions.*;
import static java.text.MessageFormat.*;
import java.util.function.*;

public class ServiceLoggingDecorator implements UnaryOperator<Flux<Object>> {
    private final MethodDecoratorScope scope;
    private final Property<Boolean> enabled;
    private final ServiceMethodIdentifier id;
    private final Logger logger;
    private final UnaryOperator<Flux<Object>> decorator;

    public ServiceLoggingDecorator(ServiceMethodIdentifier id, ServerConfiguration configuration, MethodDecoratorScope scope) {
        this.scope = scope;
        this.id = id;
        decorator = createDecorator();
        logger = logger(SERVER_LOGGER + SPACE + OPENING_SQUARE_BRACES + scope + CLOSING_SQUARE_BRACES);
        enabled = property(() -> withLogging() && configuration.isLogging(id)).listenConsumer(() -> configuration
                .getConsumer()
                .loggingConsumer());
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return decorator.apply(input);
    }

    private void logSubscribe() {
        if (!enabled.get()) return;
        logger.info(format(SERVICE_SUBSCRIBED_MESSAGE, id.getServiceId(), id.getMethodId()));
    }

    private void logComplete() {
        if (!enabled.get()) return;
        logger.info(format(SERVICE_COMPLETED_MESSAGE, id.getServiceId(), id.getMethodId()));
    }

    private void logInput(Object data) {
        if (!enabled.get()) return;
        logger.info(format(SERVICE_INPUT_DATA, id.getServiceId(), id.getMethodId(), toPrettyString(data)));
    }

    private void logOutput(Object data) {
        if (!enabled.get()) return;
        logger.info(format(SERVICE_OUTPUT_DATA, id.getServiceId(), id.getMethodId(), toPrettyString(data)));
    }

    private void logException(Throwable exception) {
        logger.error(format(SERVICE_FAILED_MESSAGE, id.getServiceId(), id.getMethodId()), exception);
    }

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
        throw new ImpossibleSituationException();
    }
}
