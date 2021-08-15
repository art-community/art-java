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

package io.art.transport.module;

import io.art.core.context.*;
import io.art.core.module.*;
import io.art.transport.configuration.*;
import io.art.transport.configuration.TransportModuleConfiguration.*;
import lombok.*;
import static io.art.core.constants.ModuleIdentifiers.*;
import static io.art.core.context.Context.*;
import static io.art.transport.constants.TransportModuleConstants.Messages.*;
import static io.art.transport.pool.TransportPool.*;
import static lombok.AccessLevel.*;

@Getter
public class TransportModule implements StatelessModule<TransportModuleConfiguration, Configurator> {
    @Getter(lazy = true, value = PRIVATE)
    private static final StatelessModuleProxy<TransportModuleConfiguration> transportModule = context().getStatelessModule(TRANSPORT_MODULE_ID);
    private final String id = TRANSPORT_MODULE_ID;
    private final TransportModuleConfiguration configuration = new TransportModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);

    public static StatelessModuleProxy<TransportModuleConfiguration> transportModule() {
        return getTransportModule();
    }

    @Override
    public void launch(Context.Service contextService) {
        configureCommonTransportPool(configuration.getCommonPoolConfiguration());
    }

    @Override
    public void shutdown(Context.Service contextService) {
        shutdownCommonTransportPool();
    }

    @Override
    public String print() {
        return TRANSPORT_CONFIGURING_MESSAGE;
    }
}
