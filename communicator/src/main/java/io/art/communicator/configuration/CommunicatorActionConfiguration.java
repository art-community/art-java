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

package io.art.communicator.configuration;

import io.art.communicator.refresher.*;
import io.art.core.changes.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.resilience.configuration.*;
import lombok.*;
import reactor.core.scheduler.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.ConfigurationKeys.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.Defaults.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.resilience.constants.ResilienceModuleConstants.ConfigurationKeys.RESILIENCE_SECTION;

@Getter
public class CommunicatorActionConfiguration {
    private boolean logging;
    private boolean deactivated;
    private Scheduler scheduler;
    private ResilienceConfiguration resilienceConfiguration;
    private ImmutableMap<String, String> connectors;

    public static CommunicatorActionConfiguration from(CommunicatorModuleRefresher refresher, ConfigurationSource source) {
        CommunicatorActionConfiguration configuration = new CommunicatorActionConfiguration();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener deactivationListener = refresher.deactivationListener();
        configuration.logging = loggingListener.emit(orElse(source.getBool(LOGGING_KEY), false));
        configuration.deactivated = deactivationListener.emit(orElse(source.getBool(DEACTIVATED_KEY), false));
        configuration.scheduler = DEFAULT_COMMUNICATOR_SCHEDULER;
        configuration.connectors = source.getNestedMap(CONNECTORS_KEY, NestedConfiguration::asString);
        configuration.resilienceConfiguration = source.getNested(RESILIENCE_SECTION, action -> ResilienceConfiguration.from(refresher.resilienceListener(), action));
        return configuration;
    }
}
