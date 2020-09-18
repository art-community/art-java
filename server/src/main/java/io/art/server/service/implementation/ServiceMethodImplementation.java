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

import com.google.common.collect.*;
import io.art.server.constants.ServerModuleConstants.*;
import io.art.server.exception.*;
import io.art.server.interceptor.*;
import io.art.server.service.specification.*;
import lombok.*;
import static io.art.server.constants.ServerModuleConstants.ExceptionsMessages.*;
import static io.art.server.constants.ServerModuleConstants.ServiceMethodImplementationMode.*;
import static io.art.server.module.ServerModule.*;
import static java.text.MessageFormat.*;
import java.util.function.*;


@Getter
@RequiredArgsConstructor
public class ServiceMethodImplementation {
    private Consumer<Object> consumer;
    private Supplier<Object> producer;
    private Function<Object, Object> handler;
    private final ServiceMethodImplementationMode mode;
    private final String serviceId;
    private final String methodId;
    @Getter(lazy = true)
    private final ServiceSpecification serviceSpecification = services().get(serviceId);
    @Getter(lazy = true)
    private final ServiceMethodSpecification methodSpecification = getServiceSpecification().getMethods().get(methodId);

    public static ServiceMethodImplementation consumer(Consumer<Object> consumer, String serviceId, String methodId) {
        ServiceMethodImplementation implementation = new ServiceMethodImplementation(CONSUMER, serviceId, methodId);
        implementation.consumer = consumer;
        return implementation;
    }

    public static ServiceMethodImplementation producer(Supplier<Object> producer, String serviceId, String methodId) {
        ServiceMethodImplementation implementation = new ServiceMethodImplementation(PRODUCER, serviceId, methodId);
        implementation.producer = producer;
        return implementation;
    }

    public static ServiceMethodImplementation handler(Function<Object, Object> handler, String serviceId, String methodId) {
        ServiceMethodImplementation implementation = new ServiceMethodImplementation(HANDLER, serviceId, methodId);
        implementation.handler = handler;
        return implementation;
    }

    public Object execute() {
        if (mode == PRODUCER) {
            return producer.get();
        }
        throw new ServiceMethodExecutionException(format(UNKNOWN_SERVICE_METHOD_IMPLEMENTATION_MODE, mode));
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
        throw new ServiceMethodExecutionException(format(UNKNOWN_SERVICE_METHOD_IMPLEMENTATION_MODE, mode));    }
}
