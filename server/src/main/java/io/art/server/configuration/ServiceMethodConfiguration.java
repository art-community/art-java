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

package io.art.server.configuration;

import io.art.core.changes.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.server.refresher.*;
import lombok.Builder;
import lombok.*;
import reactor.core.publisher.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.server.constants.ServerConstants.ConfigurationKeys.*;
import static io.art.transport.constants.TransportModuleConstants.ConfigurationKeys.*;
import java.util.function.*;

@Getter
@Builder(toBuilder = true)
public class ServiceMethodConfiguration {
    private Boolean deactivated;
    private Boolean logging;
    private Boolean validating;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> inputDecorators;
    private final ImmutableArray<UnaryOperator<Flux<Object>>> outputDecorators;

    public static ServiceMethodConfiguration serviceMethodConfiguration(ServerRefresher refresher, ServiceMethodConfiguration current, ConfigurationSource source) {
        current = orElse(current, ServiceMethodConfiguration::defaults);
        ServiceMethodConfiguration configuration = current.toBuilder().build();
        ChangesListener deactivationListener = refresher.deactivationListener();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener validationListener = refresher.validationListener();
        configuration.deactivated = deactivationListener.emit(orElse(source.getBoolean(DEACTIVATED_KEY), current.deactivated));
        configuration.logging = loggingListener.emit(orElse(source.getBoolean(LOGGING_KEY), current.logging));
        configuration.validating = validationListener.emit(orElse(source.getBoolean(VALIDATING_KEY), current.validating));
        return configuration;
    }

    public static ServiceMethodConfiguration defaults() {
        return ServiceMethodConfiguration.builder()
                .inputDecorators(emptyImmutableArray())
                .outputDecorators(emptyImmutableArray())
                .build();
    }
}
