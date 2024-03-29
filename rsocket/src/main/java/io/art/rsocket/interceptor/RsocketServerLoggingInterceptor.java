/*
 * ART
 *
 * Copyright 2019-2022 ART
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.art.rsocket.interceptor;

import io.art.core.property.*;
import io.art.logging.logger.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.configuration.server.*;
import io.rsocket.*;
import io.rsocket.plugins.*;
import static io.art.core.property.Property.*;
import static io.art.logging.Logging.*;
import static io.art.rsocket.constants.RsocketModuleConstants.Messages.*;

public class RsocketServerLoggingInterceptor implements RSocketInterceptor {
    private final Logger logger;
    private final RsocketCommonServerConfiguration serverConfiguration;
    private final Property<Boolean> enabled;

    public RsocketServerLoggingInterceptor(RsocketModuleConfiguration moduleConfiguration, RsocketCommonServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
        logger = logger(RSOCKET_SERVER_LOGGER);
        enabled = property(this::enabled).listenConsumer(() -> moduleConfiguration
                .getConsumer()
                .serverLoggingConsumer());
    }

    @Override
    public RSocket apply(RSocket rsocket) {
        return new RsocketLoggingProxy(logger, rsocket, enabled);
    }

    private boolean enabled() {
        return serverConfiguration.isVerbose();
    }
}
