/*
 * ART Java
 *
 * Copyright 2019 ART
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

package ru.art.network.manager.configuration;

import lombok.Getter;
import ru.art.core.module.ModuleConfiguration;
import ru.art.network.manager.constants.NetworkManagerModuleConstants.BalancerMode;
import static ru.art.core.constants.StringConstants.SLASH;
import static ru.art.core.context.Context.contextConfiguration;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.BalancerMode.ROUND_ROBIN;
import static ru.art.network.manager.constants.NetworkManagerModuleConstants.DEFAULT_STATE_PORT;

public interface NetworkManagerModuleConfiguration extends ModuleConfiguration {
    String getIpAddress();

    BalancerMode getBalancerMode();

    long getRefreshRateSeconds();

    long getConnectionPingRateSeconds();

    String getStateHost();

    String getStatePath();

    int getStatePort();

    @Getter
    class NetworkManagerModuleDefaultConfiguration implements NetworkManagerModuleConfiguration {
        private final BalancerMode balancerMode = ROUND_ROBIN;
        private final long refreshRateSeconds = 5;
        private final long connectionPingRateSeconds = 5;
        private final String ipAddress = contextConfiguration().getIpAddress();
        private final String stateHost = ipAddress;
        private final int statePort = DEFAULT_STATE_PORT;
        private final String statePath = SLASH;
    }
}
