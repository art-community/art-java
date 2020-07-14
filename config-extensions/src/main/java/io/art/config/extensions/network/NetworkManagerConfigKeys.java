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

package io.art.config.extensions.network;

public interface NetworkManagerConfigKeys {
    String NETWORK_SECTION_ID = "network";
    String IP_ADDRESS = "ipAddress";
    String BALANCER_MODE = "balancerMode";
    String ALIVE_CONNECTION_UPDATE_SECONDS = "aliveConnectionUpdateSeconds";
    String REFRESH_RATE_SECONDS = "refreshRateSeconds";
    String STATE_HOST = "state.host";
    String STATE_PORT = "state.port";
    String STATE_PATH = "state.path";
}
