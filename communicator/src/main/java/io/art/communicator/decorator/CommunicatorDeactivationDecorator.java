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

package io.art.communicator.decorator;

import io.art.communicator.configuration.*;
import io.art.core.model.*;
import io.art.core.property.*;
import io.art.logging.logger.*;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.property.Property.*;
import static io.art.logging.Logging.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

public class CommunicatorDeactivationDecorator implements UnaryOperator<Flux<Object>> {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(CommunicatorDeactivationDecorator.class);
    private final Property<Boolean> enabled;
    private final CommunicatorConfiguration configuration;

    public CommunicatorDeactivationDecorator(CommunicatorActionIdentifier id, CommunicatorConfiguration configuration) {
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

    private Supplier<Boolean> enabled(CommunicatorActionIdentifier id) {
        return () -> !configuration.isDeactivated(id);
    }
}
