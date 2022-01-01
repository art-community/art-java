/*
 * ART
 *
 * Copyright 2019-2022 ART
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
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.meta.model.*;
import io.art.meta.registry.*;
import lombok.*;
import lombok.experimental.*;
import static io.art.core.collection.ImmutableMap.emptyImmutableMap;
import static io.art.core.property.LazyProperty.*;

@Getter
@Accessors
public class MetaModuleConfiguration implements ModuleConfiguration {
    private LazyProperty<? extends MetaLibrary> library;
    private LazyProperty<ImmutableArray<? extends MetaLibrary>> dependencies;
    private CustomMetaTypeRegistry customTypes;
    private CustomMetaTransformerRegistry customTransformers;

    public MetaLibrary library() {
        return library.get();
    }

    public MetaModuleConfiguration(LazyProperty<? extends MetaLibrary> library) {
        this.library = library;
        dependencies = lazy(ImmutableArray::emptyImmutableArray);
        customTransformers = new CustomMetaTransformerRegistry(emptyImmutableMap());
        customTypes = new CustomMetaTypeRegistry(emptyImmutableMap());
    }

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<MetaModuleConfiguration, Configurator> {
        private final MetaModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            return this;
        }

        @Override
        public Configurator initialize(MetaModuleConfiguration configuration) {
            this.configuration.library = lazy(configuration::library);
            this.configuration.customTypes = configuration.getCustomTypes();
            this.configuration.customTransformers = configuration.getCustomTransformers();
            this.configuration.dependencies = configuration.getDependencies();
            return this;
        }
    }
}
