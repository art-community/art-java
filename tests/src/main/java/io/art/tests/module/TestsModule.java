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

package io.art.tests.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.tests.configuration.*;
import lombok.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.tests.invoker.TestSuitInvoker.*;

@Getter
public class TestsModule implements StatelessModule<TestsModuleConfiguration, TestsModuleConfiguration.Configurator> {
    private static final LazyProperty<StatelessModuleProxy<TestsModuleConfiguration>> testsModule = lazy(() -> context().getStatelessModule(TESTS_MODULE_ID));
    private final String id = TESTS_MODULE_ID;
    private final TestsModuleConfiguration configuration = new TestsModuleConfiguration();
    private final TestsModuleConfiguration.Configurator configurator = new TestsModuleConfiguration.Configurator(configuration);

    public static StatelessModuleProxy<TestsModuleConfiguration> testsModule() {
        return testsModule.get();
    }

    @Override
    public void launch(ContextService contextService) {
        for (TestSuitConfiguration suit : configuration.getSuits().get().values()) {
            invokeTestSuit(suit);
        }
    }
}
