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

package io.art.rsocket.interceptor;

import io.art.core.property.*;
import io.art.rsocket.configuration.*;
import io.art.rsocket.refresher.*;
import io.rsocket.*;
import io.rsocket.plugins.*;
import lombok.*;
import org.apache.logging.log4j.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.core.property.Property.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.module.RsocketModule.*;
import static lombok.AccessLevel.*;

public class RsocketServerLoggingInterceptor implements RSocketInterceptor {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketServerLoggingInterceptor.class);
    private final Property<Boolean> enabled;

    public RsocketServerLoggingInterceptor(RsocketModuleRefresher.Consumer consumer) {
        enabled = property(this::enabled).listenConsumer(consumer::serverLoggingConsumer);
    }

    @Override
    public RSocket apply(RSocket rsocket) {
        return new RsocketLoggingProxy(getLogger(), rsocket, enabled);
    }

    private boolean enabled() {
        return let(rsocketModule().configuration().getServerConfiguration(), RsocketServerConfiguration::isLogging, false);
    }
}
