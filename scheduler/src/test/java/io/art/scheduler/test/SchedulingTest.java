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

import io.art.logging.module.*;
import io.art.scheduler.*;
import io.art.scheduler.module.*;
import io.art.scheduler.test.counter.*;
import io.art.scheduler.test.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.*;
import static io.art.core.context.TestingContext.*;
import static io.art.core.extensions.DateTimeExtensions.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.scheduler.Scheduling.*;
import static io.art.scheduler.module.SchedulerModule.*;
import static java.time.LocalDateTime.*;
import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

@TestMethodOrder(OrderAnnotation.class)
public class SchedulingTest {
    @BeforeAll
    public static void initialize() {
        testing(LoggingModule::new, SchedulerModule::new);
    }

    @Test
    @Order(0)
    public void testSingleTask() {
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        schedule(task);
        ignoreException(water::await);
        assertTrue(task.completed());
    }

    @Test
    @Order(1)
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

    @Test
    @Order(2)
    public void testSingleDelayedTask() {
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        LocalDateTime time = now().plusSeconds(2);
        schedule(time, task);
        ignoreException(water::await);
        assertTrue(task.completed());
        assertEquals(SECONDS.convert(toMillis(time), MILLISECONDS), SECONDS.convert(toMillis(task.completionTimeStamp()), MILLISECONDS));
    }

    @Test
    @Order(3)
    public void testMultipleDelayedTask() {
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
        LocalDateTime time = now().plusSeconds(2);
        tasks.forEach(task -> schedule(time, task));
        ignoreException(water::await);
        for (int index = 0; index < tasks.size(); index++) {
            OrderedScheduledTask task = tasks.get(index);
            assertTrue(task.completed());
            assertEquals(SECONDS.convert(toMillis(time), MILLISECONDS), SECONDS.convert(toMillis(task.completionTimeStamp()), MILLISECONDS));
            assertEquals(index, counter.getOrders().get(task));
        }
    }

    @Test
    @Order(4)
    public void testShutdown() {
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
        deferredExecutor().shutdown();
        tasks.forEach(task -> assertTrue(task.completed()));
    }
}
