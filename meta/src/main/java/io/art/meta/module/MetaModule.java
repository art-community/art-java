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

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.meta.configuration.*;
import io.art.meta.model.*;
import lombok.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static lombok.AccessLevel.*;

@Getter
public class MetaModule implements StatelessModule<MetaModuleConfiguration, MetaModuleConfiguration.Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<MetaModuleConfiguration> metaModule = context().getStatelessModule(META_MODULE_ID);
    private final String id = META_MODULE_ID;
    private final MetaModuleConfiguration configuration;
    private final MetaModuleConfiguration.Configurator configurator;

    public MetaModule(LazyProperty<? extends MetaLibrary> library) {
        configuration = new MetaModuleConfiguration(library);
        configurator = new MetaModuleConfiguration.Configurator(configuration);
    }

    @Override
    public void load(Context.Service contextService) {
        configuration.library().compute(configuration);
    }

    public static StatelessModuleProxy<MetaModuleConfiguration> metaModule() {
        return getMetaModule();
    }
}
