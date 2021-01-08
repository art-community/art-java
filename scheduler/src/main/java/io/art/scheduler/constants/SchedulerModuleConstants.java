/*
 * ART
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

package io.art.scheduler.constants;

import lombok.*;

public interface SchedulerModuleConstants {
    String REFRESHER_TASK = "REFRESHER_TASK";

    interface ConfigurationKeys {
        String CONFIGURATION_REFRESH_DURATION = "configurator.refresh.duration";
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
