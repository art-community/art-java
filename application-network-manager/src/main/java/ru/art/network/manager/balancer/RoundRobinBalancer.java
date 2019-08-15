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

import lombok.Getter;
import ru.art.state.api.model.ModuleEndpoint;
import static java.util.stream.Collectors.toMap;
import static ru.art.core.factory.CollectionsFactory.concurrentHashMap;
import static ru.art.core.factory.CollectionsFactory.fixedArrayOf;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RoundRobinBalancer implements Balancer {
    private final Map<String, List<ModuleEndpoint>> moduleEndpoints = concurrentHashMap();
    private final AtomicInteger position = new AtomicInteger(0);
    @Getter(lazy = true)
    private final int listSize = moduleEndpoints.size();

    @Override
    public ModuleEndpoint select(String modulePath) {
        return moduleEndpoints.get(modulePath).get(getNextPosition());
    }

    @Override
    public Collection<ModuleEndpoint> getEndpoints(String modulePath) {
        return moduleEndpoints.get(modulePath);
    }

    @Override
    public void updateEndpoints(Map<String, Collection<ModuleEndpoint>> moduleEndpoints) {
        this.moduleEndpoints.clear();
        this.moduleEndpoints.putAll(moduleEndpoints.entrySet().stream().collect(toMap(Map.Entry::getKey, entry -> fixedArrayOf(entry.getValue()))));
    }

    private int getNextPosition() {
        for (; ; ) {
            int current = position.get();
            int next = current + 1;
            if (next >= getListSize()) {
                next = 0;
            }
            if (position.compareAndSet(current, next))
                return current;
        }
    }

}
