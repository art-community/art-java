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

package io.art.model.configurator;

import io.art.core.collection.*;
import io.art.model.implementation.communicator.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.factory.ArrayFactory.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
public class CommunicatorModelConfigurator {
    private final ImmutableSet.Builder<RsocketCommunicatorModelConfigurator> rsocketCommunicators = immutableSetBuilder();

    @SafeVarargs
    public final CommunicatorModelConfigurator rsocket(Class<?> proxyClass, UnaryOperator<RsocketCommunicatorModelConfigurator>... configurators) {
        streamOf(configurators)
                .map(configurator -> (Function<RsocketCommunicatorModelConfigurator, RsocketCommunicatorModelConfigurator>) configurator)
                .reduce(Function::andThen)
                .map(configurator -> configurator.apply(new RsocketCommunicatorModelConfigurator(proxyClass.getSimpleName(), proxyClass, identity())))
                .ifPresent(rsocketCommunicators::add);
        return this;
    }

    CommunicatorModuleModel configure() {
        ImmutableMap<String, RsocketCommunicatorModel> rsocket = this.rsocketCommunicators.build()
                .stream()
                .map(RsocketCommunicatorModelConfigurator::configure)
                .collect(immutableMapCollector(RsocketCommunicatorModel::getId, identity()));
        return new CommunicatorModuleModel(rsocket);
    }
}