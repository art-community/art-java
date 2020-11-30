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

package io.art.model.modeler;

import io.art.core.collection.*;
import io.art.model.implementation.*;
import io.art.rsocket.configuration.*;
import lombok.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.model.constants.ModelConstants.Protocol.*;
import static java.util.function.Function.*;
import static lombok.AccessLevel.*;
import java.util.function.*;

@Getter(value = PACKAGE)
public class ServerModeler {
    private final ImmutableSet.Builder<ServiceModeler<?>> services = immutableSetBuilder();

    public ServerModeler rsocket(UnaryOperator<ServiceModeler<RsocketServiceConfiguration>> modeler) {
        services.add(modeler.apply(new ServiceModeler<>(RSOCKET)));
        return this;
    }

    ServerModel toModel() {
        ImmutableMap<String, ServiceModel<?>> services = this.services.build()
                .stream()
                .map(ServiceModeler::apply)
                .collect(immutableMapCollector(ServiceModel::getId, identity()));
        return new ServerModel(services);
    }
}
