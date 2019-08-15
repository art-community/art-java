/*
 *    Copyright 2019 ART
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ru.art.rsocket.module;

import lombok.Getter;
import ru.art.core.module.Module;
import ru.art.rsocket.configuration.RsocketModuleConfiguration;
import ru.art.rsocket.state.RsocketModuleState;
import static ru.art.core.context.Context.context;
import static ru.art.rsocket.configuration.RsocketModuleConfiguration.RsocketModuleDefaultConfiguration;
import static ru.art.rsocket.constants.RsocketModuleConstants.RSOCKET_MODULE_ID;

@Getter
public class RsocketModule implements Module<RsocketModuleConfiguration, RsocketModuleState> {
    @Getter(lazy = true)
    private static final RsocketModuleConfiguration rsocketModule = context().getModule(RSOCKET_MODULE_ID, RsocketModule::new);
    @Getter(lazy = true)
    private static final RsocketModuleState rsocketModuleState = context().getModuleState(RSOCKET_MODULE_ID, RsocketModule::new);
    private final String id = RSOCKET_MODULE_ID;
    private final RsocketModuleConfiguration defaultConfiguration = new RsocketModuleDefaultConfiguration();
    private final RsocketModuleState state = new RsocketModuleState();

    public static RsocketModuleConfiguration rsocketModule() {
        return context().getModule(RSOCKET_MODULE_ID, RsocketModule::new);
    }

    public static RsocketModuleState rsocketModuleState() {
        return context().getModuleState(RSOCKET_MODULE_ID, RsocketModule::new);
    }
}
