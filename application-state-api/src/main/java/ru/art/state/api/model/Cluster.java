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
import static java.util.Objects.*;
import static ru.art.core.factory.CollectionsFactory.*;
import static ru.art.state.api.model.ClusterProfile.*;
import static ru.art.state.api.model.ModuleNetwork.*;
import java.util.*;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class Cluster {
    @Builder.Default
    private final Map<String, ClusterProfile> profiles = concurrentHashMap();

    public void putEndpoint(String profileId, String modulePath, ModuleEndpoint endpoint) {
        ClusterProfile clusterProfile;
        if (nonNull(clusterProfile = profiles.get(profileId))) {
            clusterProfile.putEndpoint(modulePath, endpoint);
            return;
        }
        profiles.put(profileId, clusterProfileOf(modulePath, moduleNetworkOf(endpoint)));
    }

    public void removeEndpoint(String profileId, String modulePath, ModuleEndpoint endpoint) {
        ClusterProfile clusterProfile;
        if (nonNull(clusterProfile = profiles.get(profileId))) {
            clusterProfile.removeEndpoint(modulePath, endpoint);
        }
    }
}
