/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.configuration.communicator.ws;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.rsocket.transport.netty.client.*;
import lombok.Builder;
import lombok.*;
import reactor.netty.http.client.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.extensions.CollectionExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;
import static java.util.function.UnaryOperator.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class RsocketWsClientGroupConfiguration {
    private String connector;
    private ImmutableSet<RsocketWsClientConfiguration> clientConfigurations;
    private BalancerMethod balancer;
    private UnaryOperator<HttpClient> clientDecorator;
    private UnaryOperator<WebsocketClientTransport> transportDecorator;

    public static RsocketWsClientGroupConfiguration wsClientGroupConfiguration(String connector) {
        RsocketWsClientGroupConfiguration configuration = RsocketWsClientGroupConfiguration.builder().build();
        configuration.balancer = ROUND_ROBIN;
        configuration.clientConfigurations = emptyImmutableSet();
        configuration.connector = connector;
        configuration.clientDecorator = identity();
        configuration.transportDecorator = identity();
        return configuration;
    }

    public static RsocketWsClientGroupConfiguration wsClientGroupConfiguration(RsocketModuleRefresher refresher, RsocketWsClientGroupConfiguration current, ConfigurationSource source) {
        RsocketWsClientGroupConfiguration configuration = RsocketWsClientGroupConfiguration.builder().build();
        configuration.connector = current.connector;
        configuration.clientDecorator = current.clientDecorator;
        configuration.transportDecorator = current.transportDecorator;
        configuration.balancer = rsocketBalancer(source.getString(BALANCER_KEY), current.balancer);

        ImmutableSet<RsocketWsClientConfiguration> clientConfigurations = immutableSetOf(source.getNestedArray(
                TARGETS_SECTION,
                nested -> clientConfiguration(refresher, current, nested)
        ));
        configuration.clientConfigurations = merge(current.clientConfigurations, clientConfigurations);
        return configuration;
    }

    private static RsocketWsClientConfiguration clientConfiguration(RsocketModuleRefresher refresher, RsocketWsClientGroupConfiguration current, NestedConfiguration nested) {
        RsocketWsClientConfiguration defaults = RsocketWsClientConfiguration.wsClientConfiguration(current.connector);
        return RsocketWsClientConfiguration.wsClientConfiguration(refresher, defaults, nested);
    }
}
