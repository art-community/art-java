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

package io.art.server.implementation;

import io.art.server.specification.*;
import lombok.*;
import static io.art.server.module.ServerModule.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class ServiceMethodImplementation {
    private final String serviceId;
    private final String methodId;
    private final Function<Object, Object> functor;

    @Getter(lazy = true)
    private final ServiceSpecification serviceSpecification = specifications().get(serviceId);

    @Getter(lazy = true)
    private final ServiceMethodSpecification methodSpecification = getServiceSpecification().getMethods().get(methodId);

    public static ServiceMethodImplementation consumer(Consumer<Object> consumer, String serviceId, String methodId) {
        return new ServiceMethodImplementation(serviceId, methodId, request -> {
            consumer.accept(request);
            return null;
        });
    }

    public static ServiceMethodImplementation producer(Supplier<Object> producer, String serviceId, String methodId) {
        return new ServiceMethodImplementation(serviceId, methodId, request -> producer.get());
    }

    public static ServiceMethodImplementation handler(Function<Object, Object> function, String serviceId, String methodId) {
        return new ServiceMethodImplementation(serviceId, methodId, function);
    }

    public Object execute(Object request) {
        return functor.apply(request);
    }
}