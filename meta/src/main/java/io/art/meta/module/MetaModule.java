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
import io.art.meta.configuration.*;
import io.art.meta.model.*;
import io.art.meta.registry.*;
import lombok.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.context.Context.*;
import static lombok.AccessLevel.*;

@Getter
public class MetaModule implements StatelessModule<MetaModuleConfiguration, MetaModuleConfiguration.Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<MetaModuleConfiguration> metaModule = context().getStatelessModule(MetaModule.class.getSimpleName());
    private final String id = MetaModule.class.getSimpleName();
    private final MetaLibrary library;
    private final MetaModuleConfiguration configuration;
    private final MetaModuleConfiguration.Configurator configurator;

    public MetaModule(MetaLibrary library) {
        this.library = library;
        configuration = new MetaModuleConfiguration(MetaClassMutableRegistry.get(), library);
        configurator = new MetaModuleConfiguration.Configurator(configuration);
    }

    public static StatelessModuleProxy<MetaModuleConfiguration> metaModule() {
        return getMetaModule();
    }

    public static ImmutableMap<Class<?>, MetaClass<?>> classes() {
        return metaModule().configuration().getClasses();
    }

    public static <T> MetaClass<T> declaration(Class<T> type) {
        return cast(classes().get(type));
    }

    public static <T extends MetaLibrary> T library() {
        return cast(metaModule().configuration().getLibrary());
    }
}
