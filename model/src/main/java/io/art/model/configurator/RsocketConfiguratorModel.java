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

package io.art.model.configurator;

import io.art.rsocket.configuration.*;
import lombok.*;

public class RsocketConfiguratorModel {
    @Getter
    private final RsocketConfiguratorModel.CustomRsocketModuleConfiguration configuration = new CustomRsocketModuleConfiguration();


    public RsocketConfiguratorModel activateServer() {
        configuration.activateServer = true;
        return this;
    }

    @Getter
    public static class CustomRsocketModuleConfiguration extends RsocketModuleConfiguration {
        private boolean activateServer;
    }
}
