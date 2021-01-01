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
import io.art.model.implementation.server.*;
import static io.art.core.checker.EmptinessChecker.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.model.constants.ModelConstants.ConfiguratorScope.*;
import static java.util.Arrays.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

public class ServerModelConfigurator {
    private final ImmutableSet.Builder<RsocketServiceModelConfigurator> rsocketServices = immutableSetBuilder();

    @SafeVarargs
    public final ServerModelConfigurator rsocket(Class<?> service, UnaryOperator<RsocketServiceModelConfigurator>... configurators) {
        if (isEmpty(configurators)) {
            rsocketServices.add(new RsocketServiceModelConfigurator(service, service.getSimpleName(), CLASS));
            return this;
        }
        stream(configurators)
                .map(configurator -> (Function<RsocketServiceModelConfigurator, RsocketServiceModelConfigurator>) configurator)
                .reduce(Function::andThen)
                .map(configurator -> configurator.apply(new RsocketServiceModelConfigurator(service, service.getSimpleName(), CLASS)))
                .ifPresent(rsocketServices::add);
        return this;
    }

    @SafeVarargs
    public final ServerModelConfigurator rsocket(Class<?> service, String method, UnaryOperator<RsocketServiceModelConfigurator>... configurators) {
        if (isEmpty(configurators)) {
            rsocketServices.add(new RsocketServiceModelConfigurator(service, service.getSimpleName(), METHOD).method(method));
            return this;
        }
        stream(configurators)
                .map(configurator -> (Function<RsocketServiceModelConfigurator, RsocketServiceModelConfigurator>) configurator)
                .reduce(Function::andThen)
                .map(configurator -> configurator.apply(new RsocketServiceModelConfigurator(service, service.getSimpleName(), METHOD).method(method)))
                .ifPresent(rsocketServices::add);
        return this;
    }

    ServerModel configure() {
        ImmutableMap<String, RsocketServiceModel> rsocketServices = this.rsocketServices.build()
                .stream()
                .map(RsocketServiceModelConfigurator::configure)
                .collect(immutableMapCollector(RsocketServiceModel::getId, identity()));
        return ServerModel.builder().rsocketServices(rsocketServices).build();
    }
}
