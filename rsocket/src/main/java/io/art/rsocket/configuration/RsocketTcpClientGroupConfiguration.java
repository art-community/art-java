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

package io.art.rsocket.configuration;

import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.rsocket.constants.RsocketModuleConstants.*;
import io.art.rsocket.refresher.*;
import lombok.Builder;
import lombok.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.collection.ImmutableSet.*;
import static io.art.core.factory.SetFactory.*;
import static io.art.rsocket.constants.RsocketModuleConstants.BalancerMethod.*;
import static io.art.rsocket.constants.RsocketModuleConstants.ConfigurationKeys.*;

@Getter
@Builder(toBuilder = true)
public class RsocketTcpClientGroupConfiguration {
    private String connector;
    private BalancerMethod method;
    private ImmutableSet<RsocketTcpClientConfiguration> configurations;

    public static RsocketTcpClientGroupConfiguration defaults(String connector) {
        RsocketTcpClientGroupConfiguration configuration = RsocketTcpClientGroupConfiguration.builder().build();
        configuration.method = ROUND_ROBIN;
        configuration.configurations = emptyImmutableSet();
        configuration.connector = connector;
        return configuration;
    }

    public static RsocketTcpClientGroupConfiguration from(RsocketModuleRefresher refresher, RsocketTcpClientGroupConfiguration current, ConfigurationSource source) {
        RsocketTcpClientGroupConfiguration configuration = RsocketTcpClientGroupConfiguration.builder().build();
        configuration.connector = current.connector;
        configuration.method = rsocketBalancer(source.getString(BALANCER_KEY), current.method);
        configuration.configurations = immutableSetOf(source.getNestedArray(TARGETS_SECTION, nested -> RsocketTcpClientConfiguration.from(
                refresher,
                RsocketTcpClientConfiguration.defaults(current.connector),
                nested
        )));
        return configuration;
    }
}
