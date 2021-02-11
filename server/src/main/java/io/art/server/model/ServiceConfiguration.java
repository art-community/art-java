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

import io.art.core.changes.*;
import io.art.core.collection.*;
import io.art.core.source.*;
import io.art.server.refresher.*;
import lombok.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.server.constants.ServerModuleConstants.ConfigurationKeys.*;

@Getter
public class ServiceConfiguration {
    private boolean deactivated;
    private boolean logging;
    private boolean validating;
    private ImmutableMap<String, ServiceMethodConfiguration> methods;

    public static ServiceConfiguration from(ServerModuleRefresher refresher, ConfigurationSource source) {
        ServiceConfiguration configuration = new ServiceConfiguration();
        ChangesListener deactivationListener = refresher.deactivationListener();
        ChangesListener loggingListener = refresher.loggingListener();
        ChangesListener validationListener = refresher.validationListener();
        configuration.deactivated = deactivationListener.emit(orElse(source.getBool(DEACTIVATED_KEY), false));
        configuration.logging = loggingListener.emit(orElse(source.getBool(LOGGING_KEY), true));
        configuration.validating = validationListener.emit(orElse(source.getBool(VALIDATING_KEY), true));
        configuration.methods = source.getNestedMap(METHODS_KEY, method -> ServiceMethodConfiguration.from(refresher, source));
        return configuration;
    }
}
