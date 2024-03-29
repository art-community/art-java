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

package io.art.transport.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.core.property.*;
import io.art.transport.configuration.*;
import io.art.transport.configuration.TransportModuleConfiguration.*;
import io.art.transport.reactor.*;
import lombok.*;
import static io.art.core.checker.ModuleChecker.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.core.property.LazyProperty.*;
import static io.art.transport.pool.TransportPool.*;
import static reactor.util.Loggers.*;

@Getter
public class TransportModule implements StatelessModule<TransportModuleConfiguration, Configurator> {
    private static final LazyProperty<StatelessModuleProxy<TransportModuleConfiguration>> transportModule = lazy(() -> context().getStatelessModule(TRANSPORT_MODULE_ID));
    private final String id = TRANSPORT_MODULE_ID;
    private final TransportModuleConfiguration configuration = new TransportModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);

    public static StatelessModuleProxy<TransportModuleConfiguration> transportModule() {
        return transportModule.get();
    }

    @Override
    public void launch(ContextService contextService) {
        if (!withLogging()) {
            useCustomLoggers(EmptyReactorLogger::new);
        }
        configureCommonTransportPool(configuration.getCommonPoolConfiguration());
    }
}
