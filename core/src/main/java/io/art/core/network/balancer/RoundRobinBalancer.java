/*
 * ART Java
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

package io.art.core.network.balancer;

import io.art.core.property.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.property.LazyProperty.*;
import java.util.*;
import java.util.concurrent.atomic.*;


public class RoundRobinBalancer<T> implements Balancer<T> {
    @Getter
    @Accessors(fluent = true)
    private List<T> endpoints = linkedList();
    private final AtomicInteger position = new AtomicInteger(0);
    private final LazyProperty<Integer> count = lazy(endpoints::size);

    @Override
    public T select() {
        return endpoints.get(next());
    }

    @Override
    public void endpoints(Collection<T> endpoints) {
        this.endpoints = linkedListOf(endpoints);
    }

    @Override
    public void addEndpoint(T endpoint) {
        endpoints.add(endpoint);
    }

    private int next() {
        for (; ; ) {
            int current = position.get();
            int next = current + 1;
            if (next >= count.get()) {
                next = 0;
            }
            if (position.compareAndSet(current, next))
                return current;
        }
    }

}
