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

import io.art.core.lazy.*;
import io.art.core.model.*;
import lombok.*;
import org.apache.logging.log4j.*;
import reactor.core.publisher.*;
import static io.art.core.lazy.LazyValue.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.module.ServerModule.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class ServiceDeactivationDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(ServiceDeactivationDecorator.class);
    private final LazyValue<UnaryOperator<Flux<Object>>> decorator = lazy(this::createDecorator);
    private final Supplier<Boolean> enabled;

    public ServiceDeactivationDecorator(ServiceMethodIdentifier serviceMethodId) {
        this.enabled = () -> serverModule().configuration().isDeactivated(serviceMethodId);
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        return decorator.get().apply(input);
    }

    private UnaryOperator<Flux<Object>> createDecorator() {
        return input -> input.filter(ignored -> enabled.get());
    }
}
