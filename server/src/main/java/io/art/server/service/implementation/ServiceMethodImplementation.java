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

package io.art.server.service.implementation;

import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import lombok.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodImplementationMode.*;
import static java.text.MessageFormat.*;
import java.util.function.*;


@RequiredArgsConstructor
public class ServiceMethodImplementation {
    private Consumer<Object> consumer;
    private Supplier<Object> producer;
    private Function<Object, Object> handler;
    private final ServiceMethodImplementationMode mode;

    public static ServiceMethodImplementation consumer(Consumer<Object> consumer) {
        ServiceMethodImplementation implementation = new ServiceMethodImplementation(CONSUMER);
        implementation.consumer = consumer;
        return implementation;
    }

    public static ServiceMethodImplementation producer(Supplier<Object> producer) {
        ServiceMethodImplementation implementation = new ServiceMethodImplementation(PRODUCER);
        implementation.producer = producer;
        return implementation;
    }

    public static ServiceMethodImplementation handler(Function<Object, Object> handler) {
        ServiceMethodImplementation implementation = new ServiceMethodImplementation(HANDLER);
        implementation.handler = handler;
        return implementation;
    }

    public Object execute(Object request) {
        switch (mode) {
            case CONSUMER:
                consumer.accept(request);
                return null;
            case PRODUCER:
                return producer.get();
            case HANDLER:
                return handler.apply(request);
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_SERVICE_METHOD_IMPLEMENTATION_MODE, mode));
    }
}
