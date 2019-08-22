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

package ru.art.state;

import lombok.*;
import ru.art.core.module.*;
import ru.art.state.api.model.*;
import java.util.*;
import java.util.concurrent.locks.*;

import static java.util.Objects.*;
import static java.util.Optional.*;
import static ru.art.core.factory.CollectionsFactory.*;

@Getter
@Setter
public class ApplicationState implements ModuleState {
    private final Map<String, ReentrantLock> clusterLocks = concurrentHashMap();
    private Cluster cluster = Cluster.builder().build();

    public ClusterProfile getClusterProfile(String profile) {
        return cluster.getProfiles().get(profile);
    }

    public Optional<ModuleNetwork> getModuleNetwork(String profile, String modulePath) {
        ClusterProfile clusterProfile;
        if (isNull(clusterProfile = cluster.getProfiles().get(profile))) {
            return empty();
        }
        return ofNullable(clusterProfile.getModuleNetwork(modulePath));
    }
}
