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

package io.art.core.waiter;

import lombok.experimental.*;
import static io.art.core.constants.WaiterConstants.*;
import static io.art.core.handler.ExceptionHandler.*;
import static io.art.core.wrapper.ExceptionWrapper.*;
import static java.util.concurrent.TimeUnit.*;
import java.time.*;
import java.util.concurrent.*;
import java.util.function.*;

@UtilityClass
public class Waiter {
    public static boolean waitCondition(Supplier<Boolean> condition) {
        return waitCondition(DEFAULT_WAIT_CHECK_PERIOD, DEFAULT_WAIT_TIMEOUT, condition);
    }

    public static boolean waitCondition(Duration timeout, Supplier<Boolean> condition) {
        return waitCondition(DEFAULT_WAIT_CHECK_PERIOD, timeout, condition);
    }

    public static boolean waitCondition(Duration checkPeriod, Duration timeout, Supplier<Boolean> condition) {
        CountDownLatch latch = new CountDownLatch(1);
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
        scheduler.setMaximumPoolSize(1);
        scheduler.scheduleAtFixedRate(() -> check(condition, latch), 0L, checkPeriod.toMillis(), MILLISECONDS);
        return handleException(ignored -> false).call(() -> latch.await(timeout.toMillis(), MILLISECONDS));
    }

    public static void waitTime(Duration time) {
        CountDownLatch latch = new CountDownLatch(1);
        ignoreException(() -> latch.await(time.toMillis(), MILLISECONDS));
    }

    private static void check(Supplier<Boolean> condition, CountDownLatch latch) {
        if (condition.get()) latch.countDown();
    }
}
