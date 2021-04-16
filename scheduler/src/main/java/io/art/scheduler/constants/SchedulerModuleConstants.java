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

package io.art.scheduler.constants;

import lombok.*;
import static java.lang.Integer.*;

public interface SchedulerModuleConstants {
    String REFRESHER_TASK = "REFRESHER_TASK";

    enum PeriodicTaskMode {
        FIXED,
        DELAYED
    }


    interface Defaults {
        int DEFAULT_MAX_QUEUE_SIZE = MAX_VALUE - 8;
        long DEFAULT_SHUTDOWN_TIMEOUT = 60 * 1000;
    }

    interface LoggingMessages {
        String DEFERRED_TASK_SUBMITTED = "Deferred task submitted at {0}";
        String PERIODIC_TASK_SUBMITTED = "Periodic task submitted: {0} - at {1} every {2}";
        String PERIODIC_TASK_CANCELED = "Periodic task canceled: {0}";
    }

    interface ConfigurationKeys {
        String CONFIGURATOR_REFRESH_DURATION = "configurator.refresh.duration";
    }

    interface ExceptionMessages {
        String EXCEPTION_OCCURRED_DURING = "Exception occurred during ''{0}'': {1}";
        String AWAIT_TERMINATION_EXCEPTION = "Await termination failed";

        @Getter
        @AllArgsConstructor
        enum ExceptionEvent {
            TASK_EXECUTION("task execution"),
            TASK_OBSERVING("task observing"),
            POOL_SHUTDOWN("pool shutdown");

            private final String message;
        }
    }

}
