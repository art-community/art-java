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

package io.art.meta.module;

import io.art.core.annotation.*;
import io.art.core.module.*;
import io.art.meta.configuration.*;
import io.art.meta.configuration.MetaModuleConfiguration.*;
import io.art.meta.model.*;
import lombok.*;

@ForGenerator
public class MetaInitializer implements ModuleInitializer<MetaModuleConfiguration, Configurator, MetaModule> {
    private final Initial configuration;

    public MetaInitializer(MetaLibrary library) {
        configuration = new Initial(library);
    }

    @Override
    public MetaModuleConfiguration initialize(MetaModule module) {
        return configuration;
    }

    @Getter
    private static class Initial extends MetaModuleConfiguration {
        public Initial(MetaLibrary library) {
            super(library);
        }
    }
}
