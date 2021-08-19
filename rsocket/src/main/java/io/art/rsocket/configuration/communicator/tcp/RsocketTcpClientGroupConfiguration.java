/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.rsocket.configuration.communicator.tcp;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import io.rsocket.transport.netty.client.*;
import lombok.Builder;
import lombok.*;
import reactor.netty.tcp.*;
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
public class RsocketTcpClientGroupConfiguration {
    private String connector;
    private ImmutableSet<RsocketTcpClientConfiguration> clientConfigurations;
    private BalancerMethod balancer;
    private UnaryOperator<TcpClient> clientDecorator;
    private UnaryOperator<TcpClientTransport> transportDecorator;

    public static RsocketTcpClientGroupConfiguration defaults(String connector) {
        RsocketTcpClientGroupConfiguration configuration = RsocketTcpClientGroupConfiguration.builder().build();
        configuration.balancer = ROUND_ROBIN;
        configuration.clientConfigurations = emptyImmutableSet();
        configuration.connector = connector;
        configuration.clientDecorator = identity();
        configuration.transportDecorator = identity();
        return configuration;
    }

    public static RsocketTcpClientGroupConfiguration from(RsocketModuleRefresher refresher, RsocketTcpClientGroupConfiguration current, ConfigurationSource source) {
        RsocketTcpClientGroupConfiguration configuration = RsocketTcpClientGroupConfiguration.builder().build();
        configuration.connector = current.connector;
        configuration.clientDecorator = current.clientDecorator;
        configuration.transportDecorator = current.transportDecorator;
        configuration.balancer = rsocketBalancer(source.getString(BALANCER_KEY), current.balancer);

        ImmutableSet<RsocketTcpClientConfiguration> clientConfigurations = immutableSetOf(source.getNestedArray(TARGETS_SECTION, nested -> clientConfiguration(refresher, current, nested)));
        configuration.clientConfigurations = merge(current.clientConfigurations, clientConfigurations);
        return configuration;
    }

    private static RsocketTcpClientConfiguration clientConfiguration(RsocketModuleRefresher refresher, RsocketTcpClientGroupConfiguration current, NestedConfiguration nested) {
        RsocketTcpClientConfiguration defaults = RsocketTcpClientConfiguration.defaults(current.connector);
        return RsocketTcpClientConfiguration.from(refresher, defaults, nested);
    }
}
