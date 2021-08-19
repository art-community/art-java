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
import io.art.resilience.constants.*;
import lombok.Builder;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.collection.ImmutableMap.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class CommunicatorActionsConfiguration {
    private boolean logging;
    private boolean deactivated;
    private ImmutableMap<String, CommunicatorActionConfiguration> actions;
    private ResilienceConfiguration resilience;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> inputDecorators;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> outputDecorators;

    public static CommunicatorActionsConfiguration from(CommunicatorRefresher refresher, CommunicatorActionsConfiguration current, ConfigurationSource source) {
        CommunicatorActionsConfiguration currentConfiguration = orElse(current, CommunicatorActionsConfiguration::defaults);
        CommunicatorActionsConfiguration configuration = CommunicatorActionsConfiguration.builder().build();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener deactivationListener = refresher.deactivationListener();
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), currentConfiguration.logging));
        configuration.deactivated = deactivationListener.emit(orElse(source.getBoolean(DEACTIVATED_KEY), currentConfiguration.deactivated));
        configuration.actions = source.getNestedMap(ACTIONS_SECTION, action -> getAction(refresher, currentConfiguration, action));
        String resilienceSection = ResilienceModuleConstants.ConfigurationKeys.RESILIENCE_SECTION;
        ResilienceConfiguration resilience = source.getNested(resilienceSection, action -> ResilienceConfiguration.from(refresher.resilienceListener(), action));
        configuration.resilience = orElse(resilience, currentConfiguration.resilience);
        return configuration;
    }

    private static CommunicatorActionConfiguration getAction(CommunicatorRefresher refresher, CommunicatorActionsConfiguration currentConfiguration, NestedConfiguration action) {
        return CommunicatorActionConfiguration.from(refresher, currentConfiguration.actions.get(action.getParent()), action);
    }

    public static CommunicatorActionsConfiguration defaults() {
        return CommunicatorActionsConfiguration.builder()
                .actions(emptyImmutableMap())
                .inputDecorators(emptyImmutableArray())
                .outputDecorators(emptyImmutableArray())
                .build();
    }
}
