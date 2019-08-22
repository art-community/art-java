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

import lombok.*;
import ru.art.state.api.model.*;
import java.util.*;

import static java.util.Comparator.*;
import static java.util.Map.*;
import static java.util.stream.Collectors.*;
import static ru.art.core.extension.OptionalExtensions.*;
import static ru.art.core.factory.CollectionsFactory.*;

@Getter
@RequiredArgsConstructor
public class SessionsBalancer implements Balancer {
    private final Map<String, Set<ModuleEndpoint>> moduleEndpoints = concurrentHashMap();

    public Collection<ModuleEndpoint> getEndpoints(String modulePath) {
        return moduleEndpoints.get(modulePath);
    }

    @Override
    public void updateEndpoints(Map<String, Collection<ModuleEndpoint>> moduleEndpoints) {
        this.moduleEndpoints.clear();
        this.moduleEndpoints.putAll(moduleEndpoints.entrySet().stream().collect(toMap(Entry::getKey, entry -> treeOf(entry.getValue(), comparingInt(ModuleEndpoint::getSessions)))));
    }

    @Override
    public ModuleEndpoint select(String modulePath) {
        return unwrap(moduleEndpoints.get(modulePath)
                .stream()
                .findFirst()
                .map(response -> ModuleEndpoint.builder().host(response.getHost()).port(response.getPort()).build()));
    }
}
