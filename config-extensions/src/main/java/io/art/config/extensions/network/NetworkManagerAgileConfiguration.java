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

package io.art.config.extensions.network;

import lombok.*;
import io.art.network.manager.configuration.NetworkManagerModuleConfiguration.*;
import io.art.network.manager.constants.NetworkManagerModuleConstants.*;
import static io.art.config.extensions.ConfigExtensions.*;
import static io.art.config.extensions.network.NetworkManagerConfigKeys.*;
import static io.art.core.extension.ExceptionExtensions.*;

@Getter
public class NetworkManagerAgileConfiguration extends NetworkManagerModuleDefaultConfiguration {
    private String ipAddress;
    private BalancerMode balancerMode;
    private long refreshRateSeconds;
    private long aliveConnectionUpdateSeconds;
    private String stateHost;
    private int statePort;
    private String statePath;

    public NetworkManagerAgileConfiguration() {
        refresh();
    }

    @Override
    public void refresh() {
        ipAddress = configString(NETWORK_SECTION_ID, IP_ADDRESS, super.getIpAddress());
        balancerMode = ifException(() -> BalancerMode.valueOf(configString(NETWORK_SECTION_ID, BALANCER_MODE)), super.getBalancerMode());
        refreshRateSeconds = configLong(NETWORK_SECTION_ID, REFRESH_RATE_SECONDS, super.getRefreshRateSeconds());
        aliveConnectionUpdateSeconds = configLong(NETWORK_SECTION_ID, ALIVE_CONNECTION_UPDATE_SECONDS, super.getConnectionPingRateSeconds());
        stateHost = configString(NETWORK_SECTION_ID, STATE_HOST, super.getStateHost());
        statePath = configString(NETWORK_SECTION_ID, STATE_PATH, super.getStatePath());
        statePort = configInt(NETWORK_SECTION_ID, STATE_PORT, super.getStatePort());
    }
}
