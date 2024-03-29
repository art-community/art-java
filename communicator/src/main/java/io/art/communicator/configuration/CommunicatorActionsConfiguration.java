/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import lombok.Builder;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.communicator.configuration.CommunicatorActionConfiguration.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableMap.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class CommunicatorActionsConfiguration {
    private Boolean logging;
    private Boolean deactivated;
    private ImmutableMap<String, CommunicatorActionConfiguration> actions;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> inputDecorators;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> outputDecorators;

    public static CommunicatorActionsConfiguration communicatorActionsConfiguration(CommunicatorRefresher refresher, CommunicatorActionsConfiguration current, ConfigurationSource source) {
        CommunicatorActionsConfiguration currentConfiguration = orElse(current, CommunicatorActionsConfiguration::defaults);
        CommunicatorActionsConfiguration configuration = CommunicatorActionsConfiguration.builder().build();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener deactivationListener = refresher.deactivationListener();
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), currentConfiguration.logging));
        configuration.deactivated = deactivationListener.emit(orElse(source.getBoolean(DEACTIVATED_KEY), currentConfiguration.deactivated));
        configuration.actions = source.getNestedMap(ACTIONS_SECTION, action -> getAction(refresher, currentConfiguration, action));
        return configuration;
    }

    private static CommunicatorActionConfiguration getAction(CommunicatorRefresher refresher, CommunicatorActionsConfiguration currentConfiguration, NestedConfiguration action) {
        return communicatorActionConfiguration(refresher, currentConfiguration.actions.get(action.getParent()), action);
    }

    public static CommunicatorActionsConfiguration defaults() {
        return CommunicatorActionsConfiguration.builder()
                .actions(emptyImmutableMap())
                .inputDecorators(emptyImmutableArray())
                .outputDecorators(emptyImmutableArray())
                .build();
    }
}
