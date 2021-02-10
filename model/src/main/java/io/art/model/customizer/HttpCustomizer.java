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

package io.art.model.customizer;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.http.configuration.*;
import io.art.http.module.*;
import io.art.http.refresher.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.module.*;
import io.art.rsocket.refresher.*;
import lombok.*;

@Getter
@UsedByGenerator
public class HttpCustomizer {
    private final Custom configuration;

    public HttpCustomizer(HttpModule module) {
        this.configuration = new Custom(module.getRefresher());
    }

    public HttpCustomizer services(ImmutableMap<String, HttpServiceConfiguration> services) {
        configuration.serverConfiguration = HttpServerConfiguration.defaults().toBuilder().services(services).build();
        return this;
    }

    public HttpCustomizer activateServer() {
        configuration.activateServer = true;
        return this;
    }

    public HttpCustomizer activateCommunicator() {
        configuration.activateCommunicator = true;
        return this;
    }

    @Getter
    public static class Custom extends HttpModuleConfiguration {
        private HttpServerConfiguration serverConfiguration;
        private boolean activateServer;
        private boolean activateCommunicator;

        public Custom(HttpModuleRefresher refresher) {
            super(refresher);
        }
    }
}
