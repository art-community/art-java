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

package io.art.scheduler.test;

import io.art.scheduler.*;
import io.art.scheduler.executor.deferred.*;
import io.art.scheduler.test.counter.*;
import io.art.scheduler.test.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.initializer.ContextInitializer.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.scheduler.Scheduling.*;
import static io.art.scheduler.executor.deferred.DeferredExecutor.*;
import static io.art.scheduler.module.SchedulerActivator.*;
import static io.art.scheduler.test.comparator.DateTimeApproximateComparator.*;
import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@TestMethodOrder(OrderAnnotation.class)
public class SchedulerTest {
    @BeforeAll
    public static void setup() {
        initialize(scheduler());
    }

    @RepeatedTest(1000)
    public void testSingleTask() {
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        schedule(task);
        ignoreException(water::await);
        assertTrue(task.completed());
    }

    @RepeatedTest(1000)
    public void testMultipleTasks() {
        CountDownLatch water = new CountDownLatch(8);
        List<ScheduledTask> tasks = linkedListOf(
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water)
        );
        tasks.forEach(Scheduling::schedule);
        ignoreException(water::await);
        tasks.forEach(task -> assertTrue(task.completed()));
    }

    @RepeatedTest(10)
    public void testSingleDelayedTask() {
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        LocalDateTime time = now().plusSeconds(1);
        schedule(time, task);
        ignoreException(water::await);
        assertTrue(task.completed());
        assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
    }

    @RepeatedTest(10)
    public void testMultipleDelayedTask() {
        DeferredExecutor executor = deferredExecutor().build();
        CountDownLatch water = new CountDownLatch(8);
        OrderCounter counter = new OrderCounter();
        List<OrderedScheduledTask> tasks = linkedListOf(
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter)
        );
        counter.initialize(tasks);
        LocalDateTime time = now().plusSeconds(1);
        tasks.forEach(task -> executor.execute(task, time));
        ignoreException(water::await);
        for (int index = 0; index < tasks.size(); index++) {
            OrderedScheduledTask task = tasks.get(index);
            assertTrue(task.completed());
            assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
            assertEquals(index, counter.getOrders().get(task));
        }
    }

    @RepeatedTest(5000)
    public void testShutdown() {
        DeferredExecutor executor = deferredExecutor().build();
        CountDownLatch water = new CountDownLatch(8);
        List<ScheduledTask> tasks = linkedListOf(
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water)
        );
        tasks.forEach(task -> executor.execute(task, now()));
        executor.shutdown();
        tasks.forEach(task -> assertTrue(task.completed()));
    }
}
