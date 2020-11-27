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

package io.art.model.server;

import io.art.core.builder.*;
import io.art.rsocket.configuration.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.model.constants.ModelConstants.Protocol.*;
import java.util.*;
import java.util.function.*;

public class ServerModel {
    private final ImmutableSetBuilder<ServiceModel<?>> services = immutableSet();

    public ServerModel rsocket(UnaryOperator<ServiceModel<RsocketServiceConfiguration>> model) {
        services.add(model.apply(new ServiceModel<>(RSOCKET)));
        return this;
    }

    public Set<ServiceModel<?>> getServices() {
        return services.build();
    }

}
