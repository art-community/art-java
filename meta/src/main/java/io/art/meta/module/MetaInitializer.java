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

import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.meta.configuration.*;
import io.art.meta.configuration.MetaModuleConfiguration.*;
import io.art.meta.model.*;
import io.art.meta.registry.*;
import io.art.meta.transformer.*;
import lombok.*;
import static io.art.core.collection.ImmutableArray.*;
import static io.art.core.factory.ListFactory.*;
import static io.art.core.property.LazyProperty.*;
import java.util.*;
import java.util.function.*;

public class MetaInitializer implements ModuleInitializer<MetaModuleConfiguration, Configurator, MetaModule> {
    private final Initial configuration;

    public MetaInitializer(LazyProperty<? extends MetaLibrary> library) {
        configuration = new Initial(library);
    }

    public MetaInitializer registerCustomType(MetaType<?> type) {
        configuration.customTypes.register(type);
        return this;
    }

    public MetaInitializer registerCustomTransformers(Class<?> type, CustomTransformers transformers) {
        configuration.customTransformers.register(type, transformers);
        return this;
    }

    public MetaInitializer registerLibrary(Supplier<? extends MetaLibrary> provider) {
        configuration.dependencies.add(lazy(provider));
        return this;
    }

    @Override
    public MetaModuleConfiguration initialize(MetaModule module) {
        return configuration;
    }

    @Getter
    private static class Initial extends MetaModuleConfiguration {
        private final CustomMetaTypeRegistrator customTypes;
        private final CustomMetaTransformerRegistrator customTransformers;
        private final List<LazyProperty<? extends MetaLibrary>> dependencies;

        public Initial(LazyProperty<? extends MetaLibrary> library) {
            super(library);
            customTransformers = new CustomMetaTransformerRegistrator();
            customTypes = new CustomMetaTypeRegistrator();
            dependencies = linkedList();
        }

        public LazyProperty<ImmutableArray<? extends MetaLibrary>> getDependencies() {
            return lazy(() -> dependencies.stream().map(LazyProperty::get).collect(immutableArrayCollector()));
        }

        public CustomMetaTypeRegistry getCustomTypes() {
            return new CustomMetaTypeRegistry(customTypes.getRegistry());
        }

        public CustomMetaTransformerRegistry getCustomTransformers() {
            return new CustomMetaTransformerRegistry(customTransformers.registry());
        }
    }
}
