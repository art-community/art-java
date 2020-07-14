/*
 * ART Java
 *
 * Copyright 2019 ART
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

package io.art.config.state;

import lombok.*;
import lombok.experimental.*;
import io.art.core.module.*;
import static io.art.config.state.ConfigModuleState.ConfigurationMode.*;
import static io.art.core.constants.StringConstants.*;

@Getter
@Setter
@Accessors(fluent = true)
public class ConfigModuleState implements ModuleState {
    private volatile ConfigurationMode configurationMode = FILE;
    private volatile String localConfigUrl = EMPTY_STRING;
    private volatile RemoteConfigConnectionProperties remoteConfigProperties = RemoteConfigConnectionProperties.builder().build();

    public enum ConfigurationMode {
        FILE,
        REMOTE
    }

    @Getter
    @Builder
    public static class RemoteConfigConnectionProperties {
        private final String host;
        private final int port;
        private final String path;

        @Override
        public String toString() {
            return "host: " + host + ", port: " + port + ", path: " + path;
        }
    }
}
