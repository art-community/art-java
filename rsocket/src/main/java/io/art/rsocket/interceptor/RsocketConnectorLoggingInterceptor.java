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

import io.rsocket.*;
import io.rsocket.plugins.*;
import io.rsocket.util.*;
import lombok.*;
import org.apache.logging.log4j.*;
import static io.art.core.checker.NullityChecker.*;
import static io.art.logging.LoggingModule.*;
import static io.art.rsocket.module.RsocketModule.*;
import static lombok.AccessLevel.*;

@RequiredArgsConstructor
public class RsocketConnectorLoggingInterceptor implements RSocketInterceptor {
    @Getter(lazy = true, value = PRIVATE)
    private static final Logger logger = logger(RsocketConnectorLoggingInterceptor.class);
    private final String connectorId;

    @Override
    public RSocket apply(RSocket rsocket) {
        if (!enabled()) {
            return new RSocketProxy(rsocket);
        }
        return new RsocketLoggingProxy(getLogger(), rsocket);
    }

    private boolean enabled() {
        return let(rsocketModule().configuration().getCommunicatorConfiguration(), configuration -> configuration.isLogging(connectorId), false);
    }
}
