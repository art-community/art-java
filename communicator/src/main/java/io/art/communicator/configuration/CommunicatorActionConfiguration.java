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

package io.art.communicator.configuration;

import io.art.communicator.refresher.*;
import io.art.core.changes.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.resilience.configuration.*;
import io.art.transport.payload.*;
import lombok.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.resilience.constants.ResilienceModuleConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.*;
import java.util.function.*;

@Getter
public class CommunicatorActionConfiguration {
    private boolean logging;
    private boolean deactivated;
    private ResilienceConfiguration resilienceConfiguration;
    private ImmutableMap<String, String> connectors;
    private Function<DataFormat, TransportPayloadReader> reader;
    private Function<DataFormat, TransportPayloadWriter> writer;

    public static CommunicatorActionConfiguration from(CommunicatorRefresher refresher, ConfigurationSource source) {
        CommunicatorActionConfiguration configuration = new CommunicatorActionConfiguration();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener deactivationListener = refresher.deactivationListener();
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), false));
        configuration.deactivated = deactivationListener.emit(orElse(source.getBoolean(DEACTIVATED_KEY), false));
        configuration.connectors = source.getNestedMap(CONNECTORS_KEY, NestedConfiguration::asString);
        configuration.resilienceConfiguration = source.getNested(RESILIENCE_SECTION, action -> ResilienceConfiguration.from(refresher.resilienceListener(), action));
        configuration.reader = TransportPayloadReader::new;
        configuration.writer = TransportPayloadWriter::new;
        return configuration;
    }
}
