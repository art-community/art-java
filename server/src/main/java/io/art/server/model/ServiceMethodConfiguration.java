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

package io.art.server.model;

import io.art.core.constants.*;
import io.art.core.source.*;
import lombok.*;
import reactor.core.scheduler.*;
import reactor.util.concurrent.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.constants.ThreadConstants.DEFAULT_THREAD_POOL_SIZE;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;
import static io.art.server.constants.ServerModuleConstants.Defaults.DEFAULT_SERVICE_METHOD_SCHEDULER;
import static java.lang.Short.MAX_VALUE;
import static reactor.core.scheduler.Schedulers.newBoundedElastic;

@Getter
@AllArgsConstructor
public class ServiceMethodConfiguration {
    private final boolean deactivated;
    private final boolean logging;
    private final Scheduler scheduler;

    public static ServiceMethodConfiguration from(ConfigurationSource source) {
        boolean deactivated = orElse(source.getBool(DEACTIVATED_KEY), false);
        boolean logging = orElse(source.getBool(LOGGING_KEY), false);
        Scheduler scheduler = DEFAULT_SERVICE_METHOD_SCHEDULER;
        return new ServiceMethodConfiguration(deactivated, logging, scheduler);
    }
}
