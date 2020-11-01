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

import lombok.*;
import static io.art.core.caster.Caster.*;
import java.util.function.*;

@Getter
@RequiredArgsConstructor
public class ServiceMethodImplementation {
    private final String serviceId;
    private final String methodId;
    private final Function<?, ?> functor;

    public static <T> ServiceMethodImplementation consumer(Consumer<T> consumer, String serviceId, String methodId) {
        return new ServiceMethodImplementation(serviceId, methodId, request -> {
            consumer.accept(cast(request));
            return null;
        });
    }

    public static <T> ServiceMethodImplementation producer(Supplier<T> producer, String serviceId, String methodId) {
        return new ServiceMethodImplementation(serviceId, methodId, request -> producer.get());
    }

    public static <Input, Output> ServiceMethodImplementation handler(Function<Input, Output> function, String serviceId, String methodId) {
        return new ServiceMethodImplementation(serviceId, methodId, function);
    }

    public Object execute(Object request) {
        return functor.apply(cast(request));
    }
}
