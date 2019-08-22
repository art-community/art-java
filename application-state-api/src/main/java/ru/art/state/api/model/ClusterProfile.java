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

package ru.art.state.api.model;

import lombok.*;
import java.util.*;

import static java.util.Objects.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.state.api.model.ModuleNetwork.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ClusterProfile {
    @Builder.Default
    private final Map<String, ModuleNetwork> modules = mapOf();

    static ClusterProfile clusterProfileOf(String modulePath, ModuleNetwork network) {
        ClusterProfile profile = new ClusterProfile();
        profile.modules.put(modulePath, network);
        return profile;
    }

    public void putEndpoint(String modulePath, ModuleEndpoint endpoint) {
        ModuleNetwork moduleNetwork;
        if (nonNull(moduleNetwork = modules.get(modulePath))) {
            moduleNetwork.addEndpoint(endpoint);
            return;
        }
        modules.put(modulePath, moduleNetworkOf(endpoint));
    }

    public void removeEndpoint(String modulePath, ModuleEndpoint endpoint) {
        ModuleNetwork moduleNetwork;
        if (nonNull(moduleNetwork = modules.get(modulePath))) {
            moduleNetwork.removeEndpoint(endpoint);
        }
    }

    public ModuleNetwork getModuleNetwork(String modulePath) {
        return modules.get(modulePath);
    }
}
