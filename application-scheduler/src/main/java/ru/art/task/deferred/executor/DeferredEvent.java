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

package ru.art.task.deferred.executor;

import lombok.Getter;
import static java.lang.System.currentTimeMillis;
import static java.time.ZoneId.systemDefault;
import static java.util.Comparator.comparingLong;
import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.ToLongFunction;

/**
 * Модель отложенного события
 * Используется вместе с {@link DelayQueue<EventResultType>}
 * Хранит в себе ссылку на {@link Future<EventResultType>} таску, время и порядок выполнения таски
 *
 * @param <EventResultType> Тип возвращаемого значения задачи отложенного события
 */
class DeferredEvent<EventResultType> implements Delayed {
    @Getter
    private final Future<EventResultType> task;
    private final long triggerDateTime;
    private final int order;

    DeferredEvent(Future<EventResultType> task, LocalDateTime triggerDateTime, int order) {
        this.task = task;
        this.triggerDateTime = triggerDateTime.atZone(systemDefault()).toInstant().toEpochMilli();
        this.order = order;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.toNanos(triggerDateTime - currentTimeMillis());
    }

    @Override
    public int compareTo(@SuppressWarnings("NullableProblems") Delayed o) {
        return comparingLong((ToLongFunction<DeferredEvent>) DeferredEvent::getTriggerDateTime)
                .thenComparingInt(DeferredEvent::getOrder)
                .compare(this, (DeferredEvent) o);
    }

    long getTriggerDateTime() {
        return triggerDateTime;
    }

    private int getOrder() {
        return order;
    }

}
