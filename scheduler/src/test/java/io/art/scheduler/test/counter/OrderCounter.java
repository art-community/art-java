/*
 * ART
 *
 * Copyright 2019-2021 ART
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

package io.art.scheduler.test.counter;

import io.art.scheduler.test.model.*;
import lombok.*;
import static io.art.core.factory.MapFactory.*;
import java.util.*;
import java.util.concurrent.atomic.*;

@Getter
public class OrderCounter {
    private final AtomicInteger counter = new AtomicInteger(-1);
    private final Map<OrderedScheduledTask, Integer> orders = map();

    public void initialize(List<OrderedScheduledTask> tasks) {
        for (OrderedScheduledTask task : tasks) {
            orders.put(task, 0);
        }
    }

    public void increment(OrderedScheduledTask task) {
        orders.put(task, counter.incrementAndGet());
    }
}
