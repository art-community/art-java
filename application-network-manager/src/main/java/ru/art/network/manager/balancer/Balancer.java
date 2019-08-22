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

package ru.art.network.manager.balancer;

import ru.art.network.manager.constants.NetworkManagerModuleConstants.*;
import ru.art.network.manager.exception.*;
import ru.art.state.api.model.*;
import java.util.*;

public interface Balancer {
    static Balancer balancerOf(BalancerMode mode) {
        switch (mode) {
            case ROUND_ROBIN:
                return new RoundRobinBalancer();
            case FEWER_SESSIONS:
                return new SessionsBalancer();
        }
        throw new UnknownBalancerModeException(mode);
    }

    ModuleEndpoint select(String modulePath);

    Collection<ModuleEndpoint> getEndpoints(String modulePath);

    void updateEndpoints(Map<String, Collection<ModuleEndpoint>> moduleEndpoints);
}
