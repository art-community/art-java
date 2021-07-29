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
import static io.art.core.factory.ListFactory.*;
import static io.art.core.initializer.Initializer.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static io.art.scheduler.Scheduling.*;
import static io.art.scheduler.executor.deferred.DeferredExecutor.*;
import static io.art.scheduler.module.SchedulerActivator.*;
import static io.art.scheduler.test.comparator.DateTimeApproximateComparator.*;
import static java.time.Duration.*;
import static java.time.LocalDateTime.*;
import static java.util.concurrent.TimeUnit.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;

public class SchedulerTest {
    @BeforeAll
    public static void setup() {
        initialize(scheduler());
    }

    @RepeatedTest(10)
    public void testCommonSingleTask() {
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        schedule(task);
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        assertTrue(task.completed());
    }

    @RepeatedTest(10)
    public void testOneThreadSingleTask() {
        DeferredExecutor executor = deferredExecutor().poolSize(1).build();
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        executor.execute(task, now());
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        assertTrue(task.completed());
    }

    @RepeatedTest(10)
    public void testTenThreadsSingleTask() {
        DeferredExecutor executor = deferredExecutor().poolSize(10).build();
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        executor.execute(task, now());
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        assertTrue(task.completed());
    }


    @RepeatedTest(10)
    public void testCommonMultipleTasks() {
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
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        tasks.forEach(task -> assertTrue(task.completed()));
    }

    @RepeatedTest(10)
    public void testOneThreadMultipleTasks() {
        DeferredExecutor executor = deferredExecutor().poolSize(1).build();
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
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        tasks.forEach(task -> assertTrue(task.completed()));
    }

    @RepeatedTest(10)
    public void testTenThreadsMultipleTasks() {
        DeferredExecutor executor = deferredExecutor().poolSize(10).build();
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
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        tasks.forEach(task -> assertTrue(task.completed()));
    }


    @RepeatedTest(10)
    public void testCommonSingleDelayedTask() {
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        LocalDateTime time = now().plusSeconds(1);
        schedule(time, task);
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        assertTrue(task.completed());
        assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
    }

    @RepeatedTest(10)
    public void testOneThreadSingleDelayedTask() {
        DeferredExecutor executor = deferredExecutor().poolSize(1).build();
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        LocalDateTime time = now().plusSeconds(1);
        executor.execute(task, time);
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        assertTrue(task.completed());
        assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
    }

    @RepeatedTest(10)
    public void testTenThreadsSingleDelayedTask() {
        DeferredExecutor executor = deferredExecutor().poolSize(10).build();
        CountDownLatch water = new CountDownLatch(1);
        ScheduledTask task = new ScheduledTask(water);
        LocalDateTime time = now().plusSeconds(1);
        executor.execute(task, time);
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        assertTrue(task.completed());
        assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
    }


    @RepeatedTest(10)
    public void testCommonMultipleDelayedTask() {
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
        tasks.forEach(task -> schedule(time, task));
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        for (int index = 0; index < tasks.size(); index++) {
            OrderedScheduledTask task = tasks.get(index);
            assertTrue(task.completed());
            assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
            assertEquals(index, counter.getOrders().get(task));
        }
    }

    @RepeatedTest(10)
    public void testOneThreadMultipleDelayedTask() {
        DeferredExecutor executor = deferredExecutor().poolSize(1).build();
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
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        for (int index = 0; index < tasks.size(); index++) {
            OrderedScheduledTask task = tasks.get(index);
            assertTrue(task.completed());
            assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
            assertEquals(index, counter.getOrders().get(task));
        }
    }

    @RepeatedTest(10)
    public void testTenThreadsMultipleDelayedTask() {
        DeferredExecutor executor = deferredExecutor().poolSize(10).build();
        CountDownLatch water = new CountDownLatch(12);
        OrderCounter counter = new OrderCounter();
        List<OrderedScheduledTask> tasks = linkedListOf(
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
                new OrderedScheduledTask(water, counter),
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
        ignoreException(() -> await(water), exception -> System.err.println(exception.getMessage()));
        for (int index = 0; index < tasks.size(); index++) {
            OrderedScheduledTask task = tasks.get(index);
            assertTrue(task.completed());
            assertTrue(isAfterOrEqual(task.completionTimeStamp(), time, ofMillis(100)));
            assertEquals(index, counter.getOrders().get(task));
        }
    }


    @RepeatedTest(100)
    public void testOneThreadShutdown() {
        DeferredExecutor executor = deferredExecutor().poolSize(1).build();
        CountDownLatch water = new CountDownLatch(1);
        List<ScheduledTask> tasks = linkedListOf(
                new ScheduledTask(water)
        );
        tasks.forEach(task -> executor.execute(task, now()));
        executor.shutdown();
        tasks.forEach(task -> assertTrue(task.completed()));
    }

    @RepeatedTest(100)
    public void testTenThreadsShutdown() {
        DeferredExecutor executor = deferredExecutor().poolSize(10).build();
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
        DeferredExecutor firstExecutor = executor;
        tasks.forEach(task -> firstExecutor.execute(task, now()));
        executor.shutdown();
        tasks.forEach(task -> assertTrue(task.completed()));

        executor = deferredExecutor().poolSize(10).build();
        water = new CountDownLatch(12);
        tasks = linkedListOf(
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water),
                new ScheduledTask(water)
        );
        DeferredExecutor secondExecutor = executor;
        tasks.forEach(task -> secondExecutor.execute(task, now()));
        executor.shutdown();
        tasks.forEach(task -> assertTrue(task.completed()));
    }

    private void await(CountDownLatch water) throws InterruptedException {
        assertTrue(water.await(30, SECONDS));
    }
}
