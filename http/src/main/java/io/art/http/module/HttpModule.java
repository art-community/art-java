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

package io.art.http.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.http.configuration.*;
import io.art.http.manager.*;
import io.art.http.refresher.*;
import io.art.http.state.*;
import lombok.*;
import static io.art.core.constants.EmptyFunctions.*;
import static io.art.core.context.Context.*;
import static io.art.http.configuration.HttpModuleConfiguration.*;
import static lombok.AccessLevel.*;
import static reactor.core.publisher.Hooks.*;

@Getter
public class HttpModule implements StatefulModule<HttpModuleConfiguration, Configurator, HttpModuleState> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatefulModuleProxy<HttpModuleConfiguration, HttpModuleState> httpModule = context().getStatefulModule(HttpModule.class.getSimpleName());
    private final String id = HttpModule.class.getSimpleName();
    private final HttpModuleState state = new HttpModuleState();
    private final HttpModuleRefresher refresher = new HttpModuleRefresher();
    private final HttpModuleConfiguration configuration = new HttpModuleConfiguration(refresher);
    private final HttpManager manager = new HttpManager(refresher, configuration);
    private final Configurator configurator = new Configurator(configuration);

    public static StatefulModuleProxy<HttpModuleConfiguration, HttpModuleState> httpModule() {
        return getHttpModule();
    }

    @Override
    public void launch(Context.Service contextService) {
        onErrorDropped(emptyConsumer());
        if (configuration.isEnableServer()) {
            manager.initializeServer();
        }
        if (!configuration.getConnectors().isEmpty()) {
            manager.initializeCommunicators();
        }
    }

    @Override
    public void shutdown(Context.Service contextService) {
        if (configuration.isEnableServer()) {
            manager.disposeServer();
        }
        if (!configuration.getConnectors().isEmpty()) {
            manager.disposeCommunicators();
        }
    }
}
