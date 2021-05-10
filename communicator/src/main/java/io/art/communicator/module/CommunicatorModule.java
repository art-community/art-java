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

package io.art.communicator.module;

import io.art.communicator.configuration.*;
import io.art.communicator.exception.*;
import io.art.communicator.proxy.*;
import io.art.communicator.refresher.*;
import io.art.communicator.state.*;
import io.art.core.caster.*;
import io.art.core.module.*;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorModuleConfiguration.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.ExceptionMessages.*;
import static io.art.communicator.constants.CommunicatorModuleConstants.LoggingMessages.*;
import static io.art.core.constants.StringConstants.*;
import static io.art.core.context.Context.*;
import static io.art.core.extensions.StringExtensions.*;
import static io.art.core.factory.SetFactory.*;
import static java.text.MessageFormat.*;
import static lombok.AccessLevel.*;
import java.util.*;

@Getter
public class CommunicatorModule implements StatefulModule<CommunicatorModuleConfiguration, Configurator, CommunicatorModuleState> {
    private final String id = CommunicatorModule.class.getSimpleName();
    private final CommunicatorModuleRefresher refresher = new CommunicatorModuleRefresher();
    private final CommunicatorModuleConfiguration configuration = new CommunicatorModuleConfiguration(refresher);
    private final Configurator configurator = new Configurator(configuration);
    private final CommunicatorModuleState state = new CommunicatorModuleState();
    @Getter(lazy = true, value = PRIVATE)
    private static final
    StatefulModuleProxy<CommunicatorModuleConfiguration, CommunicatorModuleState> communicatorModule = context().getStatefulModule(CommunicatorModule.class.getSimpleName());

    static {
        registerDefault(CommunicatorModule.class.getSimpleName(), CommunicatorModule::new);
    }

    public static StatefulModuleProxy<CommunicatorModuleConfiguration, CommunicatorModuleState> communicatorModule() {
        return getCommunicatorModule();
    }

    public static <T> T communicator(Class<T> communicatorClass) {
        return communicator(communicatorClass.getSimpleName());
    }

    public static <T> T communicator(String id) {
        return communicatorModule()
                .configuration()
                .getRegistry()
                .get(id)
                .map(Caster::<T>cast)
                .orElseThrow(() -> new CommunicatorModuleException(format(COMMUNICATOR_WAS_NOT_REGISTERED, id)));
    }

    @Override
    public String print() {
        Set<String> messages = set();
        for (Map.Entry<String, CommunicatorProxy> entry : configuration.getRegistry().getProxies().entrySet()) {
            Set<String> methods = entry.getValue().getActions().keySet();
            messages.add(format(COMMUNICATOR_REGISTRATION_MESSAGE, entry.getKey(), toCommaDelimitedString(methods)));
        }
        return toDelimitedString(messages, NEW_LINE);
    }
}
