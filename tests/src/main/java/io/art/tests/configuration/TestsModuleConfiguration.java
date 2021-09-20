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

package io.art.tests.configuration;

import io.art.core.annotation.*;
import io.art.core.collection.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.core.source.*;
import io.art.meta.model.*;
import io.art.tests.*;
import lombok.*;
import static io.art.core.property.LazyProperty.*;

@Getter
@Public
public class TestsModuleConfiguration implements ModuleConfiguration {
    private LazyProperty<ImmutableMap<MetaClass<? extends Tests>, TestSuitConfiguration>> suits = lazy(ImmutableMap::emptyImmutableMap);

    @RequiredArgsConstructor
    public static class Configurator implements ModuleConfigurator<TestsModuleConfiguration, Configurator> {
        private final TestsModuleConfiguration configuration;

        @Override
        public Configurator from(ConfigurationSource source) {
            return this;
        }

        @Override
        public Configurator initialize(TestsModuleConfiguration configuration) {
            this.configuration.suits = configuration.getSuits();
            return this;
        }
    }
}
