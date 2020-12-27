/*
 * ART
 *
 * Copyright 2020 ART
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
import io.art.communicator.state.*;
import io.art.core.module.*;
import lombok.*;
import static io.art.communicator.configuration.CommunicatorModuleConfiguration.*;
import static io.art.core.caster.Caster.*;
import static io.art.core.context.Context.*;

@Getter
public class CommunicatorModule implements StatefulModule<CommunicatorModuleConfiguration, Configurator, CommunicatorModuleState> {
    private final String id = CommunicatorModule.class.getSimpleName();
    private final CommunicatorModuleConfiguration configuration = new CommunicatorModuleConfiguration();
    private final Configurator configurator = new Configurator(configuration);
    private final CommunicatorModuleState state = new CommunicatorModuleState();
    @Getter(lazy = true)
    private static final
    StatefulModuleProxy<CommunicatorModuleConfiguration, CommunicatorModuleState> communicatorModule = context().getStatefulModule(CommunicatorModule.class.getSimpleName());

    public static StatefulModuleProxy<CommunicatorModuleConfiguration, CommunicatorModuleState> communicatorModule() {
        return getCommunicatorModule();
    }

    public static <T> T communicator(Class<T> communicatorClass) {
        return cast(communicatorModule().configuration().getCommunicators().get(communicatorClass.getSimpleName()));
    }
}
