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

import io.art.core.model.*;
import io.art.core.property.*;
import io.art.server.configuration.*;
import reactor.core.publisher.*;
import static io.art.core.property.Property.*;
import java.util.function.*;

public class ServiceDeactivationDecorator implements UnaryOperator<Flux<Object>> {
    private final Property<Boolean> enabled;
    private final ServerConfiguration configuration;

    public ServiceDeactivationDecorator(ServiceMethodIdentifier id, ServerConfiguration configuration) {
        this.configuration = configuration;
        this.enabled = property(enabled(id)).listenConsumer(() -> configuration
                .getConsumer()
                .deactivationConsumer());
    }

    @Override
    public Flux<Object> apply(Flux<Object> input) {
        if (!enabled.get()) return Flux.empty();
        return input.filter(ignored -> enabled.get());
    }

    private Supplier<Boolean> enabled(ServiceMethodIdentifier serviceMethodId) {
        return () -> !configuration.isDeactivated(serviceMethodId);
    }
}
