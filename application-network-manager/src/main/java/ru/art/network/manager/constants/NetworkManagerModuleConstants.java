/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.network.manager.constants;

public interface NetworkManagerModuleConstants {
    String NETWORK_MANAGER_MODULE_ID = "NETWORK_MANAGER_MODULE";
    int DEFAULT_STATE_PORT = 8000;

    enum BalancerMode {
        ROUND_ROBIN,
        FEWER_SESSIONS
    }

    interface ExceptionMessages {
        String UNKNOWN_BALANCER_MODE_MESSAGE = "Unknown balancer mode: ''{0}''";
    }

    interface LoggingMessages {
        String REFRESHING_UPDATE = "Error occurred during refreshing endpoints";
    }
}
