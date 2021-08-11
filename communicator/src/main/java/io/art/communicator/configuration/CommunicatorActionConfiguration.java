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
import lombok.Builder;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.communicator.constants.CommunicatorConstants.ConfigurationKeys.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.resilience.constants.ResilienceModuleConstants.ConfigurationKeys.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class CommunicatorActionConfiguration {
    private boolean logging;
    private boolean deactivated;
    private ResilienceConfiguration resilience;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> inputDecorators;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> outputDecorators;

    public static CommunicatorActionConfiguration from(CommunicatorRefresher refresher, CommunicatorActionConfiguration current, ConfigurationSource source) {
        CommunicatorActionConfiguration currentConfiguration = orElse(current, CommunicatorActionConfiguration::defaults);
        CommunicatorActionConfiguration configuration = CommunicatorActionConfiguration.builder().build();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener deactivationListener = refresher.deactivationListener();
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), current.logging));
        configuration.deactivated = deactivationListener.emit(orElse(source.getBoolean(DEACTIVATED_KEY), current.deactivated));
        ResilienceConfiguration resilience = source.getNested(RESILIENCE_SECTION, action -> ResilienceConfiguration.from(refresher.resilienceListener(), action));
        configuration.resilience = orElse(resilience, currentConfiguration.resilience);
        return configuration;
    }

    public static CommunicatorActionConfiguration defaults() {
        return CommunicatorActionConfiguration.builder()
                .inputDecorators(emptyImmutableArray())
                .outputDecorators(emptyImmutableArray())
                .build();
    }
}
