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

package io.art.communicator.constants;

import reactor.core.scheduler.*;
import static io.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static java.lang.Short.MAX_VALUE;
import static reactor.core.scheduler.Schedulers.newBoundedElastic;

public interface CommunicatorModuleConstants {
    interface Defaults {
        Scheduler DEFAULT_COMMUNICATOR_SCHEDULER = newBoundedElastic(DEFAULT_THREAD_POOL_SIZE, MAX_VALUE, "communicator");
    }

    interface ConfigurationKeys {
        String COMMUNICATOR_SECTION = "communicator";
        String TARGETS_KEY = "targets";
        String LOGGING_KEY = "logging";
    }
}
