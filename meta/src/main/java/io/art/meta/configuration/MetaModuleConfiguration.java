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

package io.art.meta.configuration;

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.source.*;
import io.art.meta.model.*;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class MetaModuleConfiguration implements ModuleConfiguration {
    private final ImmutableMap<Class<?>, MetaClass<?>> classes;
    private final MetaLibrary library;

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<MetaModuleConfiguration, Configurator> {
        private final MetaModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            return this;
        }
    }
}
